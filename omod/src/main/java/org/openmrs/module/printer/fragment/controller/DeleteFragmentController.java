package org.openmrs.module.printer.fragment.controller;

import org.openmrs.module.printer.Printer;
import org.openmrs.module.printer.PrinterModel;
import org.openmrs.module.printer.PrinterService;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.action.FragmentActionResult;
import org.openmrs.ui.framework.fragment.action.SuccessResult;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class DeleteFragmentController {

    public FragmentActionResult deletePrinter(@RequestParam("printer") Printer printer,
                                              @SpringBean("printerService") PrinterService printerService,
                                              UiUtils ui,
                                              HttpServletRequest request) {

        try {
            printerService.deletePrinter(printer);
        }
        catch (Exception e) {
            flashInfoMessage(request.getSession(), ui.message("printer.error.delete"));
            return new SuccessResult();  // kind of hacky
        }

        flashInfoMessage(request.getSession(), ui.message("printer.deleted"));

        return new SuccessResult();
    }

    public FragmentActionResult deletePrinterModel(@RequestParam("printerModel") PrinterModel printerModel,
                                              @SpringBean("printerService") PrinterService printerService,
                                              UiUtils ui,
                                              HttpServletRequest request) {

        try {
            printerService.deletePrinterModel(printerModel);
        }
        catch (Exception e) {
            flashInfoMessage(request.getSession(), ui.message("printer.model.error.delete"));
            return new SuccessResult();  // kind of hacky
        }

        flashInfoMessage(request.getSession(), ui.message("printer.model.deleted"));

        return new SuccessResult();
    }


    private void flashInfoMessage(HttpSession session, String message) {
        session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, message);
        session.setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_TOAST_MESSAGE, "true");
    }

}

