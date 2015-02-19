package org.openmrs.module.printer.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.module.printer.PrinterModel;
import org.openmrs.module.printer.PrinterService;
import org.openmrs.module.printer.PrinterType;
import org.openmrs.module.printer.validator.PrinterModelValidator;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class PrinterModelPageController {

    protected final Log log = LogFactory.getLog(getClass());

    public PrinterModel getPrinterModel(@RequestParam(value = "printerModelId", required = false) PrinterModel printerModel) {
        if (printerModel == null) {
            printerModel = new PrinterModel();
        }

        return printerModel;
    }

    public void get(PageModel model, @MethodParam("getPrinterModel") PrinterModel printerModel,
                    @SpringBean("printerService") PrinterService printerService) {
        addReferenceData(model, printerService);
        model.addAttribute("printerModel", printerModel);
    }

    public String post(PageModel model, @BindParams PrinterModel printerModel, BindingResult errors,
                       @SpringBean("printerService") PrinterService printerService,
                       @SpringBean("printerModelValidator")PrinterModelValidator validator,
                       HttpServletRequest request) {

        validator.validate(printerModel, errors);

        if (!errors.hasErrors()) {
            try {
                printerService.savePrinterModel(printerModel);
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_TOAST_MESSAGE, "true");
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "printer.model.saved");

                return "redirect:/printer/managePrinterModels.page";
            }
            catch (Exception e) {
                log.warn("Some error occured while saving printer model details:", e);
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                        "printer.model.error.save.fail");
            }
        }
        else {
            request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                    "emr.error.foundValidationErrors");
        }

        // redisplay the form with errors
        addReferenceData(model, printerService);
        model.addAttribute("errors", errors);
        model.addAttribute("printerModel", printerModel);
        return null;
    }

    private void addReferenceData(PageModel model, PrinterService printerService) {
        model.addAttribute("printerTypeOptions", PrinterType.values());
        model.addAttribute("printHandlers", printerService.getRegisteredPrintHandlers());
    }
}
