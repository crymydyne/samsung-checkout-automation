package tests;

import base.BaseTest;
import config.ConfigReader;
import models.TestCardData;
import models.TestCustomerData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.*;
import utils.ScreenshotUtils;
import utils.TestDataGenerator;

public class CheckoutTest extends BaseTest {

    @Test
    public void shouldCompleteGuestCheckout() {

        driver.get(
                ConfigReader.getProperty("cookie.url")
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "cookie-session"
        );

        driver.get(
                ConfigReader.getProperty("add.to.cart.url")
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "product-added-to-cart"
        );

        driver.get(
                ConfigReader.getProperty("cart.url")
        );

        CartPage cartPage =
                new CartPage(driver);

        cartPage.clickContinue();

        GuestPage guestPage =
                new GuestPage(driver);

        String email =
                TestDataGenerator.generateUniqueEmail();

        guestPage.enterEmail(email);

        guestPage.continueAsGuest();

        CheckoutPage checkoutPage =
                new CheckoutPage(driver);

        checkoutPage.fillPersonalInformation(
                TestCustomerData.FULL_NAME,
                TestCustomerData.PHONE
        );

        checkoutPage.fillAddress(
                TestCustomerData.ADDRESS,
                TestCustomerData.CITY,
                TestCustomerData.ZIP
        );

        checkoutPage.selectDeliveryMode();

        PaymentPage paymentPage =
                new PaymentPage(driver);

        paymentPage.enterCardDetails(
                TestCardData.CARD_NUMBER,
                TestCardData.EXPIRATION,
                TestCardData.CVV
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "before-order-placement"
        );

        paymentPage.placeOrder();

        ConfirmationPage confirmationPage =
                new ConfirmationPage(driver);

        Assert.assertTrue(
                confirmationPage.isOrderSuccessful(),
                "Order confirmation was not displayed."
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "order-confirmation"
        );
    }
}