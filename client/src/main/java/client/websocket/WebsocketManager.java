package client.websocket;

import com.google.gson.Gson;
import exceptions.Exceptions;
import jakarta.websocket.*;
import websocket.messages.ServerMessage;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class WebsocketManager extends Endpoint implements MessageHandler.Whole<String> {
    Session session;
    WebsocketSubscriber subscriber;
    Gson gson = new Gson();

    public WebsocketManager(String serverUrl) throws Exceptions.ServerErrorException {
        try {
            URI socketURI = new URI(serverUrl.replace("http", "ws") + "/ws");
            WebSocketContainer container = ContainerProvider.getWebSocketContainer();
            session = container.connectToServer(this, socketURI);
            this.session.addMessageHandler(this);

        } catch (DeploymentException | IOException | URISyntaxException e) {
            throw new Exceptions.ServerErrorException("couldn't connect to server websocket");
        }
    }

    public void setSubscriber(WebsocketSubscriber subscriber) {
        this.subscriber = subscriber;
    }

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
    }

    @Override
    public void onMessage(String message) {
        ServerMessage serverMessage = gson.fromJson(message, ServerMessage.class);
        if (this.subscriber != null) {
            this.subscriber.onWebsocketMessage(serverMessage);
        }
    }


}
