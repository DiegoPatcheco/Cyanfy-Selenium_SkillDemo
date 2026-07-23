package utilities;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

public abstract class BasePage {
    private static final int DEFAULT_TIMEOUT = 5;
    private final int timeOut;

    public BasePage(int timeOut) {
        this.timeOut = timeOut;
    }

    public BasePage() {
        this(DEFAULT_TIMEOUT);
    }

    protected WebDriver getDriver() {
        return new WebDriverProvider().get();
    }

    protected void waitPage(By locator, String pageName) {
        Logs.debug("Esperando a que cargue la pagina %s", pageName);
        getWait().until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement find(By locator) {
        return getWait().until(ExpectedConditions.presenceOfElementLocated(locator));
    }

    protected List<WebElement> findAll(By locator) {
        return getDriver().findElements(locator);
    }

    public abstract void waitPageLoad();

    public abstract void verifyPage();

    private WebDriverWait getWait() {
        return new WebDriverWait(getDriver(), Duration.ofSeconds(this.timeOut));
    }
}
