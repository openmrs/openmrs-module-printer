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

import org.openmrs.Location;
import org.openmrs.api.LocationService;
import org.openmrs.module.emrapi.EmrApiProperties;
import org.openmrs.module.printer.Printer;
import org.openmrs.module.printer.PrinterService;
import org.openmrs.module.printer.PrinterType;
import org.openmrs.ui.framework.annotation.SpringBean;
import org.openmrs.ui.framework.page.PageModel;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DefaultPrintersPageController {

    public void get(PageModel model, @SpringBean("printerService") PrinterService printerService,
                    @SpringBean("locationService") LocationService locationService,
                    @SpringBean EmrApiProperties emrApiProperties) {

        // TODO: maybe an actual "printer locations" tag?
        List<Location> locations = locationService.getLocationsByTag(emrApiProperties.getSupportsLoginLocationTag());

        Map<Location,Map<String,Printer>> locationsToPrintersMap = new HashMap<Location, Map<String,Printer>>();

        // TODO: change this so that we don't have to manually add each new printer type, should be dynamic based on type enum!
        // TODO: also fix this in the defaultPrinters.gsp as well

        for (Location location : locations) {
            Map<String,Printer> printersForLocation = new HashMap<String, Printer>();
            printersForLocation.put("idCardPrinter", printerService.getDefaultPrinter(location, PrinterType.ID_CARD));
            printersForLocation.put("labelPrinter", printerService.getDefaultPrinter(location, PrinterType.LABEL));
            printersForLocation.put("wristbandPrinter", printerService.getDefaultPrinter(location, PrinterType.WRISTBAND));
            locationsToPrintersMap.put(location, printersForLocation);
        }

        model.put("locationsToPrintersMap", locationsToPrintersMap);
        model.addAttribute("idCardPrinters", printerService.getPrintersByType(PrinterType.ID_CARD));
        model.addAttribute("labelPrinters", printerService.getPrintersByType(PrinterType.LABEL));
        model.addAttribute("wristbandPrinters", printerService.getPrintersByType(PrinterType.WRISTBAND));
    }
}
