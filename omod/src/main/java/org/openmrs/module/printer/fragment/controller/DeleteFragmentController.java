package org.openmrs.module.printer.fragment.controller;

import org.openmrs.module.printer.Printer;
import org.openmrs.module.printer.PrinterModel;
import org.openmrs.module.printer.PrinterService;
import org.openmrs.module.uicommons.util.InfoErrorMessageUtil;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.action.FragmentActionResult;
import org.openmrs.ui.framework.fragment.action.SuccessResult;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class DeleteFragmentController {

    public FragmentActionResult deletePrinter(@RequestParam("printer") Printer printer,
                                              @SpringBean("printerService") PrinterService printerService,
                                              UiUtils ui,
                                              HttpServletRequest request) {

        try {
            printerService.deletePrinter(printer);
        }
        catch (Exception e) {
            InfoErrorMessageUtil.flashInfoMessage(request.getSession(), ui.message("printer.error.delete"));
            return new SuccessResult();  // kind of hacky
        }

        InfoErrorMessageUtil.flashInfoMessage(request.getSession(), ui.message("printer.deleted"));

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
            InfoErrorMessageUtil.flashInfoMessage(request.getSession(), ui.message("printer.model.error.delete"));
            return new SuccessResult();  // kind of hacky
        }

        InfoErrorMessageUtil.flashInfoMessage(request.getSession(), ui.message("printer.model.deleted"));

        return new SuccessResult();
    }
}

