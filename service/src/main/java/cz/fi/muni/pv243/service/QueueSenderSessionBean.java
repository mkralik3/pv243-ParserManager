/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.service;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.JMSException;
import javax.jms.Queue;

import cz.fi.muni.pv243.service.logging.ParserManagerLogger;

import java.io.Serializable;

/**
 *
 * @author Michaela Bocanova
 */
@Named
@LocalBean
@Stateless
public class QueueSenderSessionBean {

    @Resource(mappedName="java:jboss/exported/jms/queue/ChangedParsersQueue")
    private Queue myQueue;
    @Inject 
    private JMSContext jmsContext;

    public void sendMessage(Serializable message) {
        jmsContext.createProducer().send(myQueue, message/*jmsContext.createMessage()*/);
        try {
            ParserManagerLogger.LOGGER.logMessageQueued(myQueue.getQueueName(), message.toString());
        } catch (JMSException e) {
            ParserManagerLogger.LOGGER.logMessageQueueError(e);
        }
    }
}
