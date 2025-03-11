package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.interactions.Actions;
import utilities.BasePage;

public class Footer extends BasePage {
    private final By subscriptionLabel = By.xpath("//h2[text()='Subscription']");

    @Override
    public void waitPageLoad() {
        waitPage(subscriptionLabel, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
    }

    public void moveToFooter() {
        new Actions(getDriver()).scrollToElement(find(subscriptionLabel)).perform();
    }
}
