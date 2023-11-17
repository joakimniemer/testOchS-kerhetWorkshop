package se.yrgo.pos;

import java.time.Duration;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import se.yrgo.integrations.GeneralStepDefinitions;

public class HandleLoansPage {

    public static void lendOutBook(String bookID, String userID) {
        WebElement bookIdTextField = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='Book ID']"));
        bookIdTextField.sendKeys(bookID);
        WebElement userIdTextField = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='User ID']"));
        userIdTextField.sendKeys(userID);

        // TODO: Fortsätt här

        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

}