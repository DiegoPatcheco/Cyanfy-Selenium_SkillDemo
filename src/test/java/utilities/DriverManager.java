package utilities;

import org.openqa.selenium.Capabilities;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.safari.SafariDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class DriverManager {
    private final boolean runServer = System.getenv("Run Tests and Generate Allure Report") != null;

    public void buildDriver() {
        if (runServer) {
            buildRemoteDriver();
        } else {
            buildLocalDriver();
        }
    }

    private void buildRemoteDriver() {
        final var browserProperty = System.getProperty("browser", "CHROME").toUpperCase();
        final var headlessMode = System.getProperty("headless") != null;

        Logs.debug("Inicializando driver remoto: %s", browserProperty);

        final var capabilities = switch (Browser.valueOf(browserProperty)) {
            case CHROME -> {
                var options = new ChromeOptions();
                if (headlessMode) {
                    options.addArguments("--headless=new");
                }
                yield options;
            }
            case EDGE -> {
                var options = new EdgeOptions();
                if (headlessMode) {
                    options.addArguments("--headless=new");
                }
                yield options;
            }
            case FIREFOX -> {
                var options = new FirefoxOptions();
                if (headlessMode) {
                    options.addArguments("--headless");
                }
                yield options;
            }
            case SAFARI -> new SafariDriver();
        };

        try {
            var remoteUrl = new URL(System.getProperty("remoteUrl", "http://localhost:4444/wd/hub"));
            var driver = new RemoteWebDriver(remoteUrl, (Capabilities) capabilities);

            Logs.debug("Maximizando la pantalla en el entorno remoto");
            driver.manage().window().maximize();

            Logs.debug("Borrando las cookies en el entorno remoto");
            driver.manage().deleteAllCookies();

            Logs.debug("Asignando driver remoto al WebDriver provider");
            new WebDriverProvider().set(driver);

        } catch (MalformedURLException e) {
            Logs.error("URL remota no válida para Selenium Grid", e);
            throw new RuntimeException(e);
        }
    }

    private void buildLocalDriver() {
        final var headlessMode = System.getProperty("headless") != null;
        var browserProperty = System.getProperty("browser");

        if (browserProperty == null) {
            Logs.debug("Setting Chrome as default driver");
            browserProperty = "CHROME";
        }

        final var browser = Browser.valueOf(browserProperty.toUpperCase());

        Logs.debug("Inicializando el driver: %s", browser);
        final var driver = switch (browser) {
            case CHROME -> {
                final var chromeOptions = new ChromeOptions();
                if (headlessMode) {
                    chromeOptions.addArguments("--headless=new");
                }
                yield new ChromeDriver(chromeOptions);
            }
            case EDGE -> {
                final var edgeOptions = new EdgeOptions();
                if (headlessMode) {
                    edgeOptions.addArguments("--headless=new");
                }
                yield new EdgeDriver(edgeOptions);
            }
            case FIREFOX -> {
                final var firefoxOptions = new FirefoxOptions();
                if (headlessMode) {
                    firefoxOptions.addArguments("--headless");
                }
                yield new FirefoxDriver(firefoxOptions);
            }
            case SAFARI -> new SafariDriver();
        };

        Logs.debug("Maximizando la pantalla");
        driver.manage().window().maximize();

        Logs.debug("Borrando las cookies");
        driver.manage().deleteAllCookies();

        Logs.debug("Asignando driver al webdriver provider");
        new WebDriverProvider().set(driver);
    }

    public void killDriver() {
        Logs.debug("Matando el driver");
        new WebDriverProvider().get().quit();
    }

    private enum Browser {
        CHROME,
        FIREFOX,
        EDGE,
        SAFARI
    }
}
