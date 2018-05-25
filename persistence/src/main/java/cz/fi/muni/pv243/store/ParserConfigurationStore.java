package cz.fi.muni.pv243.store;

import java.util.List;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.ParserConfiguration;

/**
 * 
 * @author Michaela Bocanova
 *
 */
public interface ParserConfigurationStore {

	List<ParserConfiguration> getAllParsers();

    ParserConfiguration addParser(ParserConfiguration parser);

    ParserConfiguration findParser(Long id);

	ParserConfiguration updateParser(ParserConfiguration parser);
}
