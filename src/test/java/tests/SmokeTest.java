package tests;

import base.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SmokeTest extends BaseTest {

    @Test
    public void shouldOpenSamsungPage() {

        driver.get("https://stg2.shop.samsung.com/getcookie.html");

        String currentUrl = driver.getCurrentUrl();

        Assert.assertTrue(currentUrl.contains("getcookie"));
    }
}