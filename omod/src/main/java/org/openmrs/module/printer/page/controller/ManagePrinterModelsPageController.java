package org.openmrs.module.printer.page.controller;

import org.openmrs.module.printer.PrinterService;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

public class ManagePrinterModelsPageController {

    public void get(PageModel model, @SpringBean("printerService") PrinterService printerService) {
        model.addAttribute("printerModels", printerService.getAllPrinterModels());
        model.addAttribute("printHandlers", printerService.getRegisteredPrintHandlers());
    }
}
