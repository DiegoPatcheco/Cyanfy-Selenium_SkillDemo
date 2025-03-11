Feature: Contact Us

  Background: Contact Us feature preconditions
    Given The user navigates to Contact page

  @regression
  Scenario: As a user, I want to verify the Contact Us page
    Then The user gets the Contact page

  @regression
  Scenario: As a user, I want to submit a message
    When The user fills the contact form and submit it
    Then The user gets the confirm success message