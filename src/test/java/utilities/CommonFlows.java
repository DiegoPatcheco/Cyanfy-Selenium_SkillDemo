package utilities;

import data.DataGiver;
import models.FakeAccount;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import pages.AccountInfoPage;
import pages.ContactPage;
import pages.HomePage;
import pages.LoginPage;
import pages.ProductsPage;
import pages.TopBar;

public class CommonFlows {
    private final FakeAccount fakeAccount = new FakeAccount();

    private WebDriver getDriver() {
        return new WebDriverProvider().get();
    }

    private void assignLoginCookie() {
        Logs.debug("Setting login cookie");
        getDriver().get("https://www.automationexercise.com/404");
        final var validCredentials = DataGiver.getValidCredentials();
        final var loginCookie = new Cookie("sessionid", validCredentials.getEmail());
        getDriver().manage().addCookie(loginCookie);
    }

    public String getUsername() {
        return fakeAccount.getUsername();
    }

    public String getEmail() {
        return fakeAccount.getEmail();
    }

    public void goToHomePage() {
        getDriver().get("https://www.automationexercise.com/");
        new HomePage().waitPageLoad();
    }

    public void goToLoginPage() {
        goToHomePage();
        new TopBar().waitPageLoad();
        new TopBar().clickLoginButton();
        new LoginPage().waitPageLoad();
    }

    public void goToAccountInfoPage() {
        goToLoginPage();
        new LoginPage().fillSignUpForm(getUsername(), getEmail());
        new AccountInfoPage().waitPageLoad();
        new AccountInfoPage().verifyPage();
    }

    public void loginToHomePage() {
        goToLoginPage();
        final var validCredentials = DataGiver.getValidCredentials();

        new LoginPage().fillLoginForm(validCredentials.getEmail(), validCredentials.getPassword());
        new HomePage().waitPageLoad();
    }

    public void goToContactPage() {
        goToLoginPage();
        final var validCredentials = DataGiver.getValidCredentials();

        new LoginPage().fillLoginForm(validCredentials.getEmail(), validCredentials.getPassword());
        new HomePage().waitPageLoad();
        new TopBar().clickContactButton();
        new ContactPage().waitPageLoad();
    }

    public void goToItemDetailsPage() {
        goToLoginPage();
        final var validCredentials = DataGiver.getValidCredentials();

        new LoginPage().fillLoginForm(validCredentials.getEmail(), validCredentials.getPassword());
        new HomePage().waitPageLoad();
        new TopBar().clickProductsButton();
        new ProductsPage().goItemDetails(1);
    }
}
