package pages;

import models.FakeAccount;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.BasePage;

public class ItemDetailsPage extends BasePage {
    private final By itemTitle = By.xpath("//h2[text()='Blue Top']");
    private final By itemDescription = By.xpath("//p[text()='Category: Women > Tops']");
    private final By itemRating = By.cssSelector("img[src='/static/images/product-details/rating.png']");
    private final By itemPrice = By.xpath("//span[text()='Rs. 500']");
    private final By itemQuantityLabel = By.xpath("//label[text()='Quantity:']");
    private final By itemAddCartButton = By.cssSelector("button[class$='cart']");
    private final By itemAvailabilityLabel = By.xpath("//b[text()='Availability:']");
    private final By itemConditionLabel = By.xpath("//b[text()='Condition:']");
    private final By itemBrandLabel = By.xpath("//b[text()='Brand:']");
    private final By writeReviewTitle = By.cssSelector("a[href='#reviews']");
    private final By nameReviewInput = By.id("name");
    private final By emailReviewInput = By.id("email");
    private final By messageReviewInput = By.id("review");
    private final By submitReviewButton = By.id("button-review");
    private final By submitSuccessMessage = By.cssSelector("div[class^='alert-success']");

    @Override
    public void waitPageLoad() {
        waitPage(writeReviewTitle, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(itemTitle).isDisplayed()),
                () -> Assertions.assertTrue(find(itemDescription).isDisplayed()),
                () -> Assertions.assertTrue(find(itemRating).isDisplayed()),
                () -> Assertions.assertTrue(find(itemPrice).isDisplayed()),
                () -> Assertions.assertTrue(find(itemQuantityLabel).isDisplayed()),
                () -> Assertions.assertTrue(find(itemAddCartButton).isDisplayed()),
                () -> Assertions.assertTrue(find(itemAvailabilityLabel).isDisplayed()),
                () -> Assertions.assertTrue(find(itemConditionLabel).isDisplayed()),
                () -> Assertions.assertTrue(find(itemBrandLabel).isDisplayed()),
                () -> Assertions.assertTrue(find(writeReviewTitle).isDisplayed()),
                () -> Assertions.assertTrue(find(nameReviewInput).isDisplayed()),
                () -> Assertions.assertTrue(find(emailReviewInput).isDisplayed()),
                () -> Assertions.assertTrue(find(messageReviewInput).isDisplayed()),
                () -> Assertions.assertTrue(find(submitReviewButton).isDisplayed())
        );
    }

    public void submitItemReview() {
        final var fakeAccount = new FakeAccount();

        find(nameReviewInput).sendKeys(fakeAccount.getName());
        find(emailReviewInput).sendKeys(fakeAccount.getEmail());
        find(messageReviewInput).sendKeys(fakeAccount.getMessage());
        find(submitReviewButton).click();
    }

    public void verifySubmitSuccessMessage() {
        Assertions.assertEquals("Thank you for your review.", find(submitSuccessMessage).getText());
    }
}
