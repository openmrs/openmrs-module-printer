package org.openmrs.module.printer;

import org.junit.Ignore;
import org.junit.Test;
import org.openmrs.module.printer.handler.PrintHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;

@Ignore
public class PrintThreadTest {

    @Test
    public void shouldPrintAndBlockViaThread() throws Exception {

        Printer printer = new Printer();
        printer.setIpAddress("127.0.0.1") ;
        printer.setPort("9100");

        Lock printerLock = new ReentrantLock();

        Map<String, Object> paramMap1 = new HashMap<String, Object>();
        paramMap1.put("wait", 10000);
        Map<String, Object> paramMap2 = new HashMap<String, Object>();
        paramMap2.put("wait", 1000);


        Thread thread1 = new Thread(new PrintThread(printer, paramMap1, printerLock, new MockPrintHandler()));
        Thread thread2 = new Thread(new PrintThread(printer, paramMap2, printerLock, new MockPrintHandler()));

        thread1.start();
        thread2.start();

        // wait for the second thread to finish
        thread2.join();

        // if the synchronization is not working right, thread2 will terminate before thread1 since thread1 has a ten-second delay and thread 2 only has a 1 second delay
        assertThat(thread1.getState(), is(Thread.State.TERMINATED));

    }

    public class MockPrintHandler implements PrintHandler {

        @Override
        public String getDisplayName() {
            return "Mock Print Handler";
        }

        @Override
        public String getBeanName() {
            return "mockPrinterHandler";
        }

        @Override
        public void print(Printer printer, Map<String, Object> paramMap) throws UnableToPrintException {
            // just sleep
            if (paramMap.get("wait") != null) {
                try {
                    Thread.sleep((Integer) paramMap.get("wait"));
                }
                catch (Exception e) {
                    throw new RuntimeException("Error", e);
                }
            }
        }
    }
}
