package se.yrgo.integrations;

import io.cucumber.java.en.*;
import io.cucumber.java.lu.an;
import se.yrgo.pos.SearchPage;
import se.yrgo.pos.StartPage;

import static org.junit.Assert.assertThat;

import java.time.Duration;

import javax.management.RuntimeErrorException;

import org.junit.jupiter.api.Assertions.*;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.assertj.core.api.Assertions;

public class SearchStepDefinitions {
    @When("the user navigates to the book search.")
    public void the_user_navigates_to_the_book_search() {
        StartPage.navigateToSearchPage();
    }

    @Then("they can see the search form.")
    public void they_can_see_the_search_form() {
        boolean isFormPresent = SearchPage.formIsPresent();
        Assertions.assertThat(isFormPresent).isTrue();
    }

    @When("the user navigates to the search page and search for an ISBN.")
    public void the_user_navigates_to_the_search_page_and_search_for_an_ISBN() {
        StartPage.navigateToSearchPage();
        if (SearchPage.formIsPresent()) {
            SearchPage.searchForISBN("9789129697285");
            return;
        }
        throw new RuntimeException("Form not found");
    }

    @Then("they can see a matching book for that ISBN with right title.")
    public void they_can_see_a_matching_book_for_that_ISBN_with_right_title() {
        boolean isbnMatchesTitle = SearchPage.isbnMatchesTitle("Här kommer Pippi Långstrump");
        Assertions.assertThat(isbnMatchesTitle).isTrue();
    }

    @When("the user navigates to the search page and search for an author.")
    public void the_user_navigates_to_the_search_page_and_search_for_an_author() {
        StartPage.navigateToSearchPage();
        if (SearchPage.formIsPresent()) {
            SearchPage.searchForAuthor("Dean Koontz");
            return;
        }
        throw new RuntimeException("Form not found");
    }

    @Then("they can see a list of all the books from that author.")
    public void they_can_see_a_list_of_all_the_books_from_that_author() {
        boolean allBooksFromAuthorFound = SearchPage.allBooksFromAuthorFound(2, "Dean Koontz");
        Assertions.assertThat(allBooksFromAuthorFound).isTrue();
    }

}