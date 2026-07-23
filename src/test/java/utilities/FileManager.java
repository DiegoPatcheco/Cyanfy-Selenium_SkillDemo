package utilities;

import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

public class FileManager {
    private static final Path EVIDENCE_PATH = Path.of("target", "evidence");
    private static final Path SCREENSHOT_PATH = EVIDENCE_PATH.resolve("screenshots");
    private static final Path PAGE_SOURCE_PATH = EVIDENCE_PATH.resolve("page-source");
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd-HHmmss-SSS");

    public static void captureFailureEvidence(Scenario scenario) {
        final var driver = new WebDriverProvider().get();
        if (driver == null) {
            Logs.warning("No hay un driver disponible para capturar evidencia de %s", scenario.getName());
            return;
        }

        final var evidenceName = buildEvidenceName(scenario.getName());
        final var screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
        final var pageSource = Jsoup.parse(driver.getPageSource()).toString();

        try {
            Files.createDirectories(SCREENSHOT_PATH);
            Files.createDirectories(PAGE_SOURCE_PATH);
            Files.write(SCREENSHOT_PATH.resolve(evidenceName + ".png"), screenshot);
            Files.writeString(PAGE_SOURCE_PATH.resolve(evidenceName + ".html"), pageSource,
                    StandardCharsets.UTF_8);
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to persist failure evidence", exception);
        }

        scenario.attach(screenshot, "image/png", evidenceName);
        scenario.attach(pageSource, "text/html", evidenceName);
    }

    public static void deletePreviousEvidence() {
        try {
            Logs.debug("Deleting previous evidence");
            FileUtils.deleteDirectory(EVIDENCE_PATH.toFile());
        } catch (IOException exception) {
            Logs.error("Error when deleting previous evidence: %s", exception.getLocalizedMessage());
        }
    }

    private static String buildEvidenceName(String scenarioName) {
        final var safeScenarioName = scenarioName
                .replaceAll("[^a-zA-Z0-9._-]+", "-")
                .replaceAll("(^-+|-+$)", "");
        final var timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        final var uniqueId = UUID.randomUUID().toString().substring(0, 8);
        return String.format("%s-%s-%s", safeScenarioName, timestamp, uniqueId);
    }
}
