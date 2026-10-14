package client;

import java.net.URI;
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
        HttpRequest req = HttpRequest.newBuilder().uri(new URI(this.serverUrl + "/db")).timeout(Duration.ofMillis(5000)).DELETE().build();

        var res = client.send(req, HttpResponse.BodyHandlers.ofString());

        if (res.statusCode() != 200) {
            throw new Exception("clear failed");
        }
    }
}
