package org.openmrs.module.printer.rest.resource;

import org.openmrs.api.APIException;
import org.openmrs.module.printer.Printer;
import org.openmrs.module.webservices.rest.web.RequestContext;
import org.openmrs.module.webservices.rest.web.RestConstants;
import org.openmrs.module.webservices.rest.web.annotation.Resource;
import org.openmrs.module.webservices.rest.web.representation.DefaultRepresentation;
import org.openmrs.module.webservices.rest.web.representation.FullRepresentation;
import org.openmrs.module.webservices.rest.web.representation.Representation;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingCrudResource;
import org.openmrs.module.webservices.rest.web.resource.impl.DelegatingResourceDescription;
import org.openmrs.module.webservices.rest.web.response.ResponseException;

@Resource(name = RestConstants.VERSION_1 + "/printer/printer", supportedClass = Printer.class,
        supportedOpenmrsVersions = {"2.0.0 - 9.*"})
public class PrinterResource extends DelegatingCrudResource<Printer> {


    @Override
    public DelegatingResourceDescription getRepresentationDescription(Representation rep) {
        if (rep instanceof DefaultRepresentation || rep instanceof FullRepresentation) {
            DelegatingResourceDescription description = new DelegatingResourceDescription();
            description.addProperty("uuid");
            description.addProperty("name");
            description.addProperty("ipAddress");
            description.addProperty("port");
            description.addProperty("type");
            description.addProperty("model", Representation.DEFAULT);
            description.addProperty("physicalLocation", Representation.REF);
            return description;
        }
        return null;
    }

    @Override
    public Printer getByUniqueId(String s) {
        throw new APIException("Not yet implemented");
    }

    @Override
    protected void delete(Printer printer, String s, RequestContext requestContext) throws ResponseException {
        throw new APIException("Not yet implemented");
    }

    @Override
    public Printer newDelegate() {
        throw new APIException("Not yet implemented");
    }

    @Override
    public Printer save(Printer printer) {
        throw new APIException("Not yet implemented");
    }

    @Override
    public void purge(Printer printer, RequestContext requestContext) throws ResponseException {
        throw new APIException("Not yet implemented");
    }

}
