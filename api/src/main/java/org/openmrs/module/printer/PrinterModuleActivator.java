/**
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


import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.openmrs.LocationAttributeType;
import org.openmrs.api.LocationService;
import org.openmrs.api.context.Context;
import org.openmrs.module.ModuleActivator;
import org.openmrs.module.emrapi.utils.GeneralUtils;

/**
 * This class contains the logic that is run every time this module is either started or stopped.
 */
public class PrinterModuleActivator implements ModuleActivator {
	
	protected Log log = LogFactory.getLog(getClass());
		
	/**
	 * @see ModuleActivator#willRefreshContext()
	 */
	public void willRefreshContext() {
		log.info("Refreshing Printer Module Module");
	}
	
	/**
	 * @see ModuleActivator#contextRefreshed()
	 */
	public void contextRefreshed() {
		log.info("Printer Module Module refreshed");
	}
	
	/**
	 * @see ModuleActivator#willStart()
	 */
	public void willStart() {
		log.info("Starting Printer Module Module");
	}
	
	/**
	 * @see ModuleActivator#started()
	 */
	public void started() {
        LocationService locationService = Context.getLocationService();
        createLocationAttributeTypes(locationService);
		log.info("Printer Module Module started");
	}
	
	/**
	 * @see ModuleActivator#willStop()
	 */
	public void willStop() {
		log.info("Stopping Printer Module Module");
	}
	
	/**
	 * @see ModuleActivator#stopped()
	 */
	public void stopped() {
		log.info("Printer Module Module stopped");
	}

    private void createLocationAttributeTypes(LocationService locationService) {
        LocationAttributeType defaultLabelPrinterAttributeType =
                locationService.getLocationAttributeTypeByUuid(PrinterConstants.LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER.get("LABEL"));

        if (defaultLabelPrinterAttributeType == null) {
            defaultLabelPrinterAttributeType = new LocationAttributeType();
            defaultLabelPrinterAttributeType.setUuid(PrinterConstants.LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER.get("LABEL"));
            defaultLabelPrinterAttributeType.setDatatypeClassname(PrinterDatatype.class.getName());
            defaultLabelPrinterAttributeType.setDatatypeConfig("LABEL");
            defaultLabelPrinterAttributeType.setMaxOccurs(1);
            defaultLabelPrinterAttributeType.setMinOccurs(0);
            defaultLabelPrinterAttributeType.setName("Default Label Printer");
            defaultLabelPrinterAttributeType.setDescription("The default label printer for this location");

            locationService.saveLocationAttributeType(defaultLabelPrinterAttributeType);
        } else {
            // if you change any field values above, you need to set them here, so existing servers can be updated
            boolean changed = GeneralUtils.setPropertyIfDifferent(defaultLabelPrinterAttributeType, "datatypeClassname", PrinterDatatype.class.getName());
            if (changed) {
                locationService.saveLocationAttributeType(defaultLabelPrinterAttributeType);
            }
        }

        LocationAttributeType defaultIdCardPrinterAttributeType =
                locationService.getLocationAttributeTypeByUuid(PrinterConstants.LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER.get("ID_CARD"));

        if (defaultIdCardPrinterAttributeType == null) {
            defaultIdCardPrinterAttributeType = new LocationAttributeType();
            defaultIdCardPrinterAttributeType.setUuid(PrinterConstants.LOCATION_ATTRIBUTE_TYPE_DEFAULT_PRINTER.get("ID_CARD"));
            defaultIdCardPrinterAttributeType.setDatatypeClassname(PrinterDatatype.class.getName());
            defaultIdCardPrinterAttributeType.setDatatypeConfig("ID_CARD");
            defaultIdCardPrinterAttributeType.setMaxOccurs(1);
            defaultIdCardPrinterAttributeType.setMinOccurs(0);
            defaultIdCardPrinterAttributeType.setName("Default ID card Printer");
            defaultIdCardPrinterAttributeType.setDescription("The default id card printer for this location");

            locationService.saveLocationAttributeType(defaultIdCardPrinterAttributeType);
        } else {
            // if you change any field values above, you need to set them here, so existing servers can be updated
            boolean changed = GeneralUtils.setPropertyIfDifferent(defaultIdCardPrinterAttributeType, "datatypeClassname", PrinterDatatype.class.getName());
            if (changed) {
                locationService.saveLocationAttributeType(defaultIdCardPrinterAttributeType);
            }
        }
    }

}
