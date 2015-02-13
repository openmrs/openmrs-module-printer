package org.openmrs.module.printer.handler;

import org.openmrs.module.printer.Printer;
import org.openmrs.module.printer.UnableToPrintException;

import java.util.Map;

public interface PrintHandler {
    public void print(Printer printer, Map<String,Object> paramMap) throws UnableToPrintException;
}
