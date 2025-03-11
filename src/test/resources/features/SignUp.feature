Feature: Sign Up

  Background: Sign up feature precondition
    Given The user navigates to Account Information page

  @regression
  Scenario: As a new user, I want to sign up on the website
    When The user fills the account information form and clicks on continue
    Then The user gets the sign up success page