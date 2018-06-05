package cz.fi.muni.pv243.rest;


import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.store.CachedParserStore;
import cz.fi.muni.pv243.service.ParserService;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/parsers")
public class ParserRestService {

    @Inject
    private ParserService parserService;

    @GET
    @Produces("application/json")
    public List<Parser> getAllParsers() {
        return parserService.getAllParsers();
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