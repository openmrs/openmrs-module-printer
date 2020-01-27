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

import org.hamcrest.CoreMatchers;
import org.hamcrest.core.Is;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;
import org.openmrs.Location;
import org.openmrs.module.printer.Printer;
import org.openmrs.module.printer.PrinterService;
import org.openmrs.module.printer.PrinterType;
import org.openmrs.ui.framework.UiUtils;
import org.openmrs.ui.framework.fragment.action.FragmentActionResult;
import org.openmrs.ui.framework.fragment.action.SuccessResult;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 *
 */
public class DefaultPrintersFragmentControllerTest {

    @Test
    public void testSaveDefaultPrinter() throws Exception {
        DefaultPrintersFragmentController controller = new DefaultPrintersFragmentController();

        PrinterType type = PrinterType.ID_CARD;
        Printer printer = new Printer();
        Location location = new Location();
        location.setName("Location");

        UiUtils ui = mock(UiUtils.class);
        when(ui.format(location)).thenReturn(location.getName());
        when(ui.message("printer." + type)).thenReturn("printer." + type);
        when(ui.message("printer.defaultUpdate", "printer." + type, location.getName())).thenReturn("printer.defaultUpdate:printer." + type + "," + location.getName());

        PrinterService printerService = mock(PrinterService.class);

        FragmentActionResult result = controller.saveDefaultPrinter(location, type, printer, printerService, ui);

        Mockito.verify(printerService).setDefaultPrinter(location, type, printer);
        Assert.assertThat(result, CoreMatchers.instanceOf(SuccessResult.class));
        SuccessResult success = (SuccessResult) result;
        Assert.assertThat(success.getMessage(), Is.is("printer.defaultUpdate:printer." + type + "," + location.getName()));
    }

}
