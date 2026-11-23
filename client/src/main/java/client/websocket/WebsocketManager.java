package client.websocket;

import com.google.gson.Gson;
import exceptions.Exceptions;
import jakarta.websocket.*;
import websocket.commands.UserGameCommand;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketManager extends Endpoint implements MessageHandler.Whole<String> {
    Session session;
    WebsocketSubscriber subscriber;
    Gson gson = new Gson();

    public WebsocketManager(String serverUrl, WebsocketSubscriber subscriber) throws Exceptions.ServerErrorException {
        this.subscriber = subscriber;

        try {
            URI socketURI = new URI(serverUrl.replace("http", "ws") + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(this);

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exceptions.ServerErrorException("couldn't connect to server websocket");
        }
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    @Override
    public void onMessage(String message) {
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        switch (serverMessage.getServerMessageType()) {
            case ERROR -> this.subscriber.onWebsocketError(gson.fromJson(message, ServerMessage.Error.class));
            case NOTIFICATION ->
                    this.subscriber.onWebsocketNotification(gson.fromJson(message, ServerMessage.Notification.class));
            case LOAD_GAME -> this.subscriber.onWebsocketLoadGame(gson.fromJson(message, ServerMessage.LoadGame.class));
        }
    }

    public void sendCommand(UserGameCommand command) {
        try {
            var message = gson.toJson(command);
            this.session.getBasicRemote().sendText(message);
        } catch (IOException e) {
            throw new Exceptions.ServerErrorException("error communicating with server");
        }
    }
}
