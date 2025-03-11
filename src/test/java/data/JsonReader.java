package data;

import com.fasterxml.jackson.databind.ObjectMapper;
import models.CredentialType;

import java.io.File;
import java.io.IOException;

public class JsonReader {
    private static final String jsonPath = "src/test/resources/data/credenciales.json";

    public static CredentialType getCredentialsMap() {
        final var objectMapper = new ObjectMapper();

        try {
            return objectMapper.readValue(new File(jsonPath), CredentialType.class);
        } catch (IOException ioException) {
            throw new RuntimeException(ioException.getLocalizedMessage());
        }
    }
}
