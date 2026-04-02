package com.imran.automation.pages.leave;

import com.imran.automation.pages.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddEntitlementPage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(AddEntitlementPage.class);
    private static final By LOADING_SPINNER = By.cssSelector(".oxd-loading-spinner");
    private static final By PAGE_BODY = By.tagName("body");
    private static final By ADD_ENTITLEMENT_HEADER = By.xpath("//h6[contains(normalize-space(),'Entitlement')]");

    public AddEntitlementPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            waitForLoaderToDisappear();
            boolean loaded = isVisible(ADD_ENTITLEMENT_HEADER);
            LOGGER.info("Add Entitlement page loaded status: {}, current URL: {}", loaded, getCurrentUrl());
            return loaded;
        } catch (TimeoutException exception) {
            LOGGER.error("Timed out while waiting for Add Entitlement page to load.", exception);
            return false;
        }
    }

    public String getPageText() {
        return getText(PAGE_BODY);
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }
}
