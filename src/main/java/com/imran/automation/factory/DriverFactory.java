package com.imran.automation.factory;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.SessionNotCreatedException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.edge.EdgeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

import java.nio.file.Files;
import java.nio.file.Path;
import java.io.IOException;

public final class DriverFactory {

    private static final ThreadLocal<WebDriver> DRIVER = new ThreadLocal<>();

    private DriverFactory() {
    }

    public static void initializeDriver(String browser, boolean headless) {
        String browserName = browser == null ? "chrome" : browser.trim().toLowerCase();
        WebDriver driver;

        switch (browserName) {
            case "firefox":
                WebDriverManager.firefoxdriver().setup();
                FirefoxOptions firefoxOptions = new FirefoxOptions();
                if (headless) {
                    firefoxOptions.addArguments("-headless");
                }
                driver = new FirefoxDriver(firefoxOptions);
                break;
            case "edge":
                WebDriverManager.edgedriver().setup();
                driver = createEdgeDriver(headless);
                break;
            case "chrome":
            default:
                WebDriverManager.chromedriver().setup();
                driver = createChromeDriver(headless);
                break;
        }

        if (!headless && !"chrome".equals(browserName)) {
            driver.manage().window().maximize();
        }

        DRIVER.set(driver);
    }

    public static WebDriver getDriver() {
        return DRIVER.get();
    }

    public static void quitDriver() {
        WebDriver driver = DRIVER.get();
        if (driver != null) {
            driver.quit();
            DRIVER.remove();
        }
    }

    private static void applyChromiumDefaults(ChromeOptions options) {
        options.addArguments(
                "--window-size=1920,1080",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--disable-gpu",
                "--remote-allow-origins=*",
                "--remote-debugging-port=9222",
                "--disable-extensions",
                "--disable-notifications",
                "--disable-popup-blocking"
        );
        options.addArguments("--user-data-dir=" + createTempProfileDirectory("chrome-profile-"));
    }

    private static void applyChromiumDefaults(EdgeOptions options) {
        options.addArguments(
                "--window-size=1920,1080",
                "--disable-dev-shm-usage",
                "--no-sandbox",
                "--disable-gpu",
                "--remote-debugging-port=9223",
                "--disable-extensions",
                "--disable-notifications",
                "--disable-popup-blocking"
        );
        options.addArguments("--user-data-dir=" + createTempProfileDirectory("edge-profile-"));
    }

    private static WebDriver createChromeDriver(boolean headless) {
        ChromeOptions primaryOptions = new ChromeOptions();
        applyChromiumDefaults(primaryOptions);
        if (headless) {
            primaryOptions.addArguments("--headless=new");
        } else {
            primaryOptions.addArguments("--start-maximized");
        }

        try {
            return new ChromeDriver(primaryOptions);
        } catch (SessionNotCreatedException exception) {
            if (!headless) {
                throw exception;
            }

            ChromeOptions fallbackOptions = new ChromeOptions();
            applyChromiumDefaults(fallbackOptions);
            fallbackOptions.addArguments("--headless");
            return new ChromeDriver(fallbackOptions);
        }
    }

    private static WebDriver createEdgeDriver(boolean headless) {
        EdgeOptions primaryOptions = new EdgeOptions();
        applyChromiumDefaults(primaryOptions);
        if (headless) {
            primaryOptions.addArguments("--headless=new");
        }

        try {
            return new EdgeDriver(primaryOptions);
        } catch (SessionNotCreatedException exception) {
            if (!headless) {
                throw exception;
            }

            EdgeOptions fallbackOptions = new EdgeOptions();
            applyChromiumDefaults(fallbackOptions);
            fallbackOptions.addArguments("--headless");
            return new EdgeDriver(fallbackOptions);
        }
    }

    private static String createTempProfileDirectory(String prefix) {
        try {
            Path tempDirectory = Files.createTempDirectory(prefix);
            tempDirectory.toFile().deleteOnExit();
            return tempDirectory.toAbsolutePath().toString();
        } catch (IOException exception) {
            throw new IllegalStateException("Unable to create temporary browser profile directory.", exception);
        }
    }
}
