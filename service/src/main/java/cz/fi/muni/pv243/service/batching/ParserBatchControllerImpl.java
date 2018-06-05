/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.service.batching;

import java.util.Properties;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.ejb.Schedule;
import javax.enterprise.context.ApplicationScoped;

import cz.fi.muni.pv243.service.logging.ParserManagerLogger;

/**
 *
 * @author Michaela Bocanova
 */
@ApplicationScoped
public class ParserBatchControllerImpl implements ParserBatchController {

    private final String parserJobName = "parser-job";
    
    /* (non-Javadoc)
     * @see cz.fi.muni.pv243.service.batching.ParserBatchController#startJob()
     */
    @Override
    @Schedule(dayOfWeek = "Mon", hour = "0"/*, persistent = "false"*/)
    public void startJob() {
        
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = new Properties();
        long execID = jobOperator.start(parserJobName, jobParameters);
        ParserManagerLogger.LOGGER.logBatchJobRunning(execID);
    }
}
