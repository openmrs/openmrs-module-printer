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

package org.openmrs.module.printer;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.APIException;
import org.openmrs.api.LocationService;
import org.openmrs.module.printer.handler.PrintHandler;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Collection;
import java.util.List;

public class PrinterServiceComponentTest extends BaseModuleContextSensitiveTest {


    @Autowired
    private PrinterService printerService;

    @Autowired
    private LocationService locationService;

    @Before
    public void beforeAllTests() throws Exception {
        executeDataSet("printerServiceComponentTestDataset.xml");
    }

    @Test
    public void testSavePrinter() {

        Printer printer = new Printer();
        printer.setName("Another Test Printer");
        printer.setIpAddress("192.1.1.8");
        printer.setType(PrinterType.ID_CARD);

        printerService.savePrinter(printer);

        List<Printer> printers = printerService.getAllPrinters();

        // there is already a test printer in the dataset, so there should be two printers now
        Assert.assertEquals(2, printers.size());

        // make sure the audit fields have been set
        Assert.assertNotNull(printer.getDateCreated());
        Assert.assertNotNull(printer.getCreator());
        Assert.assertNotNull(printer.getUuid());
    }

    @Test
    public void testDeletePrinter() {

        List<Printer> printers = printerService.getAllPrinters();

        // sanity check
        Assert.assertEquals(1, printers.size());

        printerService.deletePrinter(printers.get(0));

        printers = printerService.getAllPrinters();
        Assert.assertEquals(0, printers.size());
    }

    @Test
    public void testDeletePrinterShouldUnassignItAsDefaultForALocation() {

        LocationAttributeType defaultLabelPrinter = locationService.getLocationAttributeType(1001);

        // sanity check
        Location loc1 = locationService.getLocation(1);
        Location loc2 = locationService.getLocation(2);
        Assert.assertEquals(1, loc1.getActiveAttributes(defaultLabelPrinter).size());
        Assert.assertEquals(1, loc2.getActiveAttributes(defaultLabelPrinter).size());

        List<Printer> printers = printerService.getAllPrinters();
        printerService.deletePrinter(printers.get(0));

        Assert.assertEquals(0, loc1.getActiveAttributes(defaultLabelPrinter).size());
        Assert.assertEquals(0, loc2.getActiveAttributes(defaultLabelPrinter).size());

        // make sure we can save the locations even those they have voided attributes that reference deleted printers
        locationService.saveLocation(loc1);
        locationService.saveLocation(loc2);
    }

    @Test
    public void testShouldReturnTrueIfAnotherPrinterAlreadyHasIpAddressAssigned() {

        Printer differentPrinter = new Printer();
        differentPrinter.setName("Another printer");
        differentPrinter.setIpAddress("192.1.1.2");   // printer in test dataset has this ip
        differentPrinter.setType(PrinterType.LABEL);

        Assert.assertTrue(printerService.isIpAddressAllocatedToAnotherPrinter(differentPrinter));

    }

    @Test
    public void testAllowDuplicateIpAddressesIfAddressIsLocalHost() {

        Printer localPrinter = new Printer();
        localPrinter.setName("Local printer");
        localPrinter.setIpAddress("127.0.0.1");
        localPrinter.setType(PrinterType.LABEL);
        printerService.savePrinter(localPrinter);

        Printer anotherPrinter = new Printer();
        anotherPrinter.setName("Another printer");
        anotherPrinter.setIpAddress("127.0.0.1");
        anotherPrinter.setType(PrinterType.ID_CARD);

        Assert.assertFalse(printerService.isIpAddressAllocatedToAnotherPrinter(anotherPrinter));

    }

    @Test
    public void testShouldReturnFalseIfAnotherPrinterDoesNotHaveIpAddressAssigned() {

        Printer differentPrinter = new Printer();
        differentPrinter.setName("Another printer");
        differentPrinter.setIpAddress("192.1.1.8");
        differentPrinter.setType(PrinterType.LABEL);

        Assert.assertFalse(printerService.isIpAddressAllocatedToAnotherPrinter(differentPrinter));
    }

