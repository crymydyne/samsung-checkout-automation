package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class GuestPage extends BasePage {

    private final By pageBody =
            By.tagName("body");

    private final By guestEmailField =
            By.xpath("//input[contains(@placeholder, 'correo') or contains(@placeholder, 'Correo') or @type='email']");

    private final By checkoutAsGuestButton =
            By.xpath("//button[contains(normalize-space(), 'Checkout como invitado')]");

    public GuestPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilGuestPageIsLoaded() {

        waitForAnyTextInBody(
                "Continua iniciando sesión",
                "Continuar como invitado",
                "Ingresa tu correo",
                "Checkout como invitado",
                "Resumen del pedido"
        );
    }

    public boolean isGuestPageLoaded() {

        waitUntilGuestPageIsLoaded();

        String bodyText =
                getText(pageBody);

        return bodyText.contains("Continuar como invitado")
                || bodyText.contains("Ingresa tu correo")
                || bodyText.contains("Checkout como invitado");
    }

    public String getGuestPageText() {

        waitUntilGuestPageIsLoaded();

        return getText(pageBody);
    }

    public void enterEmail(String email) {

        waitUntilGuestPageIsLoaded();

        type(guestEmailField, email);
    }

    public void continueAsGuest() {

        click(checkoutAsGuestButton);
    }
}