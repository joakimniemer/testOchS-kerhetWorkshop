package se.yrgo.libraryapp.integrations;

import static org.junit.Assert.assertFalse;
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
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

import io.jooby.JoobyTest;
import se.yrgo.libraryapp.App;
import se.yrgo.libraryapp.entities.forms.RegisterUserData;

@Tag("integration")
@Testcontainers
@JoobyTest(value = App.class, factoryMethod = "createApp")
public class RegisterIntegrationTest {

    @Container
    private static MySQLContainer<?> mySQLContainer = new MySQLContainer<>("mysql:8.0.29");

    private HttpClient client = HttpClient.newHttpClient();

    private String registerUserData1 = "{\"name\": \"laserflaser\", \"realName\": \"joakim\", \"password\": \"hej123\"}";
    private String registerUserData2 = "{\"name\": \"bulle32\", \"realName\": \"joakim\", \"password\": \"hej123\"}";
    private String registerUserData3 = "{\"name\": \"oi\", \"realName\": \"joakim\", \"password\": \"hej123\"}";

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
    @Order(1)
    public void registerSucceded(int serverPort) {
        try {
            HttpResponse<String> registerResponse = registerRequest(serverPort,
                    registerUserData1);

            assertTrue(Boolean.valueOf(registerResponse.body()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(2)
    public void usernameAlreadyExits(int serverPort) {
        try {
            HttpResponse<String> firstRegisterResponse = registerRequest(serverPort,
                    registerUserData2);
            HttpResponse<String> secondRegisterResponse = registerRequest(serverPort,
                    registerUserData2);

            assertFalse(Boolean.valueOf(secondRegisterResponse.body()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    @Order(3)
    public void usernameToShort(int serverPort) {
        try {
            HttpResponse<String> registerResponse = registerRequest(serverPort,
                    registerUserData3);

            assertFalse(Boolean.valueOf(registerResponse.body()));

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private HttpResponse<String> registerRequest(int port, String userData)
            throws IOException, InterruptedException {
        HttpRequest registerRequest = HttpRequest.newBuilder(URI.create("http://localhost:" +
                port + "/register"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(userData))
                .build();

        return client.send(registerRequest, BodyHandlers.ofString());
    }
}
