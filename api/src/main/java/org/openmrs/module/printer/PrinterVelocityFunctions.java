package org.openmrs.module.printer;

import org.openmrs.Location;

import java.util.List;

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

    /**
     * Example usage in Html Form: (to render a list of uuids that could be assigned to a javascript variable
     * <lookup complexExpression="#set ($locations = $printer.fn.getLocationsWithDefaultPrinter($printer.type.LABEL)) #foreach ($location in $locations)'$location.uuid'#if ($velocityCount <= $locations.size() - 1),#end#end"/>
     */
    public List<Location> getLocationsWithDefaultPrinter(Printer.Type type) {
        return printerService.getLocationsWithDefaultPrinter(type);
    }

}
