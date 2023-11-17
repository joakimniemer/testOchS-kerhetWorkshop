package se.yrgo.integrations;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import se.yrgo.pos.HandleLoansPage;
import se.yrgo.pos.LoginPage;
import se.yrgo.pos.StartPage;

public class LendBookStepDefinitions {
    @Given("an admin is logged in")
    public void an_admin_is_logged_in() {
        StartPage.goToStartPage();
        StartPage.navigateToLogin();
        LoginPage.login("test", "yrgoP4ssword");
    }

    @Given("he is on the handle loans page")
    public void is_on_the_handle_loans_page() {
        StartPage.navigateToHandleLoansPage();
    }

    @When("the admin lends a book to the user")
    public void the_admin_lends_a_book_to_the_user() {
        HandleLoansPage.lendOutBook("3", "2");
    }

    @Then("that book should be noted as lent out to the user")
    public void that_book_should_be_noted_as_lent_out_to_the_user() {
    }

    @Given("an regular user is logged in")
    public void an_regular_user_is_logged_in() {
    }

    @When("he navigates to the my book loans page")
    public void he_navigates_to_the_my_book_loans_page() {
    }

    @Then("he sees all the books he has loaned")
    public void he_sees_all_the_books_he_has_loaned() {
    }
}
