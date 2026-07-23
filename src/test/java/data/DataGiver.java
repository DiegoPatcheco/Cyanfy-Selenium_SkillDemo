package data;

import models.Credential;

public class DataGiver {
    private static final String TEST_EMAIL_ENV = "CYANFY_TEST_EMAIL";
    private static final String TEST_PASSWORD_ENV = "CYANFY_TEST_PASSWORD";

    public static Credential getValidCredentials() {
        return new Credential(
                requireEnvironmentVariable(TEST_EMAIL_ENV),
                requireEnvironmentVariable(TEST_PASSWORD_ENV)
        );
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
