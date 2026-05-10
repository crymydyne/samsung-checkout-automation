package tests;

import base.BaseTest;
import config.ConfigReader;
import org.testng.Assert;
import org.testng.annotations.Test;
import utils.ScreenshotUtils;

public class SmokeTest extends BaseTest {

    @Test
    public void shouldInitializeSamsungSession() {

        String url =
                ConfigReader.getProperty("cookie.url");

        driver.get(url);

        ScreenshotUtils.takeScreenshot(
                driver,
                "cookie-page"
        );

        String pageSource =
                driver.getPageSource();

        Assert.assertTrue(
                pageSource.contains("access"),
                "Cookie initialization page did not load correctly."
        );
    }
}