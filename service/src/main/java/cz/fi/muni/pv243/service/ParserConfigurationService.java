/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.service;

import cz.fi.muni.pv243.entity.Day;
import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.Restaurant;

import java.util.List;

/**
 *
 * @author Michaela Bocanova
 */
public interface ParserConfigurationService {
    
    /**
     * parser is used by system since it's confirmed by admin
     * @param parserId 
     */
    void confirm(long parserId);

    List<Parser> getAll();

	List<Parser> getAll(boolean confirmed);

	Parser getConfirmedParser(Restaurant restaurant, Day day);
    
}
