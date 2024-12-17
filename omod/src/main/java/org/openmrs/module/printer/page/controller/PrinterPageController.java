/*
 * The contents of this file are subject to the OpenMRS Public License
 * Version 1.0 (the "License"); you may not use this file except in
 * compliance with the License. You may obtain a copy of the License at
 * http://license.openmrs.org
 *
 * Software distributed under the License is distributed on an "AS IS"
 * basis, WITHOUT WARRANTY OF ANY KIND, either express or implied. See the
 * License for the specific language governing rights and limitations
 * under the License.
 *
 * Copyright (C) OpenMRS, LLC.  All Rights Reserved.
 */

package org.openmrs.module.printer.page.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.api.LocationService;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.printer.Printer;
import org.openmrs.module.printer.PrinterService;
import org.openmrs.module.printer.PrinterType;
import org.openmrs.module.printer.validator.PrinterValidator;
import org.openmrs.module.uicommons.UiCommonsConstants;
import org.openmrs.ui.framework.annotation.BindParams;
import org.openmrs.ui.framework.annotation.MethodParam;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

public class PrinterPageController {

    protected final Log log = LogFactory.getLog(getClass());

    public Printer getPrinter(@RequestParam(value = "printerId", required = false) Printer printer) {
        if (printer == null) {
            printer = new Printer();
        }

        return printer;
    }

    public void get(PageModel model, @MethodParam("getPrinter") Printer printer,
                    @SpringBean("printerService")  PrinterService printerService,
                    @SpringBean("locationService") LocationService locationService,
                    @SpringBean EmrApiProperties emrApiProperties) {
        addReferenceData(model, locationService, printerService, emrApiProperties);
        model.addAttribute("printer", printer);
    }

    public String post(PageModel model, @BindParams Printer printer, BindingResult errors,
                       @SpringBean("printerService") PrinterService printerService,
                       @SpringBean("printerValidator") PrinterValidator printerValidator,
                       @SpringBean("locationService")LocationService locationService,
                       @SpringBean EmrApiProperties emrApiProperties,
                       HttpServletRequest request) {

        printerValidator.validate(printer, errors);

        if (!errors.hasErrors()) {
            try {
                printerService.savePrinter(printer);
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_TOAST_MESSAGE, "true");
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_INFO_MESSAGE, "printer.saved");

                return "redirect:/printer/managePrinters.page";
            }
            catch (Exception e) {
                log.warn("Some error occured while saving printer details:", e);
                request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                        "printer.error.save.fail");
            }
        }
        else {
            request.getSession().setAttribute(UiCommonsConstants.SESSION_ATTRIBUTE_ERROR_MESSAGE,
                    "emr.error.foundValidationErrors");
        }

        // redisplay the form with errors
        addReferenceData(model, locationService, printerService, emrApiProperties);
        model.addAttribute("errors", errors);
        model.addAttribute("printer", printer);
        return null;
    }

    private void addReferenceData(PageModel model, LocationService locationService, PrinterService printerService, EmrApiProperties emrApiProperties) {
        // TODO: maybe an actual "printer locations" tag?
        model.addAttribute("locations", locationService.getLocationsByTag(emrApiProperties.getSupportsLoginLocationTag()));
        model.addAttribute("printerTypes", PrinterType.values());
        model.addAttribute("printerModels", printerService.getAllPrinterModels());
    }

}
