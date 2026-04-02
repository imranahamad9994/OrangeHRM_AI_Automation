package com.imran.automation.pages.leave;

import com.imran.automation.pages.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class LeavePage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(LeavePage.class);
    private static final By LOADING_SPINNER = By.cssSelector(".oxd-loading-spinner");

    @FindBy(xpath = "//span[normalize-space()='Leave']")
    private WebElement leaveMenu;

    @FindBy(xpath = "//h6[normalize-space()='Leave']")
    private WebElement leaveHeader;

    @FindBy(xpath = "//a[normalize-space()='Apply']")
    private WebElement applyTab;

    @FindBy(xpath = "//a[normalize-space()='My Leave']")
    private WebElement myLeaveTab;

    @FindBy(xpath = "//span[normalize-space()='Entitlements']")
    private WebElement entitlementsTab;

    @FindBy(xpath = "//a[normalize-space()='Add Entitlements']")
    private WebElement addEntitlementsOption;

    public LeavePage(WebDriver driver) {
        super(driver);
    }

    public void openLeaveModule() {
        LOGGER.info("Opening Leave module from side menu.");
        click(leaveMenu);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.visibilityOf(leaveHeader));
        LOGGER.info("Leave module opened. Current URL: {}", getCurrentUrl());
    }

    public boolean isModuleLoaded() {
        try {
            waitForLoaderToDisappear();
            return wait.until(ExpectedConditions.visibilityOf(leaveHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public ApplyLeavePage openApplyLeave() {
        LOGGER.info("Opening Apply Leave tab.");
        click(applyTab);
        waitForLoaderToDisappear();
        LOGGER.info("Apply Leave tab opened. Current URL: {}", getCurrentUrl());
        return new ApplyLeavePage(driver);
    }

    public MyLeavePage openMyLeave() {
        LOGGER.info("Opening My Leave tab.");
        click(myLeaveTab);
        waitForLoaderToDisappear();
        LOGGER.info("My Leave tab opened. Current URL: {}", getCurrentUrl());
        return new MyLeavePage(driver);
    }

    public AddEntitlementPage openAddEntitlements() {
        LOGGER.info("Opening Entitlements -> Add Entitlements.");
        clickUsingJs(entitlementsTab);
        wait.until(ExpectedConditions.visibilityOf(addEntitlementsOption));
        clickUsingJs(addEntitlementsOption);
        waitForLoaderToDisappear();
        LOGGER.info("Add Entitlements opened. Current URL: {}", getCurrentUrl());
        return new AddEntitlementPage(driver);
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }
}
