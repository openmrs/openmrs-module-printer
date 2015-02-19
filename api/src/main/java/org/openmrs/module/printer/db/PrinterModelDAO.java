package org.openmrs.module.printer.db;

import org.openmrs.module.emrapi.db.SingleClassDAO;
import org.openmrs.module.printer.PrinterModel;
import org.openmrs.module.printer.PrinterType;

import java.util.List;

public interface PrinterModelDAO extends SingleClassDAO<PrinterModel> {

    /**
     * Fetches all printer model of a specified type
     *
     * @param type
     * @return
     */
    List<PrinterModel> getPrinterModelsByType(PrinterType type);


    /**
     * Given a printer model, returns true/false if that name is in use
     * by *another* printer model
     *
     * @return
     */
    boolean isNameAllocatedToAnotherPrinterModel(PrinterModel printerModel);

}
