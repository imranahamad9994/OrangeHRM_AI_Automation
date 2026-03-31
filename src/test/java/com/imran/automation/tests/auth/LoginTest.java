package com.imran.automation.tests.auth;

import com.imran.automation.base.BaseTest;
import com.imran.automation.constants.FrameworkConstants;
import com.imran.automation.listeners.ExtentReportManager;
import com.imran.automation.models.LoginTestData;
import com.imran.automation.pages.auth.LoginPage;
import com.imran.automation.pages.dashboard.DashboardPage;
import com.imran.automation.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class LoginTest extends BaseTest {

    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
        return JsonDataReader.getValidLoginTestData();
    }

    @DataProvider(name = "invalidLoginData")
    public Object[][] invalidLoginData() {
        return JsonDataReader.getInvalidLoginTestData();
    }

    @Test(dataProvider = "validLoginData", description = "Verify valid OrangeHRM login using Page Factory and JSON test data.")
    public void verifyValidLogin(LoginTestData loginData) {
        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());

        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible after successful login.");
        Assert.assertTrue(dashboardPage.getCurrentUrl().contains(loginData.getExpectedUrlKeyword()),
                "User should land on the dashboard page after login.");
    }

    @Test(dataProvider = "validLoginData", description = "Verify the core dashboard elements are visible after a successful login.")
    public void verifyDashboardElementsVisible(LoginTestData loginData) {
        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());

        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible after successful login.");

        boolean sidePanelVisible = logElementStatus("Side Panel", dashboardPage.isSidePanelVisible());
        boolean searchBoxVisible = logElementStatus("Side Panel Search Box", dashboardPage.isSidePanelSearchBoxVisible());
        boolean userDropdownVisible = logElementStatus("User Dropdown", dashboardPage.isUserDropdownVisible());
        boolean sidebarMenuVisible = logElementStatus("Sidebar Menu Items", dashboardPage.isSidebarMenuVisible());
        boolean timeAtWorkVisible = logElementStatus("Time at Work Widget", dashboardPage.isTimeAtWorkWidgetVisible());
        boolean quickLaunchVisible = logElementStatus("Quick Launch Widget", dashboardPage.isQuickLaunchWidgetVisible());

        Assert.assertTrue(sidePanelVisible
                        && searchBoxVisible
                        && userDropdownVisible
                        && sidebarMenuVisible
                        && timeAtWorkVisible
                        && quickLaunchVisible,
                "Core dashboard elements should be visible after login.");
    }

    @Test(dataProvider = "validLoginData", description = "Verify a logged-in OrangeHRM user can log out successfully.")
    public void verifySuccessfulLogout(LoginTestData loginData) {
        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());
        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible before logout.");

        dashboardPage.logout();

        Assert.assertTrue(loginPage.isLoaded(), "User should be redirected back to the login page after logout.");
    }

    @Test(dataProvider = "invalidLoginData", description = "Verify invalid login scenarios show the correct validation message.")
    public void verifyInvalidLogin(LoginTestData loginData) {
        LoginPage loginPage = new LoginPage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());

        Assert.assertEquals(loginPage.getErrorMessage(),
                loginData.getExpectedErrorMessage() == null
                        ? FrameworkConstants.INVALID_CREDENTIALS_MESSAGE
                        : loginData.getExpectedErrorMessage(),
                "The application should display the expected invalid login message.");
    }

    private boolean logElementStatus(String elementName, boolean status) {
        String message = "[Dashboard Check] " + elementName + " : " + (status ? "SUCCESS" : "FAILED");
        System.out.println(message);
        if (status) {
            ExtentReportManager.logPass(message);
        } else {
            ExtentReportManager.logFail(message);
        }
        return status;
    }
}
