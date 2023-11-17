package se.yrgo.integrations;

import static org.junit.jupiter.api.Assertions.fail;

import java.net.MalformedURLException;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import se.yrgo.pos.StartPage;

import java.net.URL;

public class GeneralStepDefinitions {
    private static WebDriver driver;

    @Before
    public void setupWebDriver() {
        try {
            ChromeOptions options = new ChromeOptions();
            driver = new RemoteWebDriver(new URL("http://localhost:4444"),
                    options, false);
        } catch (MalformedURLException e) {
            fail(e);
        }
    }

    @After
    public void shutdownWebDriver() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static WebDriver getDriver() {
        return driver;
    }

    @Given("the user is on the start page.")
    public void the_user_is_on_the_start_page() {
        StartPage.goToStartPage();
    }
}