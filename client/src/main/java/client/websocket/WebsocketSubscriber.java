package client.websocket;

import websocket.messages.ServerMessage;

public interface WebsocketSubscriber {
    public void onWebsocketMessage(ServerMessage msg);
}
