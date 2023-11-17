package se.yrgo.pos;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import se.yrgo.integrations.GeneralStepDefinitions;

public class LoginPage {

    public static void login(String username, String password) {
        WebElement usernameTextField = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='Username']"));
        usernameTextField.sendKeys(username);

        WebElement passwordTextField = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='Password']"));
        passwordTextField.sendKeys(password);

        WebElement loginSubmitButton = GeneralStepDefinitions.getDriver()
                .findElement(By.xpath("//input[@type='submit']"));
        loginSubmitButton.click();
    }
}
