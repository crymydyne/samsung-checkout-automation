package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

public class CartPage extends BasePage {

    private final By cartBody =
            By.tagName("body");

    private final By continueButton =
            By.xpath("//*[self::button or self::a or @role='button'][contains(normalize-space(.), 'Continuar')]");

    private static final String EXPECTED_SKU =
            "RB45DG6300B1PE";

    public CartPage(WebDriver driver) {

        super(driver);
    }

    public void waitUntilCartIsLoaded() {

        waitForAnyTextInBody(
                EXPECTED_SKU,
                "Refrigeradora",
                "producto en tu carrito",
                "carrito"
        );
    }

    public String getCartText() {

        waitUntilCartIsLoaded();

        return getText(cartBody);
    }

    public boolean isCartLoaded() {

        String bodyText =
                getCartText();

        String normalizedText =
                bodyText.toLowerCase();

        return bodyText.contains(EXPECTED_SKU)
                || normalizedText.contains("producto en tu carrito")
                || normalizedText.contains("refrigeradora")
                || normalizedText.contains("carrito");
    }

    public boolean isProductInCart(String sku) {

        String bodyText =
                getCartText();

        return bodyText.contains(sku);
    }

    public void clickContinue() {

        waitUntilCartIsLoaded();

        try {

            click(continueButton);

        } catch (TimeoutException e) {

            System.out.println("Could not click Continuar with standard locator.");
            System.out.println("Printing clickable elements for debugging:");

            printClickableElements();

            WebElement fallbackButton =
                    findContinueButtonWithJavascript();

            if (fallbackButton == null) {

                throw e;
            }

            ((JavascriptExecutor) driver)
                    .executeScript(
                            "arguments[0].scrollIntoView({block: 'center'});",
                            fallbackButton
                    );

            ((JavascriptExecutor) driver)
                    .executeScript(
                            "arguments[0].click();",
                            fallbackButton
                    );
        }
    }

    private WebElement findContinueButtonWithJavascript() {

        return (WebElement) ((JavascriptExecutor) driver)
                .executeScript(
                        "const elements = Array.from(document.querySelectorAll('button, a, [role=\"button\"]'));" +
                                "return elements.find(e => {" +
                                "  const text = (e.innerText || e.textContent || e.getAttribute('aria-label') || '').toLowerCase();" +
                                "  return text.includes('continuar');" +
                                "});"
                );
    }

    private void printClickableElements() {

        List<WebElement> elements =
                driver.findElements(
                        By.xpath("//button | //a | //*[@role='button']")
                );

        for (int i = 0; i < elements.size(); i++) {

            WebElement element =
                    elements.get(i);

            String text =
                    element.getText();

            String tag =
                    element.getTagName();

            String ariaLabel =
                    element.getAttribute("aria-label");

            String className =
                    element.getAttribute("class");

            System.out.println(
                    i +
                            " | tag=" + tag +
                            " | text=" + text +
                            " | aria-label=" + ariaLabel +
                            " | class=" + className
            );
        }
    }
}