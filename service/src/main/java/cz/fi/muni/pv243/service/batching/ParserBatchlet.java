/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.service.batching;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.service.ParserService;
import cz.fi.muni.pv243.service.ParserUtil;

import javax.batch.api.Batchlet;
import javax.batch.runtime.BatchStatus;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;
import java.util.List;

/**
 * @author Michaela Bocanova
 */
@Named("parserBatchlet")
@ApplicationScoped
public class ParserBatchlet implements Batchlet {

    @Inject
    private ParserService parserService;

    @Override
    public String process() throws Exception {

        List<Parser> parsers = parserService.getAllParsers(true);
        for (Parser parser : parsers) {
            ParserUtil.execute(parser);
        }
        return BatchStatus.COMPLETED.toString();
    }

    @Override
    public void stop() throws Exception {
    }

}
