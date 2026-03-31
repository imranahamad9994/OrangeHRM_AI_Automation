package com.imran.automation.pages.dashboard;

import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.TimeoutException;

import java.util.List;

public class DashboardPage extends BasePage {

    @FindBy(css = "h6")
    private WebElement dashboardHeader;

    @FindBy(css = ".oxd-sidepanel")
    private WebElement sidePanel;

    @FindBy(css = "input[placeholder='Search']")
    private WebElement sidePanelSearchBox;

    @FindBy(css = ".oxd-userdropdown-tab")
    private WebElement userDropdown;

    @FindBy(xpath = "//a[normalize-space()='Logout']")
    private WebElement logoutLink;

    @FindBy(css = ".oxd-main-menu-item-wrapper")
    private List<WebElement> sidebarMenuItems;

    @FindBy(xpath = "//p[normalize-space()='Time at Work']")
    private WebElement timeAtWorkWidget;

    @FindBy(xpath = "//p[normalize-space()='Quick Launch']")
    private WebElement quickLaunchWidget;

    public DashboardPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        return getText(dashboardHeader).equalsIgnoreCase("Dashboard");
    }

    public boolean isSidePanelVisible() {
        return isElementVisible(sidePanel);
    }

    public boolean isSidePanelSearchBoxVisible() {
        return isElementVisible(sidePanelSearchBox);
    }

    public boolean isUserDropdownVisible() {
        return isElementVisible(userDropdown);
    }

    public boolean isSidebarMenuVisible() {
        try {
            wait.until(driver -> sidebarMenuItems.size() >= 5);
            return sidebarMenuItems.size() >= 5;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public boolean isTimeAtWorkWidgetVisible() {
        return isElementVisible(timeAtWorkWidget);
    }

    public boolean isQuickLaunchWidgetVisible() {
        return isElementVisible(quickLaunchWidget);
    }

    public boolean areCoreDashboardElementsVisible() {
        return isSidePanelVisible()
                && isSidePanelSearchBoxVisible()
                && isUserDropdownVisible()
                && isSidebarMenuVisible()
                && isTimeAtWorkWidgetVisible()
                && isQuickLaunchWidgetVisible();
    }

    public void logout() {
        click(userDropdown);
        click(logoutLink);
    }

    private boolean isElementVisible(WebElement element) {
        try {
            return wait.until(ExpectedConditions.visibilityOf(element)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }
}
