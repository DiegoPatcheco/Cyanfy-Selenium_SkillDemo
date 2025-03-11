package steps;

import data.DataGiver;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.HomePage;
import pages.LoginPage;
import pages.TopBar;
import utilities.CommonFlows;

public class LoginStepDefinitions {
    private final CommonFlows commonFlows = new CommonFlows();
    private final LoginPage loginPage = new LoginPage();
    private final HomePage homePage = new HomePage();
    private final TopBar topBar = new TopBar();

    @Given("The user navigates to Login page")
    public void userNavigatesLoginPage() {
        commonFlows.goToLoginPage();
    }

    @Then("The user verifies both sign up & login forms")
    public void userVerifiesForms() {
        loginPage.verifyPage();
    }

    @When("The user fills the login form with valid credentials and clicks on continue")
    public void userFillsLoginFormWithValidCredentials() {
        final var validCredentials = DataGiver.getValidCredentials();
        loginPage.fillLoginForm(validCredentials.getEmail(), validCredentials.getPassword());
    }

    @Then("The user navigates to home with an open session")
    public void userNavigatesHomeWithOpenSession() {
        homePage.waitPageLoad();
        topBar.verifyOpenedSession();
    }
}
