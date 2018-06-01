package cz.fi.muni.pv243.store;

import cz.fi.muni.pv243.entity.Parser;

import java.util.List;

public interface ParserStore {

    List<Parser> getAllParsers();

    Parser addParser(Parser parser);

    Parser findParser(Long id);
    
    Parser updateParser(Parser parser);
}
