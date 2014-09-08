package org.openmrs.module.printer;

import junit.framework.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openmrs.Form;
import org.openmrs.Location;
import org.openmrs.Patient;
import org.openmrs.api.LocationService;
import org.openmrs.api.PatientService;
import org.openmrs.module.htmlformentry.FormEntryContext;
import org.openmrs.module.htmlformentry.FormEntrySession;
import org.openmrs.module.htmlformentry.HtmlForm;
import org.openmrs.test.BaseModuleContextSensitiveTest;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Date;

public class PrinterVelocityFunctionsComponentTest extends BaseModuleContextSensitiveTest {

    @Autowired
    private LocationService locationService;

    @Autowired
    private PrinterService printerService;

    @Autowired
    private PatientService patientService;

    private PrinterVelocityFunctions velocityFunctions;

    @Before
    public void beforeAllTests() throws Exception {
        executeDataSet("printerServiceComponentTestDataset.xml");
        velocityFunctions = new PrinterVelocityFunctions(printerService);
    }

    @Test
    public void testShouldReturnTrueBecauseDefaultPrinterExists() {
        Location location = locationService.getLocation(3);   // printer #1 has been set as the default label printer for location 3 in the test dataset
        Assert.assertTrue(velocityFunctions.isDefaultPrinterConfigured(location, Printer.Type.LABEL));
    }

    @Test
    public void testShouldReturnFalseIfNoDefaultPrinterForLocation() {
        Location location = locationService.getLocation(2);   // no default printers have been configured for location 2
        Assert.assertFalse(velocityFunctions.isDefaultPrinterConfigured(location, Printer.Type.LABEL));
    }

    @Test
    public void testShouldReturnFalseIfNoDefaultPrinterOfProperTypeForLocation() {
        Location location = locationService.getLocation(3);   // printer #1 has been set as the default label printer for location 3, but it is a label printer
       Assert.assertFalse(velocityFunctions.isDefaultPrinterConfigured(location, Printer.Type.ID_CARD));
    }

    @Test
    public void testShouldProperlyParseVelocityExpression() throws Exception {

        // mock form
        HtmlForm htmlForm = new HtmlForm();
        htmlForm.setDateCreated(new Date());
        Form form = new Form();
        htmlForm.setForm(form);

        Patient patient = patientService.getPatient(1);
        Location location = locationService.getLocation(3);
        FormEntrySession session = new FormEntrySession(patient, htmlForm, FormEntryContext.Mode.ENTER, null);
        session.getContext().setDefaultLocation(location);

        Assert.assertTrue(session.evaluateVelocityExpression("$printer.fn.isDefaultPrinterConfigured($context.defaultLocation, $printer.type.LABEL)").trim().equals("true"));

    }
}

