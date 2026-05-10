package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CartPage extends BasePage {

    private final By continueButton =
            By.cssSelector("button");

    public CartPage(WebDriver driver) {
        super(driver);
    }

    public void clickContinue() {
        click(continueButton);
    }
}