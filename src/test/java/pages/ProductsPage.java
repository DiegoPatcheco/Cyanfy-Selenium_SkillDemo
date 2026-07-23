package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import utilities.BasePage;

public class ProductsPage extends BasePage {
    private final By saleImage = By.id("sale_image");
    private final By productSearhBar = By.id("search_product");
    private final By productCards = By.className("product-image-wrapper");
    private final By productName = By.cssSelector(".productinfo p");
    private final By productPrice = By.cssSelector(".productinfo h2");
    private final By overlayAddCartButton = By.cssSelector(".overlay-content a.add-to-cart");
    private final By categoryLabel = By.xpath("//h2[text()='Category']");
    private final By brandsLabel = By.xpath("//h2[text()='Brands']");
    private final By allProductsLabel = By.xpath("//h2[text()='All Products']");
    private final By modalContinueButton = By.cssSelector("button[class*='close-modal']");

    @Override
    public void waitPageLoad() {
        waitPage(productSearhBar, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(saleImage).isDisplayed()),
                () -> Assertions.assertTrue(find(productSearhBar).isDisplayed()),
                () -> Assertions.assertFalse(findAll(productCards).isEmpty(), "Products should be listed"),
                () -> Assertions.assertTrue(find(categoryLabel).isDisplayed()),
                () -> Assertions.assertTrue(find(brandsLabel).isDisplayed()),
                () -> Assertions.assertTrue(find(allProductsLabel).isDisplayed())
        );
    }

    public void addingProducts(int totalProducts) {
        final var availableProducts = findAll(productCards).size();
        if (totalProducts <= 0 || totalProducts > availableProducts) {
            throw new IllegalArgumentException(String.format(
                    "Requested %d products, but %d are available", totalProducts, availableProducts
            ));
        }

        for (var i = 0; i < totalProducts; i++) {
            final var productCard = findAll(productCards).get(i);
            new Actions(getDriver()).scrollToElement(productCard).moveToElement(productCard).perform();
            final var addCartButton = getWait().until(
                    ExpectedConditions.elementToBeClickable(productCard.findElement(overlayAddCartButton))
            );
            addCartButton.click();

            final var continueShoppingModalButton = getWait().until(
                    ExpectedConditions.elementToBeClickable(modalContinueButton)
            );
            continueShoppingModalButton.click();
        }
    }

    public void goItemDetails(int itemId) {
        final var viewProductButton = find(viewProductButtonDynamicLocator(itemId));
        new Actions(getDriver()).scrollToElement(viewProductButton).moveToElement(viewProductButton).click().perform();
    }

    public void verifyItemsPrice(String itemName, int expectedPrice) {
        final var productCard = findAll(productCards).stream()
                .filter(card -> card.findElement(productName).getText().equals(itemName))
                .findFirst()
                .orElseThrow(() -> new AssertionError("Product not found: " + itemName));
        final var priceLabel = productCard.findElement(productPrice);
        final var price = Integer.parseInt(priceLabel.getText().replace("Rs. ", ""));
        Assertions.assertEquals(expectedPrice, price, "The product price should match the catalog expectation");
    }

    private By viewProductButtonDynamicLocator(int itemId) {
        return By.cssSelector(String.format("a[href='/product_details/%d']", itemId));
    }
}
