package org.openmrs.module.printer;

import org.junit.Before;
import org.junit.Test;
import org.openmrs.Person;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.printer.handler.SocketPrintHandler;
import org.openmrs.module.printer.validator.PrinterModelValidator;
import org.springframework.validation.BindException;
import org.springframework.validation.Errors;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class PrinterModelValidatorTest {

    private PrinterModelValidator validator;

    private PrinterService printerService;

    private PrinterModel printerModel;

    @Before
    public void setValidator() {
        validator = new PrinterModelValidator();
        validator.setMessageSourceService(mock(MessageSourceService.class));
        printerService = mock(PrinterService.class);
        validator.setPrinterService(printerService);
        when(printerService.getRegisteredPrintHandlerByName(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME)).thenReturn(new SocketPrintHandler());

        printerModel = new PrinterModel();
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_shouldThrowExceptionIfNull() throws Exception {
        Errors errors = new BindException(printerModel, "printer");
        validator.validate(null, errors);
    }

    @Test(expected = IllegalArgumentException.class)
    public void validate_shouldThrowExceptionIfNotPrinterModel() throws Exception {
        Errors errors = new BindException(printerModel, "printer");
        validator.validate(new Person(), errors);
    }

    @Test
    public void validate_shouldRejectAnEmptyName() throws Exception {
        printerModel.setType(PrinterType.ID_CARD);
        printerModel.setPrintHandler("socketPrintHandler");

        Errors errors = new BindException(printerModel, "printer");
        validator.validate(printerModel, errors);
        assertTrue(errors.hasFieldErrors("name"));
    }

    @Test
    public void validate_shouldRejectAnEmptyType() throws Exception {
        printerModel.setName("Test Model");
        printerModel.setPrintHandler("socketPrintHandler");

        Errors errors = new BindException(printerModel, "printer");
        validator.validate(printerModel, errors);
        assertTrue(errors.hasFieldErrors("type"));
    }

    @Test
    public void validate_shouldRejectEmptyHandler() throws Exception {
        printerModel.setName("Test Model");
        printerModel.setType(PrinterType.ID_CARD);

        Errors errors = new BindException(printerModel, "printer");
        validator.validate(printerModel, errors);
        assertTrue(errors.hasFieldErrors("printHandler"));
    }

    @Test
    public void validate_shouldRejectInvalidHandler() throws Exception {
        printerModel.setName("Test Model");
        printerModel.setType(PrinterType.ID_CARD);
        printerModel.setPrintHandler("bogus handler");

        Errors errors = new BindException(printerModel, "printer");
        validator.validate(printerModel, errors);
        assertTrue(errors.hasFieldErrors("printHandler"));
    }

    @Test
    public void validate_shouldRejectNameGreaterThan255Characters() throws Exception {
        printerModel.setName("Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer " +
                "Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer " +
                "Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer Test Printer ");
        printerModel.setType(PrinterType.ID_CARD);
        printerModel.setPrintHandler("socketPrintHandler");

        Errors errors = new BindException(printerModel, "printer");
        validator.validate(printerModel, errors);
        assertTrue(errors.hasFieldErrors("name"));
    }

    @Test
    public void validate_validPrinterModelShouldPass() throws Exception {
        printerModel.setName("Test Model");
        printerModel.setType(PrinterType.ID_CARD);
        printerModel.setPrintHandler("socketPrintHandler");

        Errors errors = new BindException(printerModel, "printer");
        validator.validate(printerModel, errors);
        assertTrue(!errors.hasErrors());
    }
}
