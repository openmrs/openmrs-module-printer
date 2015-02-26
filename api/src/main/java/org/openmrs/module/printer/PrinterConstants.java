package org.openmrs.module.printer;

import java.util.HashMap;
import java.util.Map;

public class PrinterConstants {

    public static final String SOCKET_PRINT_HANDLER_BEAN_NAME = "socketPrintHandler";

    public static final String PRIVILEGE_PRINTERS_ACCESS_PRINTERS = "Printers - Access Printers";

    public static final String PRIVILEGE_PRINTERS_MANAGE_PRINTERS = "Printers - Manage Printers";

    // remember if we add more types here to also create them in the module activator
    public static final Map<String, String> LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER = new HashMap<String, String>() {{
        put(PrinterType.ID_CARD.name(), "b48ef9a0-38d3-11e2-81c1-0800200c9a66");
        put(PrinterType.LABEL.name(), "bd6c1c10-38d3-11e2-81c1-0800200c9a66");
        put(PrinterType.WRISTBAND.name(), "7f73ad30-0b89-11e4-9191-0800200c9a66");
    }};


}
