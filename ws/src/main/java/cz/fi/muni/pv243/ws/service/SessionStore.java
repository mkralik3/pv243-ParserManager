/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.service;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

/**
 *
 * @author Michaela Bocanova
 */
@ApplicationScoped
public class SessionStore {

    //private List<Session> sessions = Collections.synchronizedList(new LinkedList<Session>());
    private ConcurrentHashMap<String, Session> sessions = new ConcurrentHashMap<>();

    public void addSession(Session session) {
        sessions.put(session.getId(), session);
    }

    public void removeSession(String id) {
        sessions.remove(id);
    }

    public Session getSession(String id) {
        return sessions.get(id);
    }

    public Collection<Session> getSessions() {
        return Collections.unmodifiableCollection(sessions.values());
    }

}
