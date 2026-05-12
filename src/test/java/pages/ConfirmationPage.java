package pages;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ConfirmationPage extends BasePage {

    private final By pageBody =
            By.tagName("body");

    public ConfirmationPage(WebDriver driver) {
        super(driver);
    }

    public void waitUntilConfirmationPageIsLoaded() {

        System.out.println("STEP: Waiting for order confirmation page");

        waitForAnyTextInBody(
                "Muchas gracias por tu compra",
                "Número de pedido"
        );
    }

    public boolean isOrderSuccessful() {

        waitUntilConfirmationPageIsLoaded();

        String bodyText =
                getText(pageBody);

        return bodyText.contains("Muchas gracias por tu compra")
                && bodyText.contains("Número de pedido");
    }

    public String getOrderNumber() {

        waitUntilConfirmationPageIsLoaded();

        String bodyText =
                getText(pageBody);

        String marker =
                "Número de pedido:";

        int markerIndex =
                bodyText.indexOf(marker);

        if (markerIndex == -1) {
            return "";
        }

        String afterMarker =
                bodyText.substring(markerIndex + marker.length())
                        .trim();

        return afterMarker
                .split("\\s+")[0]
                .trim();
    }
}