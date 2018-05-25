/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.endpoint;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.entity.ParserConfiguration;
import cz.fi.muni.pv243.service.ParserConfigurationService;
import cz.fi.muni.pv243.service.ParserManagerLogger;
import cz.fi.muni.pv243.ws.service.ParserConfigurationDecoder;
import cz.fi.muni.pv243.ws.service.ParserConfigurationEncoder;
import cz.fi.muni.pv243.ws.service.SessionStore;
import cz.fi.muni.pv243.ws.service.WSJMSMessage;

import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/**
 *
 * @author Michaela Bocanova
 */
@ApplicationScoped
@ServerEndpoint(value = "/ws", encoders = { ParserConfigurationEncoder.class }/*, decoders = { ParserConfigurationDecoder.class }*/)
public class ParserConfigurationServerEndpoint {
    
    @Inject
    private SessionStore clients;
    
    @Inject
    private ParserConfigurationService service;
    
    @OnMessage
    public void onMessage(final ParserConfiguration message, final Session session) {
    	
        service.confirm(message);
    }

    @OnOpen
    public void onOpen(final Session session) {
        clients.addSession(session);
        sendToSession(service.getAll(), session);
        ParserManagerLogger.LOGGER.logWebsocketConnect(getClass().getSimpleName());
    }

    @OnClose
    public void onClose(final Session session) {
        clients.removeSession(session.getId());
        ParserManagerLogger.LOGGER.logWebsocketDisconnect(getClass().getSimpleName());
    }
    
    @OnError
    public void onError(Throwable error) {
    	ParserManagerLogger.LOGGER.logWebsocketError(getClass().getSimpleName(), error);
    }
    
    public void onJMSMessage(@Observes @WSJMSMessage List<ParserConfiguration> parsers) {
        sendToAllSessions(parsers);        
    }
    
    private void sendToAllSessions(List<ParserConfiguration> all) {
        for (Session session : clients.getSessions()) {
            sendToSession(all, session);
        }
    }

    private void sendToSession(List<ParserConfiguration> all, Session session) {
        session.getAsyncRemote().sendObject(all);
    }
    
}
