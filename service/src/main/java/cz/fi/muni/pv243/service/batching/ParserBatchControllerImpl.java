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
import javax.ejb.Singleton;

import cz.fi.muni.pv243.service.logging.ParserManagerLogger;

/**
 *
 * @author Michaela Bocanova
 */
@Singleton
public class ParserBatchControllerImpl implements ParserBatchController {

    private final String parserJobName = "parser-job";
    
    /* (non-Javadoc)
     * @see cz.fi.muni.pv243.service.batching.ParserBatchController#startJob()
     */
    @Override
    @Schedule(hour = "23", minute = "59", second = "59", dayOfWeek = "*", persistent = false)
    public void startJob() {        
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = new Properties();
        long executionId = jobOperator.start(parserJobName, jobParameters);
    }
    
    @Override
    public void restartJob(int executionId) {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = jobOperator.getParameters(executionId);
        jobParameters.setProperty("restarted", "true");
        long newExecutionId = jobOperator.restart(executionId, jobParameters);
        
    }
}