    @Test
    public void testGetPrinterByName() {

        Printer printer = printerService.getPrinterByName("Test Printer");
        Assert.assertEquals(new Integer(1), printer.getId());
        Assert.assertEquals(new Integer(2), printer.getPhysicalLocation().getId());
        Assert.assertEquals("192.1.1.2", printer.getIpAddress());
        Assert.assertEquals("2", printer.getPort());
        Assert.assertEquals("LABEL", printer.getType().name());

    }

    @Test
    public void testShouldReturnTrueIfAnotherPrinterAlreadyHasSameName() {

        Printer differentPrinter = new Printer();
        differentPrinter.setName("Test Printer");
        differentPrinter.setIpAddress("192.1.1.9");
        differentPrinter.setType(PrinterType.LABEL);

        Assert.assertTrue(printerService.isNameAllocatedToAnotherPrinter(differentPrinter));
    }

    @Test
    public void testShouldReturnFalseIfAnotherPrinterDoesNotHaveSameName() {

        Printer differentPrinter = new Printer();
        differentPrinter.setName("Test Printer With Different Name");
        differentPrinter.setIpAddress("192.1.1.9");
        differentPrinter.setType(PrinterType.LABEL);

        Assert.assertFalse(printerService.isNameAllocatedToAnotherPrinter(differentPrinter));
    }

    @Test
    public void testShouldSetDefaultLabelPrinterForLocation() {

        Location location = locationService.getLocation(1);
        Printer printer = printerService.getPrinterById(1);

        printerService.setDefaultPrinter(location, PrinterType.LABEL, printer);

        Printer fetchedPrinter = printerService.getDefaultPrinter(location, PrinterType.LABEL);
        Assert.assertEquals(printer, fetchedPrinter);
    }

    @Test
    public void testShouldGetDefaultLabelPrinterForLocation() {

        Location location = locationService.getLocation(2);
        Printer printer = printerService.getPrinterById(1);  // this has been set as the default printer for location 2 in dataset

        Printer fetchedPrinter = printerService.getDefaultPrinter(location, PrinterType.LABEL);
        Assert.assertEquals(printer, fetchedPrinter);

    }

    @Test
    public void testShouldUpdateDefaultLabelPrinterForLocation() {

        Location location = locationService.getLocation(2); // a default printer for location 2 in has been set in the dataset

        // create a new printer and set it as the default for this location
        Printer printer = new Printer();
        printer.setName("Another Test Printer");
        printer.setIpAddress("192.1.1.8");
        printer.setType(PrinterType.LABEL);

        printerService.savePrinter(printer);
        printerService.setDefaultPrinter(location, PrinterType.LABEL, printer);

        Printer fetchedPrinter = printerService.getDefaultPrinter(location, PrinterType.LABEL);
        Assert.assertEquals(printer, fetchedPrinter);
    }


    @Test
    public void testShouldRemoveDefaultLabelPrinterForLocation() {
        Location location = locationService.getLocation(2); // a default printer for location 2 in has been set in the dataset
        printerService.setDefaultPrinter(location, PrinterType.LABEL, null);

        Printer fetchedPrinter = printerService.getDefaultPrinter(location, PrinterType.LABEL);
        Assert.assertNull(fetchedPrinter);
    }

    @Test
    public void testShouldGetAllLocationsWithDefaultPrinter() {

        List<Location> locations = printerService.getLocationsWithDefaultPrinter(PrinterType.LABEL);

        // in the test dataset, only location 2 has a default label printer
        Assert.assertEquals(2, locations.size());

        // poor man's test to make sure that list contains all proper values
        Assert.assertTrue((locations.get(0).getId() == 1 && locations.get(1).getId() == 2)
                            || (locations.get(1).getId() == 1 && locations.get(0).getId() == 2) );

    }

    @Test(expected = APIException.class)
    public void testShouldNotAllowMismatchedLocationAttributeTypeAndPrinterType() {

        Location location = locationService.getLocation(2);

        Printer printer = new Printer();
        printer.setName("Test Label Printer");
        printer.setIpAddress("192.1.1.9");
        printer.setType(PrinterType.ID_CARD);

        LocationAttributeType defaultIdCardPrinter = locationService.getLocationAttributeTypeByUuid(PrinterConstants.LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER.get("LABEL"));

        LocationAttribute attribute = new LocationAttribute();
        attribute.setAttributeType(defaultIdCardPrinter);
        attribute.setValue(printer);

        location.addAttribute(attribute);

        locationService.saveLocation(location);
    }

