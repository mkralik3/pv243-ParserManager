/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cz.fi.muni.pv243.ws.service;

import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import javax.enterprise.context.ApplicationScoped;
import javax.websocket.Session;

/**
 *
 * @author Michaela Bocanova
 */
@ApplicationScoped
public class SessionStore {
    
    private List<Session> sessions = Collections.synchronizedList(new LinkedList<Session>());
    
    public void addSession(Session session) {
        sessions.add(session);
    }
    
    public void removeSession(Session session) {
        sessions.remove(session);
    }

    public List<Session> getSessions() {
        return Collections.unmodifiableList(sessions);
    }
    
}
