package org.openmrs.module.printer;

import junit.framework.Assert;
import org.junit.Test;
import org.openmrs.module.printer.handler.PrintHandler;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;

import java.util.Collection;
import java.util.Map;

public class PrintHandlerUnregisterComponentTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private PrinterService printerService;

    @Test
    @DirtiesContext
    public void shouldUnregisterPrintHandler() {
        printerService.unregisterPrintHandler(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME);
        Collection<PrintHandler> printHandlers = printerService.getRegisteredPrintHandlers();
        Assert.assertEquals(0, printHandlers.size());
    }

    public class MockPrintHandler implements PrintHandler {

        @Override
        public String getDisplayName() {
            return "Mock Print Handler";
        }

        @Override
        public String getBeanName() {
            return "mockPrintHandler";
        }

        @Override
        public void print(Printer printer, Map<String, Object> paramMap) throws UnableToPrintException {
            // do nothing
        }
    }
}
