/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.endpoint;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.service.ParserConfigurationService;
import cz.fi.muni.pv243.service.ParserManagerLogger;
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
@ServerEndpoint(value = "/ws")
public class ParserConfigurationServerEndpoint {
    
    @Inject
    private SessionStore sessions;
    
    @Inject
    private ParserConfigurationService service;
    
    @OnMessage
    public void onMessage(String parser, Session session) {
        //service.confirm(parser);
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.addSession(session);
        sendToSession(service.getAll(), session);
        ParserManagerLogger.LOGGER.logWebsocketConnect(getClass().getSimpleName());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.removeSession(session);
        ParserManagerLogger.LOGGER.logWebsocketDisconnect(getClass().getSimpleName());
    }
    
    @OnError
    public void onError(Throwable error) {
    	ParserManagerLogger.LOGGER.logWebsocketError(getClass().getSimpleName(), error);
    }
    
    public void onJMSMessage(@Observes @WSJMSMessage List<Parser> parsers) {
        sendToAllSessions(parsers);        
    }
    
    private void sendToAllSessions(List<Parser> all) {
        for (Session session : sessions.getSessions()) {
            sendToSession(all, session);
        }
    }

    private void sendToSession(List<Parser> all, Session session) {
        session.getAsyncRemote().sendObject(all);
    }
    
}
