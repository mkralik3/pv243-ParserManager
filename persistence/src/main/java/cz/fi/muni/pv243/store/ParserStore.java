package cz.fi.muni.pv243.store;

import cz.fi.muni.pv243.entity.Day;
import cz.fi.muni.pv243.entity.Parser;

import java.util.List;

public interface ParserStore {

    Parser addParser(Parser parser);

    Parser updateParser(Parser parser);

    void deleteParser(Parser parser);

    List<Parser> getAllParsers(boolean confirmed);

    Parser findParser(Long id);

    Parser getConfirmedParser(String restaurantId, Day day);
}
