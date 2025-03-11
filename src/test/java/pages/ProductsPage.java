package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.locators.RelativeLocator;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.BasePage;

import java.time.Duration;

public class ProductsPage extends BasePage {
    private final By saleImage = By.id("sale_image");
    private final By productSearhBar = By.id("search_product");
    private final By itemsList = By.className("product-image-wrapper");
    private final By categoryLabel = By.xpath("//h2[text()='Category']");
    private final By brandsLabel = By.xpath("//h2[text()='Brands']");
    private final By allProductsLabel = By.xpath("//h2[text()='All Products']");
    private final By modalContinueButton = By.cssSelector("button[class*='close-modal']");
    private final By addCartButtonList = By.cssSelector("div[class^='productinfo']>a");

    @Override
    public void waitPageLoad() {
        waitPage(productSearhBar, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(saleImage).isDisplayed()),
                () -> Assertions.assertTrue(find(productSearhBar).isDisplayed()),
                () -> Assertions.assertTrue(find(itemsList).isDisplayed()),
                () -> Assertions.assertTrue(find(categoryLabel).isDisplayed()),
                () -> Assertions.assertTrue(find(brandsLabel).isDisplayed()),
                () -> Assertions.assertTrue(find(allProductsLabel).isDisplayed())
        );
    }

    public void addingProducts(int totalProducts) {
        new Actions(getDriver()).scrollToElement(findAll(addCartButtonList).get(4)).perform();

        for (var i = 0; i < totalProducts; i++) {
            new Actions(getDriver()).moveToElement(findAll(addCartButtonList).get(i)).perform();

            WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));

            final var addCartButton = wait.until(ExpectedConditions.elementToBeClickable(addCartButtonDynamicLocator(i + 1)));
            addCartButton.click();

            final var continueShoppingModalButton = wait.until(ExpectedConditions.elementToBeClickable(modalContinueButton));
            continueShoppingModalButton.click();
        }
    }

    public void goItemDetails(int itemId) {
        new Actions(getDriver()).scrollToElement(find(viewProductButtonDynamicLocator(4)))
                .moveToElement(find(viewProductButtonDynamicLocator(itemId))).click().perform();
    }

    public void verifyItemsPrice(String itemName, int expectedPrice) {
        final var priceLabel = find(getProductPrice(itemName));
        final var price = Integer.parseInt(priceLabel.getText().replace("Rs. ", ""));
        Assertions.assertEquals(expectedPrice, price);
    }

    private By getProductPrice(String name) {
        return RelativeLocator
                .with(By.cssSelector("h2"))
                .above(itemNameDynamicLocator(name));
    }

    private By itemNameDynamicLocator(String name) {
        return By.xpath(String.format("//p[text()='%s']", name));
    }

    private By viewProductButtonDynamicLocator(int itemId) {
        return By.cssSelector(String.format("a[href='/product_details/%d']", itemId));
    }

    private By addCartButtonDynamicLocator(int product) {
        return By.cssSelector(String.format("div[class='overlay-content']>a[data-product-id='%d']", product));
    }
}
