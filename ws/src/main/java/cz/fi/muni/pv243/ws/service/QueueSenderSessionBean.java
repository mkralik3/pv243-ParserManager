/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.service;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;
import javax.inject.Named;

/**
 *
 * @author Michaela Bocanova
 */
@Named
@LocalBean
@Stateless
public class QueueSenderSessionBean {
    
	/*@Resource(mappedName="jms/myQueue")
	private Queue myQueue;
	@Inject 
	private JMSContext jmsContext;
	
	public void sendMessage(String message) {
		
		
	}*/
}
