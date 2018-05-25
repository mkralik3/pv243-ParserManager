/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.service;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.ParserConfiguration;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.store.CachedParserConfigurationStore;
import cz.fi.muni.pv243.infinispan.store.CachedParserStore;
import java.util.List;
import javax.inject.Inject;

/**
 *
 * @author Michaela Bocanova
 */
public class ParserConfigurationServiceImpl implements ParserConfigurationService {

    @Inject
    @CachedStore
    private CachedParserStore parserStore;
    
    @Inject
    @CachedStore
    private CachedParserConfigurationStore configStore;
    
    @Override
    public void confirm(ParserConfiguration config) {
    	Parser parser = parserStore.findParser(config.getRestaurantId());
    	if (parser != null) {    		
    		parser.setXpath(config.getXpath());
    		//parserStore.addParser(parser);//update
    	}
    	else {
    		parser = new Parser();
    		//parserStore.addParser(parser);
    	}
    	config.setConfirmed(true);
		//configStore.update();
    }
    
    @Override
    public List<ParserConfiguration> getAll() {
        return configStore.getAllParsers();
    }
    
}
