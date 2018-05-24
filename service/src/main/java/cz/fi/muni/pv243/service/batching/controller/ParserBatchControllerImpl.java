/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.service.batching.controller;

import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author Michaela Bocanova
 */
@ApplicationScoped
public class ParserBatchControllerImpl implements ParserBatchController {

    private final String parserJobName = "parser-job";
    
    @Override
    public void startJob() {
        
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = new Properties();
        long execID = jobOperator.start(parserJobName, jobParameters);
        //log
    }
}
