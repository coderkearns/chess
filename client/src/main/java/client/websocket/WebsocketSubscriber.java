package client.websocket;

import websocket.messages.ServerMessage;

public interface WebsocketSubscriber {
    public void onWebsocketError(ServerMessage.Error msg);

    public void onWebsocketNotification(ServerMessage.Notification msg);

    public void onWebsocketLoadGame(ServerMessage.LoadGame msg);
}
