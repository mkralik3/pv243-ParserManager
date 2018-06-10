package cz.fi.muni.pv243.rest;


import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.service.ParserService;
import org.jboss.annotation.security.SecurityDomain;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.util.List;

@Path("/parsers")
@SecurityDomain("keycloak")
@Stateless
public class ParserRestService {

    @Inject
    private ParserService parserService;

    @GET
    @Produces("application/json")
    @PermitAll
    public List<Parser> getAllParsers(@DefaultValue("false")
                                      @QueryParam(value = "confirmed")
                                      boolean confirmed){
        return parserService.getAllParsers(confirmed);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @PermitAll
    public Parser createParser(@Valid Parser parser) {
        return parserService.addParser(parser);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    @Path("/accept/{parserId}")
    @RolesAllowed({"admin", "moderator"})
    public Response acceptParser(@PathParam("parserId") Long parserId) {
        parserService.confirm(parserId);
        return Response.ok().build();
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    @PermitAll
    public Parser updateParser(@Valid Parser parser) {
        return parserService.updateParser(parser);
    }
}