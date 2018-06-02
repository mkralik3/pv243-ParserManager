/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.service;

import java.util.List;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSDestinationDefinition;
import javax.jms.Message;
import javax.jms.MessageListener;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.service.ParserConfigurationService;

/**
 * message-driven bean to process messages asynchronously
 *
 * @author Michaela Bocanova
 */
@Named
@MessageDriven(name = "ParserConfigurationHandler",
activationConfig = {
		@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Queue"),
        @ActivationConfigProperty(propertyName = "destination", propertyValue = "java:jboss/exported/jms/queue/myQueue"),
        @ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge")
        /*mappedName = "jms/myQueue"*/})
public class ParserConfigurationMDB implements MessageListener {

	@Inject
    @WSJMSMessage
    private Event<List<Parser>> jmsEvent;

    @Inject
    private ParserConfigurationService service;
	
	@Override
	public void onMessage(Message message) {
		jmsEvent.fire(service.getAll(false));		
	}
    
}
