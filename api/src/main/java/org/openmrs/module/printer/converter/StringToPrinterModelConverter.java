package org.openmrs.module.printer.converter;

import org.apache.commons.lang.StringUtils;
import org.openmrs.module.printer.PrinterModel;
import org.openmrs.module.printer.PrinterService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.convert.converter.Converter;

public class StringToPrinterModelConverter implements Converter<String, PrinterModel> {

    @Autowired
    @Qualifier("printerService")
    private PrinterService printerService;

    @Override
    public PrinterModel convert(String s) {
        if (StringUtils.isBlank(s)) {
            return null;
        } else {
            return printerService.getPrinterModelById(Integer.valueOf(s));
        }
    }

}
