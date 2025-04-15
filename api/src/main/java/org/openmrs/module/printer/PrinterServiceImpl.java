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

import org.apache.commons.lang.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.Location;
import org.openmrs.LocationAttribute;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.APIException;
import org.openmrs.api.LocationService;
import org.openmrs.api.impl.BaseOpenmrsService;
import org.openmrs.module.printer.db.PrinterDAO;
import org.openmrs.module.printer.db.PrinterModelDAO;
import org.openmrs.module.printer.handler.PrintHandler;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PrinterServiceImpl extends BaseOpenmrsService implements PrinterService {

    private final Log log = LogFactory.getLog(getClass());

    private PrinterDAO printerDAO;

    private PrinterModelDAO printerModelDAO;

    private LocationService locationService;

    private Map<String,PrintHandler> printHandlers = new HashMap<String, PrintHandler>();

    /**
     * A map from the id of an identifier source, to an object we can lock on for that printer
     * (Idea stolen from IDGen module... )
     */
    private ConcurrentHashMap<Integer, Lock> printerLocks = new ConcurrentHashMap<Integer, Lock>();

    public void setPrinterDAO(PrinterDAO printerDAO) {
        this.printerDAO = printerDAO;
    }

    public void setPrinterModelDAO(PrinterModelDAO printerModelDAO) {
        this.printerModelDAO = printerModelDAO;
    }

    public void setLocationService(LocationService locationService) {
        this.locationService = locationService;
    }

    public void setPrintHandlers(Map<String, PrintHandler> printHandlers) {
        this.printHandlers = printHandlers;
    }

    @Override
    @Transactional(readOnly = true)
    public Printer getPrinterById(Integer id) {
        return printerDAO.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public Printer getPrinterByName(String name) {
        return printerDAO.getPrinterByName(name);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Printer> getPrintersByType(PrinterType type) {
        return printerDAO.getPrintersByType(type);
    }

    @Override
    @Transactional
    public void savePrinter(Printer printer) {
        // just in case we may be changing the type of the printer, make sure it is not set as the default for any other type
        if (printer.getPrinterId() != null) {
            removePrinterAsDefaultForAllTypesExceptType(printer, printer.getType());
        }
        printerDAO.saveOrUpdate(printer);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Printer> getAllPrinters() {
        return printerDAO.getAll();
    }

    @Override
    @Transactional
    public void deletePrinter(Printer printer) {
        // make sure this printer isn't assigned as the default printer for any locations
        removePrinterAsDefaultForAllTypes(printer);
        printerDAO.delete(printer);
    }

    @Override
    @Transactional(readOnly = true)
    public PrinterModel getPrinterModelById(Integer id) {
        return printerModelDAO.getById(id);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrinterModel> getAllPrinterModels() {
        return printerModelDAO.getAll();
    }

    @Override
    @Transactional
    public void savePrinterModel(PrinterModel printerModel) {
        printerModelDAO.saveOrUpdate(printerModel);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PrinterModel> getPrinterModelsByType(PrinterType type) {
        return printerModelDAO.getPrinterModelsByType(type);
    }

    @Override
    @Transactional
    public void deletePrinterModel(PrinterModel printerModel) {
        printerModelDAO.delete(printerModel);
    }

    @Override
    @Transactional
    public void setDefaultPrinter(Location location, PrinterType type, Printer printer) {

        LocationAttributeType locationAttributeType = getLocationAttributeTypeDefaultPrinter(type);

        // if no printer is specified, void any existing default printer
        if (printer == null) {
            for (LocationAttribute attr : location.getActiveAttributes(locationAttributeType)) {
                attr.setVoided(true);
            }
        } else {
            LocationAttribute defaultPrinter = new LocationAttribute();
            defaultPrinter.setAttributeType(locationAttributeType);
            defaultPrinter.setValue(printer);
            location.setAttribute(defaultPrinter);
        }

        locationService.saveLocation(location);
    }

    @Override
    @Transactional(readOnly = true)
    public Printer getDefaultPrinter(Location location, PrinterType type) {

        List<LocationAttribute> defaultPrinters = location.getActiveAttributes(getLocationAttributeTypeDefaultPrinter(type));

        if (defaultPrinters == null || defaultPrinters.size() == 0) {
            return null;
        }

        if (defaultPrinters.size() > 1) {
            throw new IllegalStateException("Multiple default printer of type " + type + " defined for " + location);
        }

        return (Printer) defaultPrinters.get(0).getValue();
    }

    @Override
    @Transactional(readOnly = true)
    public List<Location> getLocationsWithDefaultPrinter(PrinterType type) {

        List<Location> locationsWithDefaultPrinter = new ArrayList<Location>();

        for (Location location : locationService.getAllLocations(false)) {
            if (getDefaultPrinter(location, type) !=null) {
                locationsWithDefaultPrinter.add(location);
            }
        }

        return locationsWithDefaultPrinter;
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isIpAddressAllocatedToAnotherPrinter(Printer printer) {

        // for testing purposes, we allow two printers to both be assigned the localhost ip
        if (printer.getIpAddress().equals("127.0.0.1")) {
            return false;
        }

        return printerDAO.isIpAddressAllocatedToAnotherPrinter(printer);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNameAllocatedToAnotherPrinter(Printer printer) {
        return printerDAO.isNameAllocatedToAnotherPrinter(printer);
    }

    @Override
    @Transactional(readOnly = true)
    public boolean isNameAllocatedToAnotherPrinterModel(PrinterModel printerModel) {
        return printerModelDAO.isNameAllocatedToAnotherPrinterModel(printerModel);
    }

    @Override
    public void printViaSocket(String data, PrinterType type, Location location, String encoding) throws UnableToPrintViaSocketException {
        printViaSocket(data, type, location, encoding, false, null);
    }

    @Override
    public void printViaSocket(String data, PrinterType type, Location location, String encoding, Boolean printInSeparateThread, Integer wait)
            throws UnableToPrintViaSocketException {

        Printer printer = getDefaultPrinter(location, type);

        if (printer == null) {
            throw new IllegalStateException("No default printer assigned for " + location.getDisplayString() + ". Please contact your system administrator");
        }

        printViaSocket(data, printer, encoding, printInSeparateThread, wait);
    }

    @Override
    public void printViaSocket(String data, Printer printer, String encoding)
            throws UnableToPrintViaSocketException {
        printViaSocket(data, printer, encoding, false, null);
    }

    @Override
    public void printViaSocket(String data, Printer printer, String encoding, Boolean printInSeparateThread, Integer wait)
            throws UnableToPrintViaSocketException {

        Map<String,Object> paramMap = new HashMap<String,Object>();
        paramMap.put("encoding", encoding);
        paramMap.put("wait", wait);
        paramMap.put("data", data);

        try {
            print(paramMap, printer, printInSeparateThread, printHandlers.get(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME) );
        }
        catch (UnableToPrintException e) {
            throw new UnableToPrintViaSocketException("Unable to print via socket to printer " + printer,e);
        }
    }

    @Override
    public void print(Map<String, Object> paramMap, Printer printer, Boolean printInSeparateThread)
        throws UnableToPrintException {

        PrintHandler handler;
        String handlerName = printer.getModel() != null ? printer.getModel().getPrintHandler() : null;

        if (StringUtils.isNotBlank(handlerName)) {
            handler = printHandlers.get(handlerName);
            if (handler == null) {
                throw new APIException("Unable to find print handler " + handlerName);
            }
        }
        else {
            // default handler is the socket handler
            handler = printHandlers.get(PrinterConstants.SOCKET_PRINT_HANDLER_BEAN_NAME);
        }

        print(paramMap, printer, printInSeparateThread, handler);
    }

    @Override
    public void print(Map<String, Object> paramMap, Printer printer, Boolean printInSeparateThread, PrintHandler printHandler) throws UnableToPrintException {

        PrintThread printThread = new PrintThread(printer, paramMap, getPrinterLock(printer.getPrinterId()), printHandler);

        if (printInSeparateThread) {
            log.error("Printing in separate thread no longer supported.");
        }

        printThread.print();

       /* if (printInSeparateThread) {
            try {
                ExecutorService executorService = Executors.newSingleThreadExecutor();
                executorService.invokeAll(Arrays.asList(Executors.callable(printThread)));
            }
            catch (InterruptedException e) {
                log.error("Print job interrupted for printer " + printer.getName());
            }

        }
        else {
            printThread.print();
        }*/
    }

    @Override
    public Collection<PrintHandler> getRegisteredPrintHandlers() {
        return printHandlers.values();
    }

    @Override
    public void registerPrintHandler(PrintHandler printHandler) {
        printHandlers.put(printHandler.getBeanName(), printHandler);
    }

    @Override
    public PrintHandler getRegisteredPrintHandlerByName(String beanName) {
        if (printHandlers.containsKey(beanName)) {
            return printHandlers.get(beanName);
        }
        else {
            return null;
        }
    }

    @Override
    public void unregisterPrintHandler(String beanName) {
        if(printHandlers.containsKey(beanName)) {
            printHandlers.remove(beanName);
        }
    }

    private Lock getPrinterLock(Integer printerId) {
        // this method does not need to be synchronized, because putIfAbsent is atomic
        printerLocks.putIfAbsent(printerId, new ReentrantLock());
        return printerLocks.get(printerId);
    }


    private LocationAttributeType getLocationAttributeTypeDefaultPrinter(PrinterType type) {

        String locationAttributeTypeUuid = PrinterConstants.LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER.get(type.name());
        LocationAttributeType locationAttributeType = locationService.getLocationAttributeTypeByUuid(locationAttributeTypeUuid);

        if (locationAttributeType == null) {
            throw new IllegalStateException("Unable to fetch location attribute type for default " + type + " printer");
        }

        return locationAttributeType;
    }

    private void removePrinterAsDefaultForAllTypes(Printer printer) {
        removePrinterAsDefaultForAllTypesExceptType(printer, null);
    }

    private void removePrinterAsDefaultForAllTypesExceptType(Printer printer, PrinterType printerType) {
        for (PrinterType type : PrinterType.values()) {
            if (printerType == null || printerType != type) {
                removePrinterAsDefault(printer, type);
            }
        }
    }

    private void removePrinterAsDefault(Printer printer, PrinterType printerType) {

        LocationAttributeType type = getLocationAttributeTypeDefaultPrinter(printerType);

        Map<LocationAttributeType, Object> attributeValues = new HashMap<LocationAttributeType, Object>();
        attributeValues.put(type, printer);

        for (Location location: locationService.getLocations(null, null,attributeValues, true, null, null)) {
            for (LocationAttribute attr : location.getActiveAttributes(type)) {
                attr.setVoided(true);
            }
            locationService.saveLocation(location);
        }

    }
}
