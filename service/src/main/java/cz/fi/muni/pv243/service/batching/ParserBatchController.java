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

/**
 *
 * @author Michaela Bocanova
 */
@ApplicationScoped
public class ParserBatchController {

    private final String parserJobName = "parser-job";
    
    @Schedule(dayOfWeek = "Mon", hour = "0"/*, persistent = "false"*/)
    public void startJob() {
        
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = new Properties();
        long execID = jobOperator.start(parserJobName, jobParameters);
        //log
    }
}
