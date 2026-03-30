package com.imran.automation.pages.dashboard;

import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;

public class DashboardPage extends BasePage {

    @FindBy(css = "h6")
    private WebElement dashboardHeader;

    @FindBy(css = ".oxd-userdropdown-tab")
    private WebElement userDropdown;

    @FindBy(xpath = "//a[normalize-space()='Logout']")
    private WebElement logoutLink;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getText(dashboardHeader).equalsIgnoreCase("Dashboard");
    }

    public void logout() {
        click(userDropdown);
        click(logoutLink);
    }
}
