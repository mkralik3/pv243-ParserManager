package cz.fi.muni.pv243.rest;


import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.store.CachedParserStore;
import cz.fi.muni.pv243.service.ParserService;

import javax.inject.Inject;
import javax.ws.rs.*;
import java.util.List;

@Path("/parsers")
public class ParserRestService {

    @Inject
    private ParserService parserService;

    @GET
    @Produces("application/json")
    public List<Parser> getAllParsers(@DefaultValue("false")
                                      @QueryParam(value = "confirmed")
                                      boolean confirmed){
        return parserService.getAllParsers(confirmed);
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Parser createParser(Parser parser) {
        return parserService.addParser(parser);
    }

    @PUT
    @Consumes("application/json")
    @Produces("application/json")
    public Parser updateParser(Parser parser) {
        return parserService.updateParser(parser);
    }
}