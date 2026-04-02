package com.imran.automation.tests.pim;

import com.imran.automation.base.BaseTest;
import com.imran.automation.listeners.ExtentReportManager;
import com.imran.automation.models.EmployeeName;
import com.imran.automation.models.LoginTestData;
import com.imran.automation.pages.auth.LoginPage;
import com.imran.automation.pages.dashboard.DashboardPage;
import com.imran.automation.pages.pim.AddEmployeePage;
import com.imran.automation.pages.pim.EmployeeDetailsPage;
import com.imran.automation.pages.pim.EmployeeListPage;
import com.imran.automation.pages.pim.PimPage;
import com.imran.automation.utils.JsonDataReader;
import com.github.javafaker.Faker;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;

public class PimTest extends BaseTest {

    @Test(description = "Verify a user can add, edit, search, and delete a random employee in the OrangeHRM PIM module.")
    public void verifyAddEditAndDeleteEmployeeLifecycle() {
        LoginTestData loginData = (LoginTestData) JsonDataReader.getValidLoginTestData()[0][0];
        Faker faker = new Faker(new Locale("en-IN"));
        EmployeeName createdEmployee = buildRandomEmployeeName(faker, "Create");
        EmployeeName updatedEmployee = buildRandomEmployeeName(faker, "Edit");

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

        String employeeId = addEmployeePage.addEmployee(
                createdEmployee.getFirstName(),
                createdEmployee.getMiddleName(),
                createdEmployee.getLastName()
        );
        EmployeeDetailsPage employeeDetailsPage = addEmployeePage.goToEmployeeDetailsPage();
        Assert.assertTrue(employeeDetailsPage.isLoaded(), "Personal Details page should load after saving employee.");
        ExtentReportManager.logPass("Employee created successfully with Employee ID: " + employeeId);

        EmployeeListPage employeeListPage = pimPage.openEmployeeList();
        Assert.assertTrue(employeeListPage.isLoaded(), "Employee List page should be visible.");
        ExtentReportManager.logPass("Employee List page opened successfully.");

        employeeListPage.searchByEmployeeId(employeeId);
        Assert.assertTrue(employeeListPage.isEmployeePresent(employeeId, createdEmployee.getFirstName(), createdEmployee.getLastName()),
                "Newly added employee should be found in the PIM employee list.");
        ExtentReportManager.logPass("Newly created employee was found successfully in Employee List.");

        employeeDetailsPage = employeeListPage.openFirstSearchResultForEdit();
        Assert.assertTrue(employeeDetailsPage.isLoaded(), "Employee details page should open for edit.");
        employeeDetailsPage.updateEmployeeName(updatedEmployee);
        ExtentReportManager.logPass("Employee update action completed with new faker-generated values.");

        employeeListPage = pimPage.openEmployeeList();
        Assert.assertTrue(employeeListPage.isLoaded(), "Employee List page should be visible after edit.");
        employeeListPage.searchByEmployeeId(employeeId);
        Assert.assertTrue(employeeListPage.isEmployeeIdPresent(employeeId),
                "Edited employee should still be available in the PIM employee list by employee ID.");
        ExtentReportManager.logPass("Edited employee remains accessible in Employee List by employee ID.");

        employeeListPage.deleteFirstSearchResult();
        ExtentReportManager.logPass("Employee delete action completed successfully.");

        employeeListPage.searchByEmployeeId(employeeId);
        Assert.assertTrue(employeeListPage.isNoRecordsFoundDisplayed(),
                "Deleted employee should no longer appear in the PIM employee list.");
        ExtentReportManager.logPass("Deleted employee is no longer present in Employee List.");
    }

    private EmployeeName buildRandomEmployeeName(Faker faker, String suffixLabel) {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis()).substring(7);
        String firstName = sanitizeNamePart(faker.name().firstName()) + suffixLabel + uniqueSuffix;
        String middleName = sanitizeNamePart(faker.name().firstName());
        String lastName = sanitizeNamePart(faker.name().lastName()) + uniqueSuffix;
        return new EmployeeName(firstName, middleName, lastName);
    }

    private String sanitizeNamePart(String value) {
        String sanitized = value == null ? "" : value.replaceAll("[^A-Za-z]", "");
        if (sanitized.isBlank()) {
            return "Auto";
        }
        return sanitized;
    }
}
