package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.ContactPage;
import utilities.CommonFlows;

public class ContactStepDefinitions {
    private final CommonFlows commonFlows = new CommonFlows();
    private final ContactPage contactPage = new ContactPage();

    @Given("The user navigates to Contact page")
    public void userNavigatesContactPage() {
        commonFlows.goToContactPage();
    }

    @Then("The user gets the Contact page")
    public void userVerifiesContactPage() {
        contactPage.verifyPage();
    }

    @When("The user fills the contact form and submit it")
    public void userSubmitContactForm() {
        contactPage.fillContactForm();
    }

    @Then("The user gets the confirm success message")
    public void userGetsConfirmSuccessMessage() {
        contactPage.confirmSuccessMessage();
    }
}
