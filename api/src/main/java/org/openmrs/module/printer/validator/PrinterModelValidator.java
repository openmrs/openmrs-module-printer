package org.openmrs.module.printer.validator;

import org.apache.commons.lang.StringUtils;
import org.openmrs.annotation.Handler;
import org.openmrs.messagesource.MessageSourceService;
import org.openmrs.module.printer.PrinterModel;
import org.openmrs.module.printer.PrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;

@Handler(supports = {PrinterModel.class}, order = 50)
public class PrinterModelValidator implements Validator {

    @Autowired
    @Qualifier("messageSourceService")
    private MessageSourceService messageSourceService;

    @Autowired
    @Qualifier("printerService")
    private PrinterService printerService;

    /**
     * @param messageSourceService the messageSourceService to set
     */
    public void setMessageSourceService(MessageSourceService messageSourceService) {
        this.messageSourceService = messageSourceService;
    }

    /**
     * @param printerService the printer service to set
     */
    public void setPrinterService(PrinterService printerService) {
        this.printerService = printerService;
    }


    @Override
    public boolean supports(Class<?> clazz) {
        return PrinterValidator.class.isAssignableFrom(clazz);
    }

    @Override
    public void validate(Object obj, Errors errors) {

        if (obj == null || !(obj instanceof PrinterModel)) {
            throw new IllegalArgumentException("The parameter obj should not be null and must be of type" + PrinterModel.class);
        }

        PrinterModel printerModel = (PrinterModel) obj;

        if (StringUtils.isBlank(printerModel.getName())) {
            errors.rejectValue("name", "error.required",
                    new Object[]{messageSourceService.getMessage("printer.model.name")}, null);
        }

        if (printerModel.getType() == null) {
            errors.rejectValue("type", "error.required",
                    new Object[]{messageSourceService.getMessage("printer.type")}, null);
        }

        if (StringUtils.isBlank(printerModel.getPrintHandler())) {
            errors.rejectValue("printHandler", "error.required",
                    new Object[]{messageSourceService.getMessage("printer.model.handler")}, null);
        }

        if (printerModel.getName() != null && printerModel.getName().length() > 256) {
            errors.rejectValue("name", "printer.error.nameTooLong", null, null);
        }

        if (printerService.isNameAllocatedToAnotherPrinterModel(printerModel)) {
            errors.rejectValue("name", "printer.model.error.nameDuplicate", null, null);
        }

        if (StringUtils.isNotBlank(printerModel.getPrintHandler()) &&
            printerService.getRegisteredPrintHandlerByName(printerModel.getPrintHandler()) == null) {
            errors.rejectValue("printHandler", "printer.model.error.invalidPrintHandler", null, null);
        }

    }
}



