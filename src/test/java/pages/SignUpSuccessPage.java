package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.BasePage;

public class SignUpSuccessPage extends BasePage {
    private final By creationSuccessTitle = By.xpath("//b[text()='Account Created!']");
    private final By continueButton = By.cssSelector("a[data-qa='continue-button']");

    @Override
    public void waitPageLoad() {
        waitPage(creationSuccessTitle, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(creationSuccessTitle).isDisplayed()),
                () -> Assertions.assertTrue(find(continueButton).isEnabled()),
                () -> Assertions.assertTrue(find(continueButton).isDisplayed())
        );
    }

    public void clickContinue() {
        find(continueButton).click();
    }
}
