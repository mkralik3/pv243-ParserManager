package cz.fi.muni.pv243.service.logging;

import org.jboss.logging.BasicLogger;
import org.jboss.logging.Logger;
import org.jboss.logging.annotations.LogMessage;
import org.jboss.logging.annotations.Message;
import org.jboss.logging.annotations.MessageLogger;

import cz.fi.muni.pv243.entity.Parser;

/**
 * 
 * @author Michaela Bocanova
 *
 */
@MessageLogger(projectCode = "PARSERMNG")
public interface ParserManagerLogger extends BasicLogger {

    ParserManagerLogger LOGGER = Logger.getMessageLogger(ParserManagerLogger.class, ParserManagerLogger.class.getPackage().getName());
    int BASE = 1000;

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 0, value = "Created new parser: {0}", format = Message.Format.MESSAGE_FORMAT)
    void logParserCreated(Parser parser);

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 110, value = "Connected websocket: {0}", format = Message.Format.MESSAGE_FORMAT)
    void logWebsocketConnect(String websocket);

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 115, value = "Disconnected websocket: {0}", format = Message.Format.MESSAGE_FORMAT)
    void logWebsocketDisconnect(String websocket);

    @LogMessage(level = Logger.Level.WARN)
    @Message(id = BASE + 120, value = "Websocket: {0} encountered connection error: {1}", format = Message.Format.MESSAGE_FORMAT)
    void logWebsocketError(String websocket, Throwable error);

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 125, value = "Added session: {0}. Total number of sessions: {1}", format = Message.Format.MESSAGE_FORMAT)
    void logAddSession(String sessionId, int size);

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 130, value = "Removed session: {0}. Total number of sessions: {1}", format = Message.Format.MESSAGE_FORMAT)
    void logRemoveSession(String sessionId, int size);

    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 135, value = "Starting batch job {0}: with execution id = {1}", format = Message.Format.MESSAGE_FORMAT)
    void logBatchJobStart(String jobName, long jobId);
    
    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 136, value = "Stopping batch job {0}: with execution id = {1} and status: {2}", format = Message.Format.MESSAGE_FORMAT)
    void logBatchJobStop(String jobName, long jobId, String status);
    
    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 137, value = "Abandonig batch job {0}: with execution id = {1} and status: {2}", format = Message.Format.MESSAGE_FORMAT)
    void logBatchJobAbandon(String jobName, long jobId, String status);
    
    @LogMessage(level = Logger.Level.INFO)
    @Message(id = BASE + 140, value = "Message: {0} in queue: {1}.", format = Message.Format.MESSAGE_FORMAT)
    void logMessageQueued(String queue, String msg);
    
    @LogMessage(level = Logger.Level.WARN)
    @Message(id = BASE + 145, value = "JMS queue encountered error: {0}", format = Message.Format.MESSAGE_FORMAT)
    void logMessageQueueError(Throwable error);
}
