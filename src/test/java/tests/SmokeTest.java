package tests;

import base.BaseTest;
import org.testng.annotations.Test;
import pages.CartPage;
import utils.ScreenshotUtils;

public class SmokeTest extends BaseTest {

    @Test
    public void shouldOpenSamsungPage() {

        driver.get("https://stg2.shop.samsung.com/getcookie.html");

        ScreenshotUtils.takeScreenshot(driver, "cookie-page");

        CartPage cartPage = new CartPage(driver);

        System.out.println("Framework initialized successfully.");
    }
}