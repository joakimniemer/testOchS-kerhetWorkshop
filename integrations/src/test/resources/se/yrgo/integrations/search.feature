Feature: Searching for books
  As a user I want to be able to search for available books so I know what I can
  loan.

  Scenario: Getting to the search page
    Given the user is on the start page.
    When the user navigates to the book search.
    Then they can see the search form.

  Scenario: Searching for ISBN
    Given the user is on the start page.
    When the user navigates to the search page and search for an ISBN.
    Then they can see a matching book for that ISBN with right title.

  Scenario: Searching for author
    Given the user is on the start page.
    When the user navigates to the search page and search for an author.
    Then they can see a list of all the books from that author.

    