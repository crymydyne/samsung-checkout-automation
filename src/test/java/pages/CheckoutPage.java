package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class CheckoutPage extends BasePage {

    private final By fullNameField =
            By.name("fullName");

    private final By phoneField =
            By.name("phone");

    private final By addressField =
            By.name("address");

    private final By cityField =
            By.name("city");

    private final By zipCodeField =
            By.name("zip");

    private final By deliveryModeButton =
            By.cssSelector("button");

    public CheckoutPage(WebDriver driver) {
        super(driver);
    }

    public void fillPersonalInformation(
            String fullName,
            String phone
    ) {

        type(fullNameField, fullName);

        type(phoneField, phone);
    }

    public void fillAddress(
            String address,
            String city,
            String zip
    ) {

        type(addressField, address);

        type(cityField, city);

        type(zipCodeField, zip);
    }

    public void selectDeliveryMode() {

        click(deliveryModeButton);
    }
}