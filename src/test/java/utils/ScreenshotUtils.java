package utils;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class ScreenshotUtils {

    private static final String SCREENSHOT_DIRECTORY = "screenshots";

    private static final DateTimeFormatter TIMESTAMP_FORMAT =
            DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static void takeScreenshot(WebDriver driver, String fileName) {

        try {
            Path screenshotDirectory =
                    Path.of(SCREENSHOT_DIRECTORY);

            Files.createDirectories(screenshotDirectory);

            File sourceFile =
                    ((TakesScreenshot) driver)
                            .getScreenshotAs(OutputType.FILE);

            String timestamp =
                    LocalDateTime.now()
                            .format(TIMESTAMP_FORMAT);

            Path destinationFile =
                    screenshotDirectory.resolve(
                            fileName + "_" + timestamp + ".png"
                    );

            Files.copy(
                    sourceFile.toPath(),
                    destinationFile,
                    StandardCopyOption.REPLACE_EXISTING
            );

            System.out.println(
                    "Screenshot saved: " + destinationFile
            );

        } catch (IOException e) {
            throw new RuntimeException(
                    "Failed to save screenshot: " + fileName,
                    e
            );
        }
    }
}