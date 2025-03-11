Feature: Shopping

  Background: Shopping feature preconditions
    Given The user logs in to home page

  @regression
  Scenario: As a user, I want to ensure my cart is empty
    When The user navigates to Cart page
    And The user verifies his cart is empty
    Then The user clicks on Here link and navigates to products page

  @regression
  Scenario: As a user, I want to add items on my shopping cart
    When The user navigates to Products page
    And The user adds the 3 first items to the shopping cart
    Then The user verifies that the 3 items were added to the cart

  @regression
  Scenario: As a user, I want to verify the products page
    When The user navigates to Products page
    Then The user gets the Products page

  @regression
  Scenario Outline: As a user, I want to verify the items price
    When The user navigates to Products page
    Then The user verifies the items <item> price <price>
    Examples:
      | item                   | price |
      | "Stylish Dress"        | 1500  |
      | "Summer White Top"     | 400   |
      | "Fancy Green Top"      | 700   |
      | "Madame Top For Women" | 1000  |
