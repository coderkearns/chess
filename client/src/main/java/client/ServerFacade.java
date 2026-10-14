package client;

import exceptions.Exceptions;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

public class ServerFacade {
    private final HttpClient client = HttpClient.newHttpClient();
    private final String serverUrl;

    public ServerFacade(String url) {
        serverUrl = url;
    }

    public String register(String username, String password, String email) {
        return "";
    }

    public void clear() throws Exception {
        HttpRequest req = buildRequest("/db").DELETE().build();

        var res = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() != 200) {
            throw new Exceptions.ServerErrorException();
        }
    }

    // ----- Helpers -----
    private HttpRequest.Builder buildRequest(String path) throws URISyntaxException {
        return HttpRequest.newBuilder(new URI(this.serverUrl + path)).timeout(Duration.ofMillis(5000));
    }
}
