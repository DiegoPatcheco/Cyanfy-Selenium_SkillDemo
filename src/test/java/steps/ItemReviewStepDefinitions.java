package steps;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import pages.Footer;
import pages.ItemDetailsPage;
import utilities.CommonFlows;

public class ItemReviewStepDefinitions {
    private final CommonFlows commonFlows = new CommonFlows();
    private final ItemDetailsPage itemDetailsPage = new ItemDetailsPage();
    private final Footer footer = new Footer();

    @Given("The user navigates to Item Details page")
    public void userNavigatesItemDetailsPage() {
        commonFlows.goToItemDetailsPage();
    }

    @Then("The user verifies the Item details page")
    public void userVerifiesItemDetailsPage() {
        itemDetailsPage.verifyPage();
    }

    @When("The user fills the item review form and submit it")
    public void userSubmitsItemReviewForm() {
        footer.moveToFooter();
        itemDetailsPage.submitItemReview();
    }

    @Then("The user gets the submit success message")
    public void userVerifiesSubmitSuccessMessage() {
        itemDetailsPage.verifySubmitSuccessMessage();
    }
}
