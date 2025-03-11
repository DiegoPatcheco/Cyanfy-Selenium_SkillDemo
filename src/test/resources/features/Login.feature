Feature: Login

  Background: Login feature precondition
    Given The user navigates to Login page

  @regression
  Scenario: As a user, I want to verify the login page
    Then The user verifies both sign up & login forms

  @regression
  Scenario: As a user, I want to login into the website
    When The user fills the login form with valid credentials and clicks on continue
    Then The user navigates to home with an open session