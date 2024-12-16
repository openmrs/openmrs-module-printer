package org.openmrs.module.printer.rest.resource;

import org.openmrs.api.APIException;
import org.openmrs.module.printer.PrinterModel;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + "/printer/printermodel", supportedClass = PrinterModel.class,
        supportedOpenmrsVersions = {"2.0.0 - 9.*"})
public class PrinterModelResource extends DelegatingCrudResource<PrinterModel> {

    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
        if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("name");
            description.addProperty("type");
            description.addProperty("printHandler");
            return description;
        }
        return null;
    }

    @Override
    public PrinterModel getByUniqueId(String s) {
        throw new APIException("Not yet implemented");
    }

    @Override
    protected void delete(PrinterModel printerModel, String s, RequestContext requestContext) throws ResponseException {
        throw new APIException("Not yet implemented");
    }

    @Override
    public PrinterModel newDelegate() {
        throw new APIException("Not yet implemented");
    }

    @Override
    public PrinterModel save(PrinterModel printerModel) {
        throw new APIException("Not yet implemented");
    }

    @Override
    public void purge(PrinterModel printerModel, RequestContext requestContext) throws ResponseException {
        throw new APIException("Not yet implemented");
    }


}
