package utilities;

import io.cucumber.java.Scenario;
import org.apache.commons.io.FileUtils;
import org.jsoup.Jsoup;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class FileManager {
    private final static String screenshotPath = "src/test/resources/screenshots";
    private final static String pageStructurePath = "src/test/resources/pageStructure";

    public static void getScreenshot(String screenshotName) {
        Logs.debug("Taking screenshot");

        final var screenshotFile = ((TakesScreenshot) new WebDriverProvider().get())
                .getScreenshotAs(OutputType.FILE);
        final var path = String.format("%s/%s.png", screenshotPath, screenshotName);

        try {
            FileUtils.copyFile(screenshotFile, new File(path));
        } catch (IOException ioException) {
            Logs.error("Error when taking the screenshot %s", ioException.getLocalizedMessage());
        }
    }

    public static void attachScreenshot(Scenario scenario) {
        final var screenShotFile = ((TakesScreenshot) new WebDriverProvider().get())
                .getScreenshotAs(OutputType.BYTES);

        scenario.attach(screenShotFile, "image/png", scenario.getName());
    }

    public static void getPageSource(String fileName) {
        Logs.debug("Extracting page source from web page");
        final var path = String.format("%s/page-source-%s.html", pageStructurePath, fileName);

        try {
            final var file = new File(path);

            Logs.debug("Creating parent directories in case they don't exist");
            if (file.getParentFile().mkdirs()) {
                final var fileWritter = new FileWriter(file);
                final var pageSource = new WebDriverProvider().get().getPageSource();
                fileWritter.write(Jsoup.parse(pageSource).toString());
                fileWritter.close();
            }
        } catch (IOException ioException) {
            Logs.error("Error when getting the page source: %s", ioException.getLocalizedMessage());
        }
    }

    public static void attachPageSource(Scenario scenario) {
        final var pageSource = new WebDriverProvider().get().getPageSource();
        final var parsedPageSource = Jsoup.parse(pageSource).toString();

        scenario.attach(parsedPageSource, "text/plain", scenario.getName());
    }

    public static void deletePreviousEvidence() {
        try {
            Logs.debug("Deleting previous evidence");
            FileUtils.deleteDirectory(new File(screenshotPath));
            FileUtils.deleteDirectory(new File(pageStructurePath));
        } catch (IOException ioException) {
            Logs.error("Error when deleting previous evidence: %s", ioException.getLocalizedMessage());
        }
    }

    /*@Attachment(value = "failureScreenshot", type = "image/png")
    public static byte[] getScreenshot() {
        return ((TakesScreenshot) new WebDriverProvider().get()).getScreenshotAs(OutputType.BYTES);
    }

    @Attachment(value = "pagesource", type = "text/html", fileExtension = "txt")
    public static String getPageSource() {
        return Jsoup.parse(new WebDriverProvider().get().getPageSource()).toString();
    }*/
}
