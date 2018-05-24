/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.endpoint;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.service.ParserConfigurationService;
import cz.fi.muni.pv243.ws.service.WSJMSMessage;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import javax.ejb.Singleton;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.websocket.OnClose;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/**
 *
 * @author Michaela Bocanova
 */
@Singleton
@ServerEndpoint("/notifications")
public class ParserConfigurationEndpoint {
    
    private ConcurrentMap<String, Session> sessions = new ConcurrentHashMap<>();
    
    @Inject
    private ParserConfigurationService service;
    
    @OnMessage
    public void onMessage(String message, Session session) {
        
        System.out.println("Message: '" + message + "'");
    }

    @OnOpen
    public void onOpen(Session session) {
        sessions.put(session.getUserPrincipal().getName(), session);
        send(service.getAll(), session);
        System.out.println("WebSocket opened: " + session.getId());
    }

    @OnClose
    public void onClose(Session session) {
        sessions.remove(session.getUserPrincipal().getName());
        System.out.println("WebSocket connection closed: " + session.getId());
    }
    
    public void onJMSMessage(@Observes @WSJMSMessage List<Parser> parsers) {
        send(parsers);        
    }
    
    private void send(List<Parser> all) {
        sessions.entrySet().stream()
                .forEach(session -> {
                    session.getValue().getAsyncRemote().sendObject(all);
                });
    }

    private void send(List<Parser> all, Session session) {
        session.getAsyncRemote().sendObject(all);
    }
    
}
