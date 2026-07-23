package pages;

import com.github.javafaker.Faker;
import models.FakeAccount;
import org.junit.jupiter.api.Assertions;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.Select;
import utilities.BasePage;
import utilities.Logs;

import java.time.LocalDate;

public class AccountInfoPage extends BasePage {
    private final By accountInfoTitle = By.xpath("//b[text()='Enter Account Information']");
    private final By maleGenderRadioButton = By.id("id_gender1");
    private final By femaleGenderRadioButton = By.id("id_gender2");
    private final By userNameInput = By.id("name");
    private final By emailInput = By.id("email");
    private final By passwordInput = By.id("password");
    private final By birthDayDropdown = By.id("days");
    private final By birthMonthDropdown = By.id("months");
    private final By birthYearDropdown = By.id("years");
    private final By newsLetterCheckbox = By.id("newsletter");
    private final By partnersOffersCheckbox = By.id("optin");
    private final By firstNameInput = By.id("first_name");
    private final By lastNameInput = By.id("last_name");
    private final By companyInput = By.id("company");
    private final By mainAddressInput = By.id("address1");
    private final By secondaryAddressInput = By.id("address2");
    private final By countryDropdown = By.id("country");
    private final By stateInput = By.id("state");
    private final By cityInput = By.id("city");
    private final By zipcodeInput = By.id("zipcode");
    private final By phoneNumberInput = By.id("mobile_number");
    private final By createAccountButton = By.cssSelector("button[data-qa='create-account']");

    @Override
    public void waitPageLoad() {
        waitPage(accountInfoTitle, this.getClass().getSimpleName());
    }

    @Override
    public void verifyPage() {
        Assertions.assertAll(
                () -> Assertions.assertTrue(find(accountInfoTitle).isDisplayed()),
                () -> Assertions.assertTrue(find(createAccountButton).isEnabled()),
                () -> Assertions.assertTrue(find(createAccountButton).isDisplayed())
        );
    }

    public void fillAccountInfoForm(String username, String email) {
        final var fakeAccount = new FakeAccount();

        Logs.info("Select gender");
        find(maleGenderRadioButton).click();
        find(femaleGenderRadioButton).click();
        Assertions.assertTrue(find(femaleGenderRadioButton).isSelected());

        Logs.info("Verify username & email pre-registered values");
        Assertions.assertAll(
                () -> Assertions.assertEquals(username, find(userNameInput).getDomProperty("value")),
                () -> Assertions.assertEquals(email, find(emailInput).getDomProperty("value"))
        );

        Logs.info("Enter password");
        find(passwordInput).sendKeys(fakeAccount.getPassword());

        Logs.info("Select date of birth");
        final var daySelect = new Select(find(birthDayDropdown));
        final var monthSelect = new Select(find(birthMonthDropdown));
        final var yearSelect = new Select(find(birthYearDropdown));
        final var dateOfBirth = getRandomDateOfBirth();
        daySelect.selectByValue(String.valueOf(dateOfBirth.getDayOfMonth()));
        monthSelect.selectByValue(String.valueOf(dateOfBirth.getMonthValue()));
        yearSelect.selectByValue(String.valueOf(dateOfBirth.getYear()));

        Logs.info("Checkmark the checkboxes");
        find(newsLetterCheckbox).click();
        find(partnersOffersCheckbox).click();

        Logs.info("Enter first name, last name, company, main address & secondary address");
        find(firstNameInput).sendKeys(fakeAccount.getName());
        find(lastNameInput).sendKeys(fakeAccount.getLastName());
        find(companyInput).sendKeys(fakeAccount.getCompany());
        find(mainAddressInput).sendKeys(fakeAccount.getAddress());
        find(secondaryAddressInput).sendKeys(fakeAccount.getAltAddress());

        Logs.info("Select country");
        final var countrySelect = new Select(find(countryDropdown));
        countrySelect.selectByValue(getRandomCountryValue());

        Logs.info("Enter state, city, zipcode & phone number");
        find(stateInput).sendKeys(fakeAccount.getState());
        find(cityInput).sendKeys(fakeAccount.getCity());
        find(zipcodeInput).sendKeys(fakeAccount.getZipcode());
        find(phoneNumberInput).sendKeys(fakeAccount.getPhoneNumber());

        Logs.info("Click on create account button");
        find(createAccountButton).click();
    }

    private static LocalDate getRandomDateOfBirth() {
        final var faker = new Faker();
        final var year = faker.number().numberBetween(1900, 2022);
        final var month = faker.number().numberBetween(1, 13);
        final var firstDayOfMonth = LocalDate.of(year, month, 1);
        final var day = faker.number().numberBetween(1, firstDayOfMonth.lengthOfMonth() + 1);
        return firstDayOfMonth.withDayOfMonth(day);
    }

    private static String getRandomCountryValue() {
        final var faker = new Faker();
        final var number = faker.number().numberBetween(1, 8);

        return switch (number) {
            case 1 -> "India";
            case 2 -> "United States";
            case 3 -> "Canada";
            case 4 -> "Australia";
            case 5 -> "Israel";
            case 6 -> "New Zealand";
            case 7 -> "Singapore";
            default -> throw new IllegalStateException("Unexpected value: " + number);
        };
    }
}
