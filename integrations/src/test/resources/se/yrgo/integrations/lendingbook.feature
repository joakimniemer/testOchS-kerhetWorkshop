Feature: Lending of books
  As admin i want to be able to lend out books to users, and users
  want to be able to keep track of the books they lend.

  Scenario: Lending out book 
    Given an admin is logged in
    And he is on the handle loans page
    When the admin lends a book to the user
    Then that book should be noted as lent out to the user

  Scenario: Check my book loans 
    Given an regular user is logged in
    When he navigates to the my book loans page
    Then he sees all the books he has loaned