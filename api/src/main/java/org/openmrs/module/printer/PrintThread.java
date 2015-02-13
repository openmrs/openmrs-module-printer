package org.openmrs.module.printer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.printer.handler.PrintHandler;

import java.util.Map;

/**
 * Implementing socket printing in a separate thread so that it can be performed asynchronously
 */
public class PrintThread implements Runnable {

    private final Log log = LogFactory.getLog(getClass());

    private Printer printer;

    private Map<String,Object> paramMap;

    private Object printerLock;

    private PrintHandler printHandler;

    public PrintThread(Printer printer, Map<String, Object> paramMap, Object printerLock, PrintHandler printHandler) {
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
        synchronized (printerLock) {
            printHandler.print(printer, paramMap);
        }
    }

    public void setPrinter(Printer printer) {
        this.printer = printer;
    }

    public void setParamMap(Map<String, Object> paramMap) {
        this.paramMap = paramMap;
    }

    public void setPrinterLock(Object printerLock) {
        this.printerLock = printerLock;
    }

    public void setPrintHandler(PrintHandler printHandler) {
        this.printHandler = printHandler;
    }
}
