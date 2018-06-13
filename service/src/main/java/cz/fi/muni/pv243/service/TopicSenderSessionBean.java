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
import javax.jms.Topic;

import cz.fi.muni.pv243.service.logging.ParserManagerLogger;

import java.io.Serializable;

/**
 *
 * @author Michaela Bocanova
 */
@Named
@LocalBean
@Stateless
public class TopicSenderSessionBean {

    @Resource(mappedName="java:jboss/exported/jms/topic/ChangedParsersTopic")
    private Topic myTopic;
    @Inject 
    private JMSContext jmsContext;

    public void sendMessage(Serializable message) {
        try {
            jmsContext.createProducer().send(myTopic, message/*jmsContext.createMessage()*/);
            ParserManagerLogger.LOGGER.logMessageTopiced(myTopic.getTopicName(), message.toString());
        } catch (JMSException e) {
            ParserManagerLogger.LOGGER.logMessageTopicError(e);
        }
    }
}
