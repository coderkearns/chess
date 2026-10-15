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
import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final Gson gson = new Gson();

    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    private record AuthResponse(String username, String authToken) {
    }

    public String register(String username, String password, String email) throws Exceptions.ServerErrorException {
        var res = sendRequest(
                "/user",
                req -> req.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(Map.of(
                        "username", username,
                        "password", password,
                        "email", email
                )))).header("Content-Type", "application/json")
        );

        validateStatusCode(res);

        var registerResponse = gson.fromJson(res.body(), AuthResponse.class);
        if (registerResponse.authToken() == null) {
            throw new Exceptions.ServerErrorException();
        }
        return registerResponse.authToken();
    }

    public String login(String username, String password) throws Exceptions.ServerErrorException {
        var res = sendRequest(
                "/session",
                req -> req.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(Map.of(
                        "username", username,
                        "password", password
                )))).header("Content-Type", "application/json")
        );

        validateStatusCode(res);

        var loginResponse = gson.fromJson(res.body(), AuthResponse.class);
        if (loginResponse.authToken() == null) {
            throw new Exceptions.ServerErrorException();
        }
        return loginResponse.authToken();
    }

    public void logout(String authToken) throws Exceptions.ServerErrorException {
        var res = sendRequest("/session", req -> req.DELETE().header("Authorization", authToken));

        validateStatusCode(res);
    }

    public static record GameResponse(int gameID, String whiteUsername, String blackUsername, String gameName) {
    }

    private static record ListGamesResponse(List<GameResponse> games) {
    }

    public List<GameResponse> listGames(String authToken) throws Exceptions.ServerErrorException {
        var res = sendRequest("/game", req -> req.GET().header("Authorization", authToken));

        validateStatusCode(res);

        var listGamesResponse = gson.fromJson(res.body(), ListGamesResponse.class);
        return listGamesResponse.games();
    }

    public int createGame(String authToken, String gameName) throws Exceptions.ServerErrorException {
        var res = sendRequest("/game", req -> req.POST(HttpRequest.BodyPublishers.ofString(gson.toJson(Map.of(
                        "gameName", gameName
                )))).headers(
                        "Content-Type", "application/json",
                        "Authorization", authToken
                )
        );

        validateStatusCode(res);

        var gameResponse = gson.fromJson(res.body(), GameResponse.class);
        return gameResponse.gameID();
    }

    public void joinGame(String authToken, String playerColor, int gameID) throws Exceptions.ServerErrorException {
        var res = sendRequest("/game", req -> req.PUT(HttpRequest.BodyPublishers.ofString(gson.toJson(Map.of(
                        "gameID", gameID,
                        "playerColor", playerColor
                )))).headers(
                        "Content-Type", "application/json",
                        "Authorization", authToken
                )
        );

        validateStatusCode(res);
    }

    public void clear() throws Exceptions.ServerErrorException {
        var res = sendRequest("/db", req -> req.DELETE());

        validateStatusCode(res);
    }

    // ----- Helpers -----
    private HttpRequest.Builder buildRequest(String path) throws Exceptions.ServerErrorException {
        try {
            return HttpRequest.newBuilder(new URI(this.serverUrl + path)).timeout(Duration.ofMillis(5000));
        } catch (URISyntaxException e) {
            throw new Exceptions.ServerErrorException("Invalid URL");
        }
    }

    private HttpResponse<String> sendRequest(String path, UnaryOperator<HttpRequest.Builder> modifyRequest) {
        HttpRequest req = modifyRequest.apply(buildRequest(path)).build();
        try {
            return client.send(req, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            throw new Exceptions.ServerErrorException();
        }
    }

    private void validateStatusCode(HttpResponse<String> res) {
        switch (res.statusCode()) {
            case 200:
                return;
            case 400:
                throw new Exceptions.BadRequestException();
            case 401:
                throw new Exceptions.NotAuthorizedException();
            case 403:
                throw new Exceptions.AlreadyTakenException();
            default:
                throw new Exceptions.ServerErrorException();
        }
    }
}
