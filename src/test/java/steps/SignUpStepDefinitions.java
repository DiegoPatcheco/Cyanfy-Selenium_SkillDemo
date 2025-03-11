package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.AccountInfoPage;
import pages.SignUpSuccessPage;
import utilities.CommonFlows;

public class SignUpStepDefinitions {
    private final CommonFlows commonFlows = new CommonFlows();
    private final AccountInfoPage accountInfoPage = new AccountInfoPage();
    private final SignUpSuccessPage signUpSuccessPage = new SignUpSuccessPage();

    @Given("The user navigates to Account Information page")
    public void userNavigatesAccountInfoPage() {
        commonFlows.goToAccountInfoPage();
    }

    @When("The user fills the account information form and clicks on continue")
    public void userFillsAccountInfoForm() {
        accountInfoPage.fillAccountInfoForm(commonFlows.getUsername(), commonFlows.getEmail());
    }

    @Then("The user gets the sign up success page")
    public void userGetsSignUpSuccessPage() {
        signUpSuccessPage.waitPageLoad();
        signUpSuccessPage.verifyPage();
    }
}
