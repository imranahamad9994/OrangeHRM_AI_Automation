package com.imran.automation.base;

import com.imran.automation.factory.DriverFactory;
import com.imran.automation.utils.ConfigReader;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

public abstract class BaseTest {

    @BeforeMethod(alwaysRun = true)
    @Parameters("browser")
    public void setUp(@Optional String browser) {
        String browserName = (browser == null || browser.isBlank())
                ? ConfigReader.get("browser")
                : browser;

        DriverFactory.initializeDriver(browserName);
        getDriver().get(ConfigReader.get("baseUrl"));
    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        DriverFactory.quitDriver();
    }

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }
}
