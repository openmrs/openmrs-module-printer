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

package org.openmrs.module.printer.fragment.controller;

import org.openmrs.Location;
import org.openmrs.module.printer.Printer;
import org.openmrs.module.printer.PrinterService;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.fragment.action.FailureResult;
import org.openmrs.ui.framework.fragment.action.FragmentActionResult;
import org.openmrs.ui.framework.fragment.action.SuccessResult;
import org.springframework.web.bind.annotation.RequestParam;

public class DefaultPrintersFragmentController {


    public FragmentActionResult saveDefaultPrinter(@RequestParam (value = "location", required =  true) Location location,
                                                   @RequestParam (value = "type", required = true) Printer.Type type,
                                                   @RequestParam (value = "printer", required = false) Printer printer,
                                                   @SpringBean("printerService") PrinterService printerService,
                                                   UiUtils ui) {

        try {
            printerService.setDefaultPrinter(location, type, printer);
        }
        catch (Exception e) {
            return new FailureResult(e.getMessage());
        }

        String successMessage = ui.message("printer.defaultUpdate", ui.message("emr.printer." + type), ui.format(location));
        return new SuccessResult(successMessage);
    }


}
