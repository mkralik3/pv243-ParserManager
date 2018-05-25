/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.service;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
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
    
    @Override
    public void confirm(Parser parser) {
    	parser.setValid(true);
    	parserStore.addParser(parser);//update
        //Parser parser = parserStore.findParser(parserId);
    }
    
    @Override
    public List<Parser> getAll() {
        return parserStore.getAllParsers();
    }
    
}
