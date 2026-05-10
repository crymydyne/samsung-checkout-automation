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

        System.out.println("STEP: Initializing session");

        driver.get(
                ConfigReader.getProperty("cookie.url")
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "01-cookie-page"
        );

        System.out.println("STEP: Adding product to cart");

        driver.get(
                ConfigReader.getProperty("add.to.cart.url")
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "02-product-added"
        );

        System.out.println("STEP: Opening cart page");

        driver.get(
                ConfigReader.getProperty("cart.url")
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "03-cart-page"
        );

        CartPage cartPage =
                new CartPage(driver);

        Assert.assertTrue(
                cartPage.isProductInCart(
                        "RB45DG6300B1PE"
                ),
                "SKU was not found in cart."
        );

        System.out.println("STEP: Proceeding to checkout");

        cartPage.clickContinue();

        ScreenshotUtils.takeScreenshot(
                driver,
                "04-guest-page"
        );

        GuestPage guestPage =
                new GuestPage(driver);

        String email =
                TestDataGenerator.generateUniqueEmail();

        System.out.println(
                "Generated email: " + email
        );

        guestPage.enterEmail(email);

        guestPage.continueAsGuest();

        ScreenshotUtils.takeScreenshot(
                driver,
                "05-checkout-page"
        );

        CheckoutPage checkoutPage =
                new CheckoutPage(driver);

        System.out.println(
                "STEP: Filling personal information"
        );

        checkoutPage.fillPersonalInformation(
                TestCustomerData.FULL_NAME,
                TestCustomerData.PHONE
        );

        Assert.assertTrue(
                checkoutPage.isAddressSectionEnabled(),
                "Address section was not enabled."
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "06-personal-information"
        );

        System.out.println(
                "STEP: Filling address information"
        );

        checkoutPage.fillAddress(
                TestCustomerData.ADDRESS,
                TestCustomerData.CITY,
                TestCustomerData.ZIP
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "07-address-information"
        );

        System.out.println(
                "STEP: Selecting delivery mode"
        );

        checkoutPage.selectDeliveryMode();

        Assert.assertTrue(
                checkoutPage.isDeliveryModeSelected(),
                "Delivery mode was not selected."
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "08-delivery-mode"
        );

        PaymentPage paymentPage =
                new PaymentPage(driver);

        System.out.println(
                "STEP: Entering payment information"
        );

        paymentPage.enterCardDetails(
                TestCardData.CARD_NUMBER,
                TestCardData.EXPIRATION,
                TestCardData.CVV
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "09-payment-page"
        );

        System.out.println(
                "STEP: Placing order"
        );

        paymentPage.placeOrder();

        ConfirmationPage confirmationPage =
                new ConfirmationPage(driver);

        Assert.assertTrue(
                confirmationPage.isOrderSuccessful(),
                "Order confirmation was not displayed."
        );

        String orderNumber =
                confirmationPage.getOrderNumber();

        System.out.println(
                "Generated Order Number: "
                        + orderNumber
        );

        Assert.assertFalse(
                orderNumber.isEmpty(),
                "Order number was not generated."
        );

        ScreenshotUtils.takeScreenshot(
                driver,
                "10-order-confirmation"
        );
    }
}