package pages;

import models.FakeAccount;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.Alert;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import utilities.BasePage;

import java.io.File;
import java.time.Duration;

public class ContactPage extends BasePage {
    private final By contactUsTitle = By.xpath("//h2[text()='Contact ']");
    private final By nameInput = By.cssSelector("input[data-qa='name']");
    private final By emailInput = By.cssSelector("input[data-qa='email']");
    private final By subjectInput = By.cssSelector("input[data-qa='subject']");
    private final By messageInput = By.cssSelector("textarea[data-qa='message']");
    private final By uploadFileButton = By.cssSelector("input[type='file']");
    private final By submitButton = By.cssSelector("input[data-qa='submit-button']");
    private final By submitSuccessMessage = By.cssSelector("div[class$='alert-success']");

    @Override
    public void waitPageLoad() {
        waitPage(contactUsTitle, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(contactUsTitle).isDisplayed()),
                () -> Assertions.assertTrue(find(nameInput).isDisplayed()),
                () -> Assertions.assertTrue(find(emailInput).isDisplayed()),
                () -> Assertions.assertTrue(find(subjectInput).isDisplayed()),
                () -> Assertions.assertTrue(find(messageInput).isDisplayed()),
                () -> Assertions.assertTrue(find(uploadFileButton).isDisplayed()),
                () -> Assertions.assertTrue(find(submitButton).isDisplayed())
        );
    }

    public void fillContactForm() {
        final var fakeAccount = new FakeAccount();
        final var img = new File("src/test/resources/samples/pruebaFoto.png");

        find(nameInput).sendKeys(fakeAccount.getName());
        find(emailInput).sendKeys(fakeAccount.getEmail());
        find(subjectInput).sendKeys(fakeAccount.getSubject());
        find(messageInput).sendKeys(fakeAccount.getMessage());
        find(uploadFileButton).sendKeys(img.getAbsolutePath());
        find(submitButton).click();
    }

    public void confirmSuccessMessage() {
        WebDriverWait wait = new WebDriverWait(getDriver(), Duration.ofSeconds(5));

        final var confirm = (Alert) wait.until(ExpectedConditions.alertIsPresent());
        confirm.accept();

        Assertions.assertEquals("Success! Your details have been submitted successfully.",
                find(submitSuccessMessage).getText());
    }
}
