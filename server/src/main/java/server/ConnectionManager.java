package server;

import com.google.gson.Gson;
import org.eclipse.jetty.websocket.api.Session;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class ConnectionManager {
    public final ConcurrentHashMap<Integer, Set<Session>> games = new ConcurrentHashMap<>();
    public final ConcurrentHashMap<Session, Session> connections = new ConcurrentHashMap<>();

    private Set<Session> getGameSet(int gameID) {
        var selector = Integer.valueOf(gameID);

        if (games.containsKey(selector)) {
            return games.get(selector);
        } else {
            Set<Session> newSessionsSet = ConcurrentHashMap.newKeySet();
            games.put(selector, newSessionsSet);
            return newSessionsSet;
        }
    }

    public void add(int gameID, Session session) {
        getGameSet(gameID).add(session);
    }

    public void remove(int gameID, Session session) {
        getGameSet(gameID).remove(session);
    }

    public void broadcast(int gameID, Session excludeSession, ServerMessage message) throws IOException {
        String msg = new Gson().toJson(message);
        for (Session c : getGameSet(gameID)) {
            if (c.isOpen()) {
                if (!c.equals(excludeSession)) {
                    c.getRemote().sendString(msg);
                }
            }
        }
    }
}