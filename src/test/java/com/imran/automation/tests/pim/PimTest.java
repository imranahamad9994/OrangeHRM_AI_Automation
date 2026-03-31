package com.imran.automation.tests.pim;

import com.imran.automation.base.BaseTest;
import com.imran.automation.listeners.ExtentReportManager;
import com.imran.automation.models.LoginTestData;
import com.imran.automation.models.PimEmployeeData;
import com.imran.automation.pages.auth.LoginPage;
import com.imran.automation.pages.dashboard.DashboardPage;
import com.imran.automation.pages.pim.AddEmployeePage;
import com.imran.automation.pages.pim.EmployeeListPage;
import com.imran.automation.pages.pim.PimPage;
import com.imran.automation.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class PimTest extends BaseTest {

    @DataProvider(name = "validLoginData")
    public Object[][] validLoginData() {
        return JsonDataReader.getValidLoginTestData();
    }

    @DataProvider(name = "pimEmployeeData")
    public Object[][] pimEmployeeData() {
        return JsonDataReader.getPimEmployeeTestData();
    }

    @Test(dataProvider = "pimEmployeeData", description = "Verify a user can add a new employee in PIM and find it in Employee List.")
    public void verifyAddAndSearchEmployee(PimEmployeeData employeeData) {
        LoginTestData loginData = (LoginTestData) JsonDataReader.getValidLoginTestData()[0][0];
        String uniqueSuffix = String.valueOf(System.currentTimeMillis()).substring(7);
        String firstName = employeeData.getFirstName() + uniqueSuffix;
        String middleName = employeeData.getMiddleName();
        String lastName = employeeData.getLastName() + uniqueSuffix;

        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());
        PimPage pimPage = new PimPage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());
        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible after login.");
        ExtentReportManager.logPass("Logged into OrangeHRM successfully for PIM validation.");

        pimPage.openPimModule();
        Assert.assertTrue(pimPage.isModuleLoaded(), "PIM module should load successfully.");
        ExtentReportManager.logPass("PIM module opened successfully.");

        AddEmployeePage addEmployeePage = pimPage.openAddEmployee();
        Assert.assertTrue(addEmployeePage.isLoaded(), "Add Employee page should be visible.");
        ExtentReportManager.logPass("Add Employee page opened successfully.");

        String employeeId = addEmployeePage.addEmployee(firstName, middleName, lastName);
        Assert.assertTrue(addEmployeePage.isPersonalDetailsPageLoaded(), "Personal Details page should load after saving employee.");
        ExtentReportManager.logPass("Employee created successfully with Employee ID: " + employeeId);

        EmployeeListPage employeeListPage = pimPage.openEmployeeList();
        Assert.assertTrue(employeeListPage.isLoaded(), "Employee List page should be visible.");
        ExtentReportManager.logPass("Employee List page opened successfully.");

        employeeListPage.searchByEmployeeId(employeeId);
        Assert.assertTrue(employeeListPage.isEmployeePresent(employeeId, firstName, lastName),
                "Newly added employee should be found in the PIM employee list.");
        ExtentReportManager.logPass("Newly created employee was found successfully in Employee List.");
    }
}
