package org.openmrs.module.printer;

import junit.framework.Assert;
import org.junit.Test;
import org.openmrs.module.printer.handler.PrintHandler;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

// note that these tests don't follow good practice... they must be run in order and dirty the context--we used to use @DirtiesContext as a way around this, but this isn't working after upgrading to 2.x
public class PrintHandlerRegisterComponentTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private PrinterService printerService;


    @Test
    public void shouldAddNewPrintHandler() {
        printerService.registerPrintHandler(new MockPrintHandler());
        Collection<PrintHandler> printHandlers = printerService.getRegisteredPrintHandlers();
        Assert.assertEquals(2, printHandlers.size());


        List<String> handlerNames = new ArrayList<String>();
        Iterator<PrintHandler> i = printHandlers.iterator();
        while (i.hasNext()) {
            handlerNames.add(i.next().getBeanName());
        }

        Assert.assertTrue(handlerNames.contains("mockPrintHandler"));
        Assert.assertTrue(handlerNames.contains("socketPrintHandler"));
    }

    @Test
    public void shouldUnregisterPrintHandler() {
        Collection<PrintHandler> printHandlers = printerService.getRegisteredPrintHandlers();
        Assert.assertEquals(2, printHandlers.size());  // expects the print handler registered in the previous test to still be there
        printerService.unregisterPrintHandler("mockPrintHandler");
        Assert.assertEquals(1, printHandlers.size());
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
