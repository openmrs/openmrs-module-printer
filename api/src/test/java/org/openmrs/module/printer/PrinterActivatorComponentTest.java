package org.openmrs.module.printer;

import org.junit.Test;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.module.emrapi.EmrApiConstants;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import static org.hamcrest.core.Is.is;
import static org.hamcrest.core.IsNull.notNullValue;
import static org.junit.Assert.assertThat;

public class PrinterActivatorComponentTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private LocationService locationService;

    @Test
    public void confirmThatLocationAttributeTypesHaveBeenCreated() {
        PrinterModuleActivator activator = new PrinterModuleActivator();
        activator.willStart();
        activator.started();

        LocationAttributeType defaultIdCardPrinter = locationService.getLocationAttributeTypeByUuid(PrinterConstants.LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER.get(Printer.Type.ID_CARD.name()));
        LocationAttributeType defaultLabelPrinter = locationService.getLocationAttributeTypeByUuid(PrinterConstants.LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER.get(Printer.Type.LABEL.name()));

        assertThat(defaultIdCardPrinter, is(notNullValue()));
        assertThat(defaultLabelPrinter, is(notNullValue()));
    }


}
