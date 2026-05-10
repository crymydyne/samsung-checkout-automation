package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By continueButton =
            By.cssSelector("button");

    private final By cartBody =
            By.tagName("body");

    public CartPage(WebDriver driver) {

        super(driver);
    }

    public boolean isProductInCart(String sku) {

        return getText(cartBody)
                .contains(sku);
    }

    public void clickContinue() {

        click(continueButton);
    }
}