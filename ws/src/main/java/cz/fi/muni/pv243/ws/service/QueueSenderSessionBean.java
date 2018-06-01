/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.service;

import javax.annotation.Resource;
import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSContext;
import javax.jms.Queue;

import cz.fi.muni.pv243.entity.Parser;

/**
 *
 * @author Michaela Bocanova
 */
@Named
@LocalBean
@Stateless
public class QueueSenderSessionBean {
    
	@Resource(mappedName="java:app/jms/myQueue")
	private Queue myQueue;
	@Inject 
	private JMSContext jmsContext;
	
	public void sendMessage(long message) {
		jmsContext.createProducer().send(myQueue, message/*jmsContext.createMessage()*/);
		
	}
}
