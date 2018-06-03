/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.endpoint;

import cz.fi.muni.pv243.entity.Parser;
import cz.fi.muni.pv243.infinispan.annotation.CachedStore;
import cz.fi.muni.pv243.infinispan.store.CachedParserStore;
import cz.fi.muni.pv243.service.ParserService;
import cz.fi.muni.pv243.service.ParserManagerLogger;
import cz.fi.muni.pv243.service.QueueSenderSessionBean;
import cz.fi.muni.pv243.ws.service.Action;
import cz.fi.muni.pv243.ws.service.ActionMessage;
import cz.fi.muni.pv243.ws.service.ActionMessageDecoder;
import cz.fi.muni.pv243.ws.service.ParserEncoder;
import cz.fi.muni.pv243.ws.service.ParsersEncoder;
import cz.fi.muni.pv243.ws.service.SessionStore;
import cz.fi.muni.pv243.ws.service.WSJMSMessage;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.event.Observes;
import javax.inject.Inject;
import javax.inject.Named;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import java.util.List;


/**
 *
 * @author Michaela Bocanova
 */
@Named
@ApplicationScoped
@ServerEndpoint(value = "/ws", encoders = { ParsersEncoder.class, ParserEncoder.class }, decoders = { ActionMessageDecoder.class })
public class ParserConfigurationServerEndpoint {

    @Inject
    private SessionStore clients;

    @Inject
    private ParserService service;

    @Inject
    @CachedStore
    private CachedParserStore parserStore;

    @Inject
    private QueueSenderSessionBean senderBean;

    @OnMessage
    public void onMessage(ActionMessage msg, final Session session) {

        long parserId = Long.valueOf(msg.getId());
        if (msg.getAction().equals(Action.DETAIL)) {
            Parser parser = parserStore.findParser(parserId);
            Parser confirmed = service.getConfirmedParser(parser.getRestaurant(), parser.getDay());

            sendToSession(confirmed, session);
        } else if (msg.getAction().equals(Action.CONFIRM)) {
            service.confirm(parserId);
        }
    }

    private void sendToSession(Parser confirmed, Session session) {
        session.getAsyncRemote().sendObject(confirmed);
    }

    @OnOpen
    public void onOpen(final Session session) {
        clients.addSession(session);
        sendToSession(service.getAllParsers(false), session);
        ParserManagerLogger.LOGGER.logWebsocketConnect(getClass().getSimpleName());
    }

    @OnClose
    public void onClose(final Session session) {
        clients.removeSession(session.getId());
        ParserManagerLogger.LOGGER.logWebsocketDisconnect(getClass().getSimpleName());
    }

    @OnError
    public void onError(Throwable error) throws Throwable {
        ParserManagerLogger.LOGGER.logWebsocketError(getClass().getSimpleName(), error);
    }

    public void onJMSMessage(@Observes @WSJMSMessage List<Parser> parsers) {
        sendToAllSessions(parsers);        
    }

    private void sendToAllSessions(List<Parser> all) {
        for (Session session : clients.getSessions()) {
            sendToSession(all, session);
        }
    }

    private void sendToSession(List<Parser> all, Session session) {
        session.getAsyncRemote().sendObject(all);
    }

}
