package com.imran.automation.base;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import com.imran.automation.factory.DriverFactory;
import com.imran.automation.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(BaseTest.class);

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional String browser) {
        String browserName = (browser == null || browser.isBlank())
                ? ConfigReader.get("browser")
                : browser;
        boolean headless = Boolean.parseBoolean(ConfigReader.get("headless"));

        LOGGER.info("Starting test setup. Browser parameter: {}, resolved browser: {}, headless: {}", browser, browserName, headless);
        DriverFactory.initializeDriver(browserName, headless);
        getDriver().get(ConfigReader.get("baseUrl"));
        LOGGER.info("Navigated to base URL: {}", ConfigReader.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        LOGGER.info("Starting test teardown.");
        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
