package org.openmrs.module.printer;

import org.openmrs.Location;

/**
 * Functions to be provided in the velocity context availabe to html forms
 */
public class PrinterVelocityFunctions {

    private PrinterService printerService;

    public PrinterVelocityFunctions(PrinterService printerService) {
        this.printerService = printerService;
    }

    /**
     * Example usage in Html Form: <includeIf velocityTest="$printer.fn.isDefaultPrinterConfigured($sessionContext.sessionLocation, $printer.type.WRISTBAND)">
     */
    public boolean isDefaultPrinterConfigured(Location location, Printer.Type type) {
        return printerService.getDefaultPrinter(location, type) != null ? true : false;
    }

}
