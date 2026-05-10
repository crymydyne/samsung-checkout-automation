package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GuestPage extends BasePage {

    private final By emailField =
            By.name("email");

    private final By continueButton =
            By.cssSelector("button");

    public GuestPage(WebDriver driver) {
        super(driver);
    }

    public void enterEmail(String email) {

        type(emailField, email);
    }

    public void continueAsGuest() {

        click(continueButton);
    }
}