    @Test
    public void testShouldGetPrinterByType() {

        List<Printer> printers = printerService.getPrintersByType(PrinterType.LABEL);
        Assert.assertEquals(1, printers.size());
        Assert.assertEquals("Test Printer", printers.get(0).getName());

    }

    @Test
    public void testSavePrinterModel() {

        PrinterModel printerModel = new PrinterModel();
        printerModel.setName("Another Test Printer Model");
        printerModel.setPrintHandler(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME);
        printerModel.setType(PrinterType.ID_CARD);

        printerService.savePrinterModel(printerModel);

        List<PrinterModel> printerModels = printerService.getAllPrinterModels();

        // there is already a test printer model in the dataset, so there should be two printers now
        Assert.assertEquals(2, printerModels.size());

        // make sure the audit fields have been set
        Assert.assertNotNull(printerModel.getDateCreated());
        Assert.assertNotNull(printerModel.getCreator());
        Assert.assertNotNull(printerModel.getUuid());
    }

    @Test
    public void testDeletePrinterModel() {

        List<PrinterModel> printerModels = printerService.getAllPrinterModels();

        // sanity check
        Assert.assertEquals(1, printerModels.size());

        printerService.deletePrinterModel(printerModels.get(0));

        printerModels = printerService.getAllPrinterModels();
        Assert.assertEquals(0, printerModels.size());

    }

    @Test
    public void testShouldReturnTrueIfAnotherPrinterModelAlreadyHasSameName() {

        PrinterModel differentPrinterModel = new PrinterModel();
        differentPrinterModel.setName("Test Printer Model");
        differentPrinterModel.setType(PrinterType.LABEL);
        differentPrinterModel.setPrintHandler(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME);

        Assert.assertTrue(printerService.isNameAllocatedToAnotherPrinterModel(differentPrinterModel));
    }


    @Test
    public void testShouldReturnFalseIfAnotherPrinterModelDoesntHaveSameName() {

        PrinterModel differentPrinterModel = new PrinterModel();
        differentPrinterModel.setName("Different Printer Model Name");
        differentPrinterModel.setType(PrinterType.LABEL);
        differentPrinterModel.setPrintHandler(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME);

        Assert.assertFalse(printerService.isNameAllocatedToAnotherPrinterModel(differentPrinterModel));
    }

    @Test
    public void testShouldGetPrinterModelByType() {

        List<PrinterModel> printerModels = printerService.getPrinterModelsByType(PrinterType.ID_CARD);
        Assert.assertEquals(0, printerModels.size());

        printerModels = printerService.getPrinterModelsByType(PrinterType.LABEL);
        Assert.assertEquals(1, printerModels.size());
        Assert.assertEquals("Test Printer Model", printerModels.get(0).getName());
    }

    @Test
    public void byDefaultOnlySocketPrintHandlerIsAvailable() {
        Collection<PrintHandler> printHandlers = printerService.getRegisteredPrintHandlers();
        Assert.assertEquals(1, printHandlers.size());
        Assert.assertEquals(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME, printHandlers.iterator().next().getBeanName());
    }

    @Test
    public void unregisterPrintHandlerShouldNotFailIfHandlerNotFound() {
        printerService.unregisterPrintHandler("blah");
        Collection<PrintHandler> printHandlers = printerService.getRegisteredPrintHandlers();
        Assert.assertEquals(1, printHandlers.size());
    }

    @Test
    public void shouldFetchPrintHandlerByBeanName() {
        PrintHandler printHandler = printerService.getRegisteredPrintHandlerByName(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME);
        Assert.assertEquals(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME, printHandler.getBeanName());
    }

    @Test
    public void shouldReturnNullIfNoHandlerMatchingName() {
        PrintHandler printHandler = printerService.getRegisteredPrintHandlerByName("bogus name");
        Assert.assertNull(printHandler);
    }
    
}
