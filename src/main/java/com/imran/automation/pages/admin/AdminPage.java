package com.imran.automation.pages.admin;

import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AdminPage extends BasePage {

    @FindBy(xpath = "//span[normalize-space()='Admin']")
    private WebElement adminMenu;

    @FindBy(xpath = "//h6[normalize-space()='Admin']")
    private WebElement adminHeader;

    public AdminPage(WebDriver driver) {
        super(driver);
    }

    public void openAdminModule() {
        click(adminMenu);
        wait.until(ExpectedConditions.visibilityOf(adminHeader));
    }

    public boolean isModuleLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(adminHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public SystemUsersPage openSystemUsersPage() {
        wait.until(ExpectedConditions.visibilityOf(adminHeader));
        return new SystemUsersPage(driver);
    }
}
