package utilities;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.safari.SafariOptions;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.Locale;

public class DriverManager {
    private static final String DEFAULT_REMOTE_URL = "http://localhost:4444/wd/hub";
    private final WebDriverProvider driverProvider = new WebDriverProvider();

    public void buildDriver() {
        final var executionMode = ExecutionMode.valueOf(
                System.getProperty("executionMode", "LOCAL").toUpperCase(Locale.ROOT)
        );
        final var browser = Browser.valueOf(
                System.getProperty("browser", "CHROME").toUpperCase(Locale.ROOT)
        );
        final var headlessMode = System.getProperty("headless") != null;

        final var driver = switch (executionMode) {
            case LOCAL -> buildLocalDriver(browser, headlessMode);
            case REMOTE -> buildRemoteDriver(browser, headlessMode);
        };

        driverProvider.set(driver);
        try {
            configureDriver(driver, headlessMode, executionMode);
        } catch (RuntimeException exception) {
            killDriver();
            throw exception;
        }
    }

    private RemoteWebDriver buildRemoteDriver(Browser browser, boolean headlessMode) {
        Logs.debug("Inicializando driver remoto: %s", browser);
        final Capabilities capabilities = switch (browser) {
            case CHROME -> {
                final var options = new ChromeOptions();
                if (headlessMode) {
                    configureChromiumHeadless(options);
                }
                yield options;
            }
            case EDGE -> {
                final var options = new EdgeOptions();
                if (headlessMode) {
                    configureChromiumHeadless(options);
                }
                yield options;
            }
            case FIREFOX -> {
                final var options = new FirefoxOptions();
                if (headlessMode) {
                    options.addArguments("--headless", "--width=1920", "--height=1080");
                }
                yield options;
            }
            case SAFARI -> {
                validateSafariHeadless(headlessMode);
                yield new SafariOptions();
            }
        };

        try {
            final URL remoteUrl = new URL(System.getProperty("remoteUrl", DEFAULT_REMOTE_URL));
            return new RemoteWebDriver(remoteUrl, capabilities);
        } catch (MalformedURLException exception) {
            throw new IllegalArgumentException("Invalid Selenium Grid URL", exception);
        }
    }

    private WebDriver buildLocalDriver(Browser browser, boolean headlessMode) {
        Logs.debug("Inicializando el driver local: %s", browser);
        return switch (browser) {
            case CHROME -> {
                final var options = new ChromeOptions();
                if (headlessMode) {
                    configureChromiumHeadless(options);
                }
                yield new ChromeDriver(options);
            }
            case EDGE -> {
                final var options = new EdgeOptions();
                if (headlessMode) {
                    configureChromiumHeadless(options);
                }
                yield new EdgeDriver(options);
            }
            case FIREFOX -> {
                final var options = new FirefoxOptions();
                if (headlessMode) {
                    options.addArguments("--headless", "--width=1920", "--height=1080");
                }
                yield new FirefoxDriver(options);
            }
            case SAFARI -> {
                validateSafariHeadless(headlessMode);
                yield new SafariDriver();
            }
        };
    }

    private void configureDriver(WebDriver driver, boolean headlessMode, ExecutionMode executionMode) {
        if (!headlessMode) {
            Logs.debug("Maximizando la pantalla en modo %s", executionMode);
            driver.manage().window().maximize();
        }

        Logs.debug("Borrando las cookies");
        driver.manage().deleteAllCookies();
    }

    private void configureChromiumHeadless(ChromeOptions options) {
        options.addArguments("--headless=new", "--window-size=1920,1080");
    }

    private void configureChromiumHeadless(EdgeOptions options) {
        options.addArguments("--headless=new", "--window-size=1920,1080");
    }

    private void validateSafariHeadless(boolean headlessMode) {
        if (headlessMode) {
            throw new IllegalArgumentException("Safari does not support this framework's headless mode");
        }
    }

    public void killDriver() {
        final var driver = driverProvider.get();

        try {
            if (driver != null) {
                Logs.debug("Cerrando el driver");
                driver.quit();
            }
        } finally {
            driverProvider.remove();
        }
    }

    private enum Browser {
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI
    }

    private enum ExecutionMode {
        LOCAL,
        REMOTE
    }
}
