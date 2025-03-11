package pages;

import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import utilities.BasePage;
import utilities.Logs;

public class LoginPage extends BasePage {
    private final By loginForm = By.className("login-form");
    private final By signUpForm = By.className("signup-form");
    private final By userNameSignUpInput = By.cssSelector("input[data-qa='signup-name']");
    private final By emailSignUpInput = By.cssSelector("input[data-qa='signup-email']");
    private final By signUpButton = By.cssSelector("button[data-qa='signup-button']");
    private final By emailLoginInput = By.cssSelector("input[data-qa='login-email']");
    private final By passwordLoginInput = By.cssSelector("input[data-qa='login-password']");
    private final By loginButton = By.cssSelector("button[data-qa='login-button']");

    @Override
    public void waitPageLoad() {
        waitPage(loginForm, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(loginForm).isDisplayed()),
                () -> Assertions.assertTrue(find(signUpForm).isDisplayed())
        );
    }

    public void fillSignUpForm(String username, String email) {
        Logs.info("Enter the username");
        find(userNameSignUpInput).sendKeys(username);

        Logs.info("Enter the email address");
        find(emailSignUpInput).sendKeys(email);

        Logs.info("Click on Sign up button");
        find(signUpButton).click();
    }

    public void fillLoginForm(String email, String password) {
        Logs.info("Enter the user email");
        find(emailLoginInput).sendKeys(email);

        Logs.info("Enter the user password");
        find(passwordLoginInput).sendKeys(password);

        Logs.info("Click on login button");
        find(loginButton).click();
    }
}
