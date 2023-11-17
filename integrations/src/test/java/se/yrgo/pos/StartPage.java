package se.yrgo.pos;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import se.yrgo.integrations.GeneralStepDefinitions;

public class StartPage {

    public static void navigateToSearchPage() {
        WebElement findBookButton = GeneralStepDefinitions.getDriver().findElement(By.linkText("FIND A BOOK"));
        findBookButton.click();
        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public static void goToStartPage() {
        GeneralStepDefinitions.getDriver().get("http://frontend");
        if (!"The Library".equals(GeneralStepDefinitions.getDriver().getTitle())) {
            throw new IllegalStateException("Not on the start page");
        }
        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public static void navigateToLogin() {
        WebElement loginButton = GeneralStepDefinitions.getDriver().findElement(By.linkText("LOGIN"));
        loginButton.click();
        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public static void navigateToHandleLoansPage() {
        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
        WebElement administrationButton = GeneralStepDefinitions.getDriver()
                .findElement(By.linkText("Administration"));
        administrationButton.click();
        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        WebElement handleLoansButton = GeneralStepDefinitions.getDriver().findElement(By.linkText("Handle loans"));
        handleLoansButton.click();
        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

}
