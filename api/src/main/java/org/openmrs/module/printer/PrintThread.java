package org.openmrs.module.printer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.printer.handler.PrintHandler;

import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;

/**
 * Implementing socket printing in a separate thread so that it can be performed asynchronously
 */
public class PrintThread implements Runnable {

    private final Log log = LogFactory.getLog(getClass());

    private Printer printer;

    private Map<String,Object> paramMap;

    private Lock printerLock;

    private PrintHandler printHandler;

    public PrintThread(Printer printer, Map<String, Object> paramMap, Lock printerLock, PrintHandler printHandler) {
        this.printer = printer;
        this.paramMap = paramMap;
        this.printerLock = printerLock;
        this.printHandler = printHandler;
    }

    @Override
    public void run() {

        try {
            print();
        }
        catch (UnableToPrintException e) {
            throw new RuntimeException("Thread unable to print", e);
        }

    }

    public void print() throws UnableToPrintException {
        // only allow one call to a printer at time
        boolean hasLock = false;
        try {
            hasLock = printerLock.tryLock(1, TimeUnit.MINUTES);
            if (hasLock) {
                log.info("Locking printer " + printer.getName() + " with lock " + printerLock);
                printHandler.print(printer, paramMap);
            }
            else {
                log.error("Unable to lock printer " + printer.getName() + " with lock " + printerLock);
            }
        }
        catch (InterruptedException e) {
            log.error("Interrupted while trying to lock printer");
        }
        finally {
            if (hasLock) {
                log.info("Unlocking printer "  + printer.getName() + " with lock " + printerLock);
                printerLock.unlock();
            }
        }
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public void setPrinterLock(Lock printerLock) {
        this.printerLock = printerLock;
    }

    public void setPrintHandler(PrintHandler printHandler) {
        this.printHandler = printHandler;
    }
}
