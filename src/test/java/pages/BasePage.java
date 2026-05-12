package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class BasePage {

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(25));
    }

    protected WebElement waitForVisibility(By locator) {
        return wait.until(ExpectedConditions.visibilityOfElementLocated(locator));
    }

    protected WebElement waitForClickability(By locator) {
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }

    protected void waitForTextInBody(String expectedText) {

        By body = By.tagName("body");

        wait.until(driver ->
                driver.findElement(body)
                        .getText()
                        .contains(expectedText)
        );
    }

    protected void waitForAnyTextInBody(String... expectedTexts) {

        By body = By.tagName("body");

        wait.until(driver -> {

            String bodyText =
                    driver.findElement(body)
                            .getText();

            for (String text : expectedTexts) {

                if (bodyText.contains(text)) {
                    return true;
                }
            }

            return false;
        });
    }

    protected void waitForPageLoad() {

        wait.until(driver ->
                ((JavascriptExecutor) driver)
                        .executeScript("return document.readyState")
                        .equals("complete"));
    }

    protected void scrollToElement(WebElement element) {

        ((JavascriptExecutor) driver)
                .executeScript(
                        "arguments[0].scrollIntoView({block: 'center'});",
                        element
                );
    }

    protected void click(By locator) {

        WebElement element =
                waitForClickability(locator);

        scrollToElement(element);

        element.click();
    }

    protected void type(By locator, String text) {

        WebElement element =
                waitForVisibility(locator);

        scrollToElement(element);

        element.clear();

        element.sendKeys(text);
    }

    protected String getText(By locator) {

        return waitForVisibility(locator)
                .getText();
    }

    protected boolean isDisplayed(By locator) {

        return waitForVisibility(locator)
                .isDisplayed();
    }
}