package cz.fi.muni.pv243.rest;


import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.store.CachedParserStore;

import javax.inject.Inject;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

@Path("/parsers")
public class ParserService {

    @Inject
    @CachedStore
    private CachedParserStore parserStore;

    @GET
    @Produces("application/json")
    public List<Parser> getAllParsers() {
        return parserStore.getAllParsers();
    }

    @POST
    @Consumes("application/json")
    @Produces("application/json")
    public Parser createParser(Parser parser) {
        parserStore.addParser(parser);
        return parser;
    }
}