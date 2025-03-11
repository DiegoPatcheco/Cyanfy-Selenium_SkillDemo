package steps;

import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.CartPage;
import pages.ProductsPage;
import pages.TopBar;
import utilities.CommonFlows;

public class ShoppingStepDefinitions {
    private final CommonFlows commonFlows = new CommonFlows();
    private final CartPage cartPage = new CartPage();
    private final ProductsPage productsPage = new ProductsPage();
    private final TopBar topBar = new TopBar();

    @Given("The user logs in to home page")
    public void userLogsInHomePage() {
        commonFlows.loginToHomePage();
    }

    @When("The user navigates to Cart page")
    public void userNavigatesCartPage() {
        topBar.clickCartButton();
    }

    @And("The user verifies his cart is empty")
    public void userVerifiesCartEmpty() {
        cartPage.waitPageLoad();
        cartPage.verifyCartEmpty();
    }

    @Then("The user clicks on Here link and navigates to products page")
    public void userClicksHereLink() {
        cartPage.clickBuyProductsLinkTest();
        productsPage.waitPageLoad();
    }

    @When("The user navigates to Products page")
    public void userNavigatesProductsPage() {
        topBar.clickProductsButton();
        productsPage.waitPageLoad();
    }

    @And("The user adds the {int} first items to the shopping cart")
    public void userAddsItemsToShoppingCart(int products) {
        productsPage.addingProducts(products);
    }

    @Then("The user verifies that the {int} items were added to the cart")
    public void userVerifiesTotalItemsAddedToCart(int products) {
        topBar.clickCartButton();
        cartPage.waitPageLoad();
        cartPage.verifyTotalCartItems(products);
    }

    @Then("The user gets the Products page")
    public void userGetsProductsPage() {
        productsPage.waitPageLoad();
        productsPage.verifyPage();
    }

    @Then("The user verifies the items {string} price {int}")
    public void userVerifiesItemsPrice(String itemName, int expectedPrice) {
        productsPage.waitPageLoad();
        productsPage.verifyItemsPrice(itemName, expectedPrice);
    }
}
