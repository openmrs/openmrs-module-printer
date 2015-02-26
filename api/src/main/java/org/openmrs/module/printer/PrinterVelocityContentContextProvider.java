package org.openmrs.module.printer;

import org.apache.velocity.VelocityContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.velocity.VelocityContextContentProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

@Component
public class PrinterVelocityContentContextProvider implements VelocityContextContentProvider {

    @Autowired
    private PrinterService printerService;

    @Override
    public void populateContext(FormEntrySession session, VelocityContext velocityContext) {

        Map printerVelocityContext = new HashMap();
        printerVelocityContext.put("fn", new PrinterVelocityFunctions(printerService));

        // better way to do this? adding all enum values to be available in context
        Map typeMap = new HashMap();
        for (PrinterType type : PrinterType.values()) {
            typeMap.put(type.toString(), type);
        }
        printerVelocityContext.put("type", typeMap);

        velocityContext.put("printer", printerVelocityContext);
    }

}
