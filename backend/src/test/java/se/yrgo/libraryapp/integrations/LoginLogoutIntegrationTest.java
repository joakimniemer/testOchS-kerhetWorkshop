package se.yrgo.libraryapp.integrations;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpRequest.BodyPublisher;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse;
import java.net.http.HttpResponse.BodyHandlers;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.jooby.JoobyTest;
import se.yrgo.libraryapp.App;
import se.yrgo.libraryapp.entities.forms.LoginData;

@Tag("integration")
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class LoginLogoutIntegrationTest {
    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.29");

    private String goodLoginInput = "{\"username\":\"test\",\"password\":\"yrgoP4ssword\"}";
    private String badLoginInput = "{\"username\":\"bad\",\"password\":\"veryBad\"}";

    private HttpClient client = HttpClient.newHttpClient();

    public static App createApp() {
        System.setProperty("db.url", mySQLContainer.getJdbcUrl());
        System.setProperty("db.user", mySQLContainer.getUsername());
        System.setProperty("db.password", mySQLContainer.getPassword());
        return new App();
    }

    @AfterAll
    static void reset() {
        System.clearProperty("db.url");
        System.clearProperty("db.user");
        System.clearProperty("db.password");
    }

    @Test
    public void alwaysOkToLogout(int serverPort) throws IOException {
        try {
            HttpResponse<String> logoutRespons = logoutRequest(serverPort);
            assertEquals("", logoutRespons.body());
            assertEquals(HttpURLConnection.HTTP_OK, logoutRespons.statusCode());
        } catch (IllegalArgumentException | InterruptedException ex) {
            fail(ex);
        }
    }

    @Test
    public void okToLoginLogout(int serverPort) throws IOException {
        try {
            HttpResponse<String> loginResponse = loginRequest(serverPort, goodLoginInput);
            assertEquals(HttpURLConnection.HTTP_OK, loginResponse.statusCode());

            HttpResponse<String> logoutResponse = logoutRequest(serverPort);
            assertEquals("", logoutResponse.body());
            assertEquals(HttpURLConnection.HTTP_OK, logoutResponse.statusCode());

        } catch (IllegalArgumentException | InterruptedException ex) {
            fail(ex);
        }
    }

    @Test
    public void loginUserDontExist(int serverPort) throws IOException {
        try {
            HttpResponse<String> loginResponse = loginRequest(serverPort, badLoginInput);
            assertTrue(loginResponse.statusCode() >= 400);

        } catch (IllegalArgumentException | InterruptedException ex) {
            fail(ex);
        }
    }

    private HttpResponse logoutRequest(int port) throws IOException, InterruptedException {
        HttpRequest logoutRequest = HttpRequest.newBuilder(URI.create("http://localhost:" +
                port + "/logout")).build();
        return client.send(logoutRequest, BodyHandlers.ofString());
    }

    private HttpResponse loginRequest(int port, String userInput)
            throws IOException, InterruptedException {
        HttpRequest loginRequest = HttpRequest.newBuilder(URI.create("http://localhost:" +
                port + "/login"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(userInput))
                .build();

        return client.send(loginRequest, BodyHandlers.ofString());
    }

}

// Skriv några lämpliga tester för att kontrollera funktionaliteten i
// RegisterUserController, dvs. /register-
// endpointen. Tänkt åter igen på att du gör integrationstester så det är inte
// relevant med 100% heltäckande tester.