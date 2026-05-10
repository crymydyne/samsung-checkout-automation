package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PaymentPage extends BasePage {

    private final By cardNumberField =
            By.name("cardNumber");

    private final By expirationField =
            By.name("expiration");

    private final By cvvField =
            By.name("cvv");

    private final By placeOrderButton =
            By.cssSelector("button");

    public PaymentPage(WebDriver driver) {
        super(driver);
    }

    public void enterCardDetails(
            String cardNumber,
            String expiration,
            String cvv
    ) {

        type(cardNumberField, cardNumber);

        type(expirationField, expiration);

        type(cvvField, cvv);
    }

    public void placeOrder() {

        click(placeOrderButton);
    }
}