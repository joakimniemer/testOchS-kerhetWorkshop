package se.yrgo.pos;

import java.time.Duration;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import se.yrgo.integrations.GeneralStepDefinitions;

public class SearchPage {

    public static boolean formIsPresent() {
        boolean isTitleTextFieldDisplayed = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='Title']")).isDisplayed();
        boolean isAuthorTextFieldDisplayed = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='Author']")).isDisplayed();
        boolean isISBNTextFieldDisplayed = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='ISBN']")).isDisplayed();

        boolean forIsPresent = isTitleTextFieldDisplayed && isAuthorTextFieldDisplayed && isISBNTextFieldDisplayed;

        return forIsPresent;
    }

    public static void searchForISBN(String isbn) {
        WebElement titleTextField = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='ISBN']"));
        titleTextField.sendKeys(isbn);
        WebElement searchButton = GeneralStepDefinitions.getDriver().findElement(By.xpath("//input[@type='submit']"));
        searchButton.click();
        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public static boolean isbnMatchesTitle(String title) {
        List<WebElement> listOfTable = GeneralStepDefinitions.getDriver().findElements(By.tagName("td"));
        for (WebElement webElement : listOfTable) {
            if (webElement.getText().equals(title)) {
                return true;
            }
        }
        return false;
    }

    public static void searchForAuthor(String author) {
        WebElement titleTextField = GeneralStepDefinitions.getDriver()
                .findElement(By.cssSelector("input[placeholder='Author']"));
        titleTextField.sendKeys(author);
        WebElement searchButton = GeneralStepDefinitions.getDriver().findElement(By.xpath("//input[@type='submit']"));
        searchButton.click();
        GeneralStepDefinitions.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
    }

    public static boolean allBooksFromAuthorFound(int numberOfBooksByAuthor, String author) {
        int counter = 0;
        List<WebElement> listOfTable = GeneralStepDefinitions.getDriver().findElements(By.tagName("td"));
        for (WebElement webElement : listOfTable) {
            if (webElement.getText().equals(author)) {
                counter++;
            }
            if (counter == numberOfBooksByAuthor) {
                return true;
            }
        }
        return false;
    }
}
