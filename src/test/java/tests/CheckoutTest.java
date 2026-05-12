package tests;

import base.BaseTest;
import config.ConfigReader;
import models.TestCardData;
import models.TestCustomerData;
import org.testng.Assert;
import org.testng.annotations.Test;
import pages.CartPage;
import pages.CheckoutPage;
import pages.ConfirmationPage;
import pages.GuestPage;
import utils.ScreenshotUtils;
import utils.TestDataGenerator;

public class CheckoutTest extends BaseTest {

    @Test
    public void shouldCompleteGuestCheckout() {

        System.out.println("STEP: Initializing session");

        driver.get(ConfigReader.getProperty("cookie.url"));

        ScreenshotUtils.takeScreenshot(driver, "01-cookie-page");

        System.out.println("STEP: Adding product to cart");

        driver.get(ConfigReader.getProperty("add.to.cart.url"));

        ScreenshotUtils.takeScreenshot(driver, "02-product-added");

        System.out.println("STEP: Opening cart page");

        driver.get(ConfigReader.getProperty("cart.url"));

        CartPage cartPage =
                new CartPage(driver);

        cartPage.waitUntilCartIsLoaded();

        ScreenshotUtils.takeScreenshot(driver, "03-cart-page");

        Assert.assertTrue(
                cartPage.isCartLoaded(),
                "Cart page did not load correctly."
        );

        Assert.assertTrue(
                cartPage.isProductInCart("RB45DG6300B1PE"),
                "SKU was not found in cart."
        );

        System.out.println("STEP: Proceeding to guest identification");

        cartPage.clickContinue();

        GuestPage guestPage =
                new GuestPage(driver);

        guestPage.waitUntilGuestPageIsLoaded();

        ScreenshotUtils.takeScreenshot(driver, "04-guest-page");

        Assert.assertTrue(
                guestPage.isGuestPageLoaded(),
                "Guest identification page did not load."
        );

        String email =
                TestDataGenerator.generateUniqueEmail();

        System.out.println("Generated guest email: " + email);

        guestPage.enterEmail(email);

        guestPage.continueAsGuest();

        CheckoutPage checkoutPage =
                new CheckoutPage(driver);

        checkoutPage.waitUntilCheckoutPageIsLoaded();

        ScreenshotUtils.takeScreenshot(driver, "05-checkout-personal-info");

        Assert.assertTrue(
                checkoutPage.isCheckoutPageLoaded(),
                "Checkout page did not load."
        );

        System.out.println("STEP: Filling personal information");

        checkoutPage.fillPersonalInformation(
                TestCustomerData.FIRST_NAME,
                TestCustomerData.LAST_NAME,
                TestCustomerData.PHONE,
                TestCustomerData.DOCUMENT_NUMBER
        );

        checkoutPage.continueFromPersonalInformation();

        ScreenshotUtils.takeScreenshot(driver, "06-delivery-section");

        Assert.assertTrue(
                checkoutPage.isAddressSectionEnabled(),
                "Delivery address section was not enabled."
        );

        System.out.println("STEP: Filling delivery address");

        checkoutPage.fillDeliveryAddress(
                TestCustomerData.ADDRESS,
                TestCustomerData.STREET_NUMBER
        );

        checkoutPage.selectDeliveryMode();

        Assert.assertTrue(
                checkoutPage.isDeliveryModeSelected(),
                "Delivery mode was not selected."
        );

        ScreenshotUtils.takeScreenshot(driver, "07-delivery-mode-selected");

        System.out.println("STEP: Accepting required terms");

        checkoutPage.acceptRequiredTerms();

        ScreenshotUtils.takeScreenshot(driver, "08-terms-accepted");

        checkoutPage.continueToPayment();

        ScreenshotUtils.takeScreenshot(driver, "09-payment-section");

        Assert.assertTrue(
                checkoutPage.isPaymentSectionVisible(),
                "Payment section was not displayed."
        );

        System.out.println("STEP: Selecting credit/debit card payment");

        checkoutPage.selectCreditCardPayment();

        ScreenshotUtils.takeScreenshot(driver, "10-credit-card-opened");

        System.out.println("STEP: Filling payment information");

        checkoutPage.enterCardDetails(
                TestCardData.CARD_NUMBER,
                TestCardData.CARDHOLDER_NAME,
                TestCardData.EXPIRATION,
                TestCardData.CVV
        );

        ScreenshotUtils.takeScreenshot(driver, "11-payment-filled");

        System.out.println("STEP: Placing order");

        checkoutPage.placeOrder();

        ScreenshotUtils.takeScreenshot(driver, "12-after-place-order");

        ConfirmationPage confirmationPage =
                new ConfirmationPage(driver);

        confirmationPage.waitUntilConfirmationPageIsLoaded();

        Assert.assertTrue(
                confirmationPage.isOrderSuccessful(),
                "Order confirmation was not displayed."
        );

        String orderNumber =
                confirmationPage.getOrderNumber();

        System.out.println("Generated Order Number: " + orderNumber);

        Assert.assertFalse(
                orderNumber.isEmpty(),
                "Order number was not generated."
        );

        ScreenshotUtils.takeScreenshot(driver, "13-order-confirmation");
    }
}