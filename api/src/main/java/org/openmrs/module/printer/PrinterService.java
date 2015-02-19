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

import org.openmrs.Location;
import org.openmrs.annotation.Authorized;
import org.openmrs.api.OpenmrsService;
import org.openmrs.module.printer.handler.PrintHandler;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * TODO move this out of the emrapi module
 */
public interface PrinterService extends OpenmrsService {


    /**
     * Fetches a printer by id
     *
     * @param id
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    Printer getPrinterById(Integer id);

    /**
     * Fetches a printer by name
     *
     * @param name
     * @return
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    Printer getPrinterByName(String name);

    /**
     * Fetches all printers of the specified type
     *
     * @param type
     * @return
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    List<Printer> getPrintersByType(PrinterType type);

    /**
     * Saves a printer
     *
     * @param printer
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_MANAGE_PRINTERS)
    void savePrinter(Printer printer);

    /**
     * Fetches all printers in the system
     *
     * @return all printers in the systesm
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    List<Printer> getAllPrinters();

    /**
     * Fetches a printer model by id
     *
     * @param id
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    PrinterModel getPrinterModelById(Integer id);

    /**
     * Fetches all printer models of the specified type
     *
     * @param type
     * @return
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    List<PrinterModel> getPrinterModelsByType(PrinterType type);

    /**
     * Saves a printer model
     *
     * @param printerModel to save
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_MANAGE_PRINTERS)
    void savePrinterModel(PrinterModel printerModel);

    /**
     * Fetches all printer model in the system
     *
     * @return all printers models in the system
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    List<PrinterModel> getAllPrinterModels();


    /**
     * Sets the specified printer as the default printer of the specified type
     * at the specified location; if printer = null, remove any default printer
     * of that type at that location
     *
     * @param location
     * @param type
     * @param printer
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_MANAGE_PRINTERS)
    void setDefaultPrinter(Location location, PrinterType type, Printer printer);

    /**
     * Gets the printer of the specified type that is the default printer
     * for that location; returns null if no printer found
     *
     * @param location
     * @param type
     * @return
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    Printer getDefaultPrinter(Location location, PrinterType type);

    /**
     * Returns all locations that have a default printer configured of the specified type
     *
     * @param type
     * @return
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    List<Location> getLocationsWithDefaultPrinter(PrinterType type);

    /**
     * Given a printer, returns true/false if that ip address is in use
     * by *another* printer
     *
     * @return
     * @should always return false for localhost ip (127.0.0.1)
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_MANAGE_PRINTERS)
    boolean isIpAddressAllocatedToAnotherPrinter(Printer printer);

    /**
     * Given a printer, returns true/false if that name is in use
     * by *another* printer
     *
     * @return
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_MANAGE_PRINTERS)
    boolean isNameAllocatedToAnotherPrinter(Printer printer);

    /**
     * Given a printer model, returns true/false if that name is in use
     * by *another* printer model
     *
     * @return
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_MANAGE_PRINTERS)
    boolean isNameAllocatedToAnotherPrinterModel(PrinterModel printerModel);


    /**
     * Prints the string data to the default printer of the specified type
     * at the specific location via socket
     *
     * @param data
     * @param location
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    void printViaSocket(String data, PrinterType type, Location location, String encoding)
            throws UnableToPrintViaSocketException;


    /**
     * Prints the string data to the default printer of the specified type
     * at the specific location via socket
     *
     * @param data
     * @param location
     * @param printInSeparateThread true/false whether to print a separate thread (will not catch errors)
     * @param wait time in ms to wait after printing before allowing another job to be sent to same printer
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    void printViaSocket(String data, PrinterType type, Location location, String encoding, Boolean printInSeparateThread, Integer wait)
            throws UnableToPrintViaSocketException;

    /**
     * Prints the string data to the specified printer (without using a separate thread, and with no wait-time)
     *
     * @param data the data to print
     * @param printer the printer to print to
     * @param encoding the encoding to use
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    void printViaSocket(String data, Printer printer, String encoding)
            throws UnableToPrintViaSocketException;

    /**
     * Prints the string data to the specified printer
     *
     * @param data the data to print
     * @param printer the printer to print to
     * @param encoding the encoding to use
     * @param printInSeparateThread true/false whether to print a separate thread (will not catch errors)
     * @param wait time in ms to wait after printing before allowing another job to be sent to same printer
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    void printViaSocket(String data, Printer printer, String encoding, Boolean printInSeparateThread, Integer wait)
            throws UnableToPrintViaSocketException;


    /**
     * Prints to the specified printer, using the print handler associated with that printer
     * Parameters to pass in vary: for example, the SocketPrintHandler (the default print handler) expects
     * a "data" key in the paramMap, where the value is the content to send out over socket
     *
     * @param paramMap used to pass parameters and contents to the print handler
     * @param printer the printer to print to
     * @param printInSeparateThread true/false whether to print a separate thread (will not catch errors)
     */
    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    void print(Map<String,Object> paramMap, Printer printer, Boolean printInSeparateThread)
        throws UnableToPrintException;

    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    void print(Map<String,Object> paramMap, Printer printer, Boolean printInSeparateThread, PrintHandler printHandler)
            throws UnableToPrintException;

    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    Collection<PrintHandler> getRegisteredPrintHandlers();

    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_ACCESS_PRINTERS)
    PrintHandler getRegisteredPrintHandlerByName(String beanName);

    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_MANAGE_PRINTERS)
    void registerPrintHandler(PrintHandler printHandler);

    @Authorized(PrinterConstants.PRIVILEGE_PRINTERS_MANAGE_PRINTERS)
    void unregisterPrintHandler(String beanName);

}
