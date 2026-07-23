package models;

import com.github.javafaker.Faker;

public class FakeAccount {
    private final String username;
    private final String email;
    private final String password;
    private final String name;
    private final String lastName;
    private final String company;
    private final String address;
    private final String altAddress;
    private final String state;
    private final String city;
    private final String zipcode;
    private final String phoneNumber;
    private final String subject;
    private final String message;

    public FakeAccount() {
        final var faker = new Faker();
        phoneNumber = faker.phoneNumber().cellPhone();
        zipcode = faker.address().zipCode();
        city = faker.address().city();
        state = faker.address().state();
        altAddress = faker.address().secondaryAddress();
        address = faker.address().streetAddress();
        company = faker.company().name();
        lastName = faker.name().lastName();
        name = faker.name().firstName();
        password = faker.internet().password();
        email = faker.internet().emailAddress();
        username = faker.name().username();
        subject = faker.book().title();
        message = faker.book().genre() + faker.book().author() + faker.book().publisher();
    }

    public String getUsername() {
        return username;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getName() {
        return name;
    }

    public String getLastName() {
        return lastName;
    }

    public String getCompany() {
        return company;
    }

    public String getAddress() {
        return address;
    }

    public String getAltAddress() {
        return altAddress;
    }

    public String getState() {
        return state;
    }

    public String getCity() {
        return city;
    }

    public String getZipcode() {
        return zipcode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public String getSubject() {
        return subject;
    }

    public String getMessage() {
        return message;
    }
}
