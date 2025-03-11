package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.BasePage;

public class TopBar extends BasePage {
    private final By mainTitle = By.cssSelector("img[alt='Website for automation practice']");
    private final By loginButton = By.cssSelector("a[href='/login']");
    private final By logoutButton = By.cssSelector("a[href='/logout']");
    private final By deleteAccountButton = By.cssSelector("a[href='/delete_account']");
    private final By productsButton = By.cssSelector("a[href='/products']");
    private final By cartButton = By.cssSelector("a[href='/view_cart']");
    private final By contactButton = By.cssSelector("a[href='/contact_us']");

    @Override
    public void waitPageLoad() {
        waitPage(mainTitle, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertTrue(find(loginButton).isDisplayed());
    }

    public void clickLoginButton() {
        find(loginButton).click();
    }

    public void verifyOpenedSession() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(logoutButton).isDisplayed()),
                () -> Assertions.assertTrue(find(deleteAccountButton).isDisplayed())
        );
    }

    public void clickCartButton() {
        find(cartButton).click();
    }

    public void clickProductsButton() {
        find(productsButton).click();
    }

    public void clickContactButton() {
        find(contactButton).click();
    }
}
