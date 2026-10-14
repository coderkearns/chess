package client;

import com.google.gson.Gson;
import exceptions.Exceptions;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public String register(String username, String password, String email) throws Exceptions.ServerErrorException {
        var res = sendRequest(
                "/user",
                req -> req.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(Map.of(
                        "username", username,
                        "password", password,
                        "email", email
                )))).header("Content-Type", "application/json"),
                HttpResponse.BodyHandlers.ofString()
        );

        if (res.statusCode() != 200) {
            if (res.statusCode() != 403) throw new Exceptions.ServerErrorException();
            throw new Exceptions.AlreadyTakenException();
        }

        var registerResponse = gson.fromJson(res.body(), Map.class);
        String authToken = (String) registerResponse.get("authToken");

        if (authToken == null) throw new Exceptions.ServerErrorException();

        return authToken;
    }

    public String login(String username, String password) throws Exceptions.ServerErrorException {
        var res = sendRequest(
                "/session",
                req -> req.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(Map.of(
                        "username", username,
                        "password", password
                )))).header("Content-Type", "application/json"),
                HttpResponse.BodyHandlers.ofString()
        );

        if (res.statusCode() != 200) {
            if (res.statusCode() != 401) throw new Exceptions.ServerErrorException();
            throw new Exceptions.NotAuthorizedException();
        }

        var registerResponse = gson.fromJson(res.body(), Map.class);
        String authToken = (String) registerResponse.get("authToken");

        if (authToken == null) throw new Exceptions.ServerErrorException();

        return authToken;
    }

    public void clear() throws Exceptions.ServerErrorException {
        var res = sendRequest("/db", req -> req.DELETE(), HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() != 200) throw new Exceptions.ServerErrorException();
    }

    // ----- Helpers -----
    private HttpRequest.Builder buildRequest(String path) throws Exceptions.ServerErrorException {
        try {
            return HttpRequest.newBuilder(new URI(this.serverUrl + path)).timeout(Duration.ofMillis(5000));
        } catch (URISyntaxException e) {
            throw new Exceptions.ServerErrorException("Invalid URL");
        }
    }

    private <T> HttpResponse<T> sendRequest(String path, UnaryOperator<HttpRequest.Builder> modifyRequest, HttpResponse.BodyHandler<T> handler) {
        HttpRequest req = modifyRequest.apply(buildRequest(path)).build();
        try {
            return client.send(req, handler);
        } catch (IOException | InterruptedException e) {
            throw new Exceptions.ServerErrorException();
        }
    }
}
