package cz.fi.muni.pv243.service.batching;

import java.util.Properties;

import javax.batch.api.listener.AbstractJobListener;
import javax.batch.operations.JobOperator;
import javax.batch.runtime.BatchRuntime;
import javax.batch.runtime.BatchStatus;
import javax.batch.runtime.context.JobContext;
import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Named;

import cz.fi.muni.pv243.service.logging.ParserManagerLogger;

/**
 * 
 * @author Michaela Bocanova
 *
 */
@Named("jobListener")
public class JobListener extends AbstractJobListener {

    @Inject
    private JobContext jobContext;

    @Override
    public void beforeJob() throws Exception {
        ParserManagerLogger.LOGGER.logBatchJobStart(jobContext.getJobName(), jobContext.getExecutionId());
    }

    @Override
    public void afterJob() throws Exception {
        JobOperator jobOperator = BatchRuntime.getJobOperator();
        Properties jobParameters = jobOperator.getParameters(jobContext.getExecutionId());
        //if already restarted, mark as abandoned
        String restarted = jobParameters.getProperty("restarted");
        if (restarted != null && restarted.equalsIgnoreCase("true") 
                && jobContext.getBatchStatus().equals(BatchStatus.FAILED)) {
            ParserManagerLogger.LOGGER.logBatchJobAbandon(jobContext.getJobName(), jobContext.getExecutionId(), jobContext.getBatchStatus().toString());
            jobOperator.abandon(jobContext.getExecutionId());
        }
        ParserManagerLogger.LOGGER.logBatchJobStop(jobContext.getJobName(), jobContext.getExecutionId(), jobContext.getBatchStatus().toString());
    }
}
