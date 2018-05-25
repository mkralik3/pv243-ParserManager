package cz.fi.muni.pv243.infinispan.store;

import java.util.List;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.transaction.Transactional;

import org.infinispan.Cache;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.ParserConfiguration;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.annotation.DefaultCacheConfiguration;
import cz.fi.muni.pv243.jpa.annotation.JPAStore;
import cz.fi.muni.pv243.store.ParserConfigurationStore;
import cz.fi.muni.pv243.store.ParserStore;

@Named
@ApplicationScoped
@CachedStore
public class CachedParserConfigurationStore implements ParserConfigurationStore {

	@Inject
    @JPAStore
    private ParserConfigurationStore delegate;

    @Inject
    @DefaultCacheConfiguration
    private Cache<Long, ParserConfiguration> parserCache;

    @Override
    public List<ParserConfiguration> getAllParsers() {
        return delegate.getAllParsers();
    }

    @Override
    @Transactional
    public ParserConfiguration addParser(ParserConfiguration parser){
        parser = delegate.addParser(parser);
        parserCache.put(parser.getId(), parser);
        return parser;
    }

    @Override
    public ParserConfiguration findParser(Long id) {
        return delegate.findParser(id);
    }
}
