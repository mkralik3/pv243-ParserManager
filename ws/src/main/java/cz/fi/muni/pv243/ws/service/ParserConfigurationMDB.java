/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.service;

import java.util.List;

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
 *
 * @author Michaela Bocanova
 */
@Named
@JMSDestinationDefinition(
        name = "java:app/jms/myQueue",
        interfaceName = "javax.jms.Queue",
        destinationName = "myQueue"
)
@MessageDriven(mappedName = "java:app/jms/myQueue")
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
