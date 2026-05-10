package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmationPage extends BasePage {

    private final By successMessage =
            By.tagName("body");

    private final By orderNumber =
            By.cssSelector(".order-number");

    public ConfirmationPage(WebDriver driver) {

        super(driver);
    }

    public boolean isOrderSuccessful() {

        return getText(successMessage)
                .contains("Order");
    }

    public String getOrderNumber() {

        return getText(orderNumber);
    }
}