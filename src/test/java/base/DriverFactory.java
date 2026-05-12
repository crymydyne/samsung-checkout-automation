package base;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.Dimension;

import java.time.Duration;

public class DriverFactory {

    public static WebDriver createDriver() {

        WebDriverManager.chromedriver().setup();

        WebDriver driver = new ChromeDriver();

        driver.manage().window().maximize();
        driver.manage().window().setSize(new Dimension(1920, 1080));

        driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));

        return driver;
    }
}