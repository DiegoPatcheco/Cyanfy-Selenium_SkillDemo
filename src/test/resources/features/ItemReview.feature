Feature: Item Review

  Background: Item review feature preconditions
    Given The user navigates to Item Details page

  @regression
  Scenario: As a user, I want to verify the item details page
    Then The user verifies the Item details page

  @regression
  Scenario: As a user, I want to submit an item review
    When The user fills the item review form and submit it
    Then The user gets the submit success message