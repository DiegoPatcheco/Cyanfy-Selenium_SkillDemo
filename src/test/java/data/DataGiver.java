package data;

import models.Credential;

import java.util.Map;

public class DataGiver {
    private static final String TEST_EMAIL_ENV = "CYANFY_TEST_EMAIL";
    private static final String TEST_PASSWORD_ENV = "CYANFY_TEST_PASSWORD";

    private static Map<String, Credential> getCredentialMap() {
        return JsonReader.getCredentialsMap().getMapCredentials();
    }

    public static Credential getValidCredentials() {
        return new Credential(
                requireEnvironmentVariable(TEST_EMAIL_ENV),
                requireEnvironmentVariable(TEST_PASSWORD_ENV),
                ""
        );
    }

    public static Credential getLockedCredentials() {
        return getCredentialMap().get("locked");
    }

    public static Credential getUnexistentCredentials() {
        return getCredentialMap().get("unexistent");
    }

    private static String requireEnvironmentVariable(String variableName) {
        final var value = System.getenv(variableName);

        if (value == null || value.isBlank()) {
            throw new IllegalStateException(
                    String.format("Required environment variable %s is not configured", variableName)
            );
        }

        return value;
    }
}
