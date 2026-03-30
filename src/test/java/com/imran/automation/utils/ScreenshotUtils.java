package com.imran.automation.utils;

import com.imran.automation.constants.FrameworkConstants;
import com.imran.automation.factory.DriverFactory;
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

public final class ScreenshotUtils {

    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    private ScreenshotUtils() {
    }

    public static String captureScreenshot(String testName) {
        WebDriver driver = DriverFactory.getDriver();
        if (driver == null || !(driver instanceof TakesScreenshot)) {
            return null;
        }

        try {
            Files.createDirectories(Path.of(FrameworkConstants.SCREENSHOT_DIRECTORY));

            String safeTestName = testName.replaceAll("[^a-zA-Z0-9-_]", "_");
            String fileName = safeTestName + "_" + LocalDateTime.now().format(TIMESTAMP_FORMAT) + ".png";
            Path destination = Path.of(FrameworkConstants.SCREENSHOT_DIRECTORY, fileName);

            File source = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
            Files.copy(source.toPath(), destination, StandardCopyOption.REPLACE_EXISTING);

            return destination.toFile().getAbsolutePath();
        } catch (IOException exception) {
            return null;
        }
    }
}
