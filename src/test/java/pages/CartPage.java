package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utilities.BasePage;

public class CartPage extends BasePage {
    private final By emptyCartLabel = By.id("empty_cart");
    private final By shoppingCartTitle = By.cssSelector("li.active");
    private final By buyProductsLink = By.cssSelector("a[href='/products']");
    private final By itemsList = By.cssSelector("tr[id]");
    private final By removeItemButtons = By.cssSelector("a.cart_quantity_delete");

    @Override
    public void waitPageLoad() {
        waitPage(shoppingCartTitle, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertEquals("Shopping Cart", find(shoppingCartTitle).getText(),
                "The Shopping Cart breadcrumb should identify the current page");
    }

    public void clickBuyProductsLink() {
        find(buyProductsLink).click();
    }

    public void verifyCartEmpty() {
        while (!findAll(removeItemButtons).isEmpty()) {
            final var previousItemCount = findAll(itemsList).size();
            findAll(removeItemButtons).get(0).click();
            getWait().until(ExpectedConditions.numberOfElementsToBeLessThan(itemsList, previousItemCount));
        }

        final var label = getWait().until(ExpectedConditions.visibilityOfElementLocated(emptyCartLabel));
        Assertions.assertTrue(label.isDisplayed(), "The empty cart message should be visible");
    }

    public void verifyTotalCartItems(int expectedTotalItems) {
        Assertions.assertEquals(expectedTotalItems, findAll(itemsList).size(),
                "The cart should contain the expected number of products");
    }
}
