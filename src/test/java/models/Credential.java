package models;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Credential {
    @JsonProperty("email")
    private String email;
    @JsonProperty("password")
    private String password;
    @JsonProperty("message")
    private String message;

    public Credential() {
    }

    public Credential(String email, String password, String message) {
        this.email = email;
        this.password = password;
        this.message = message;
    }

    public String getEmail() {
        return email;
    }

    public String getPassword() {
        return password;
    }

    public String getMessage() {
        return message;
    }
}
