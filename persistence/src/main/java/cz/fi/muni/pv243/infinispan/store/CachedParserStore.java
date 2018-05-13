package cz.fi.muni.pv243.infinispan.store;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.ParserStore;
import org.infinispan.Cache;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;
import java.util.List;

@Named
@ApplicationScoped
@CachedStore
public class CachedParserStore implements ParserStore {

    @Inject
    @JPAStore
    private ParserStore delegate;

    @Inject
    @DefaultCacheConfiguration
    private Cache<Long, Parser> parserCache;

    @Override
    public List<Parser> getAllParsers() {
        return delegate.getAllParsers();
    }

    @Override
    @Transactional
    public Parser addParser(Parser parser){
        parser = delegate.addParser(parser);
        parserCache.put(parser.getId(), parser);
        return parser;
    }
}