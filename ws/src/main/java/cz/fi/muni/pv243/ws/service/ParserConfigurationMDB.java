/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.service;

import javax.ejb.MessageDriven;
import javax.enterprise.event.Event;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 *
 * @author Michaela Bocanova
 */
@Named
@MessageDriven(mappedName = "jms/myQueue")
public class ParserConfigurationMDB implements MessageListener {

	@Inject
    @WSJMSMessage     
    Event<Message> jmsEvent; 
	
	@Override
	public void onMessage(Message message) {
		jmsEvent.fire(message);		
	}
    
}
