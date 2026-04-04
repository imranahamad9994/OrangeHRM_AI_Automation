package com.imran.automation.tests.admin;

import com.github.javafaker.Faker;
import com.imran.automation.base.BaseTest;
import com.imran.automation.listeners.ExtentReportManager;
import com.imran.automation.models.AdminUserData;
import com.imran.automation.models.EmployeeName;
import com.imran.automation.models.LoginTestData;
import com.imran.automation.pages.admin.AdminPage;
import com.imran.automation.pages.admin.SystemUsersPage;
import com.imran.automation.pages.admin.UserFormPage;
import com.imran.automation.pages.auth.LoginPage;
import com.imran.automation.pages.dashboard.DashboardPage;
import com.imran.automation.pages.pim.AddEmployeePage;
import com.imran.automation.pages.pim.PimPage;
import com.imran.automation.utils.JsonDataReader;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.Locale;

public class AdminTest extends BaseTest {

    @Test(description = "Verify Admin user form shows required validation when submitted without mandatory fields.")
    public void verifyRequiredFieldValidationForAdminUser() {
        LoginTestData loginData = (LoginTestData) JsonDataReader.getValidLoginTestData()[0][0];

        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());
        AdminPage adminPage = new AdminPage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());
        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible after login.");

        adminPage.openAdminModule();
        Assert.assertTrue(adminPage.isModuleLoaded(), "Admin module should load successfully.");

        SystemUsersPage systemUsersPage = adminPage.openSystemUsersPage();
        Assert.assertTrue(systemUsersPage.isLoaded(), "System Users page should be visible.");

        UserFormPage userFormPage = systemUsersPage.openAddUserForm();
        Assert.assertTrue(userFormPage.isLoaded(), "Add User form should open successfully.");

        userFormPage.submitEmptyFormExpectingRequiredValidation();

        Assert.assertTrue(userFormPage.hasValidationMessage("Required"),
                "The Add User form should show required field validation messages.");
        Assert.assertTrue(userFormPage.getValidationMessageCount() >= 4,
                "Expected multiple required field validation messages on the Add User form.");
        ExtentReportManager.logPass("Required field validation is displayed correctly on the Add User form.");
    }

    @Test(description = "Verify Admin user form rejects usernames shorter than the minimum supported length.")
    public void verifyMinimumUsernameLengthValidationForAdminUser() {
        LoginTestData loginData = (LoginTestData) JsonDataReader.getValidLoginTestData()[0][0];
        Faker faker = new Faker(new Locale("en-IN"));
        EmployeeName employeeName = buildRandomEmployeeName(faker);
        AdminUserData invalidUser = new AdminUserData("adm", "Orange@12345", "ESS", "Enabled");

        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());
        PimPage pimPage = new PimPage(getDriver());
        AdminPage adminPage = new AdminPage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());
        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible after login.");

        createEmployeeForAdminValidation(pimPage, employeeName);
        ExtentReportManager.logPass("Created random employee for Admin username length validation.");

        adminPage.openAdminModule();
        Assert.assertTrue(adminPage.isModuleLoaded(), "Admin module should load successfully.");

        SystemUsersPage systemUsersPage = adminPage.openSystemUsersPage();
        Assert.assertTrue(systemUsersPage.isLoaded(), "System Users page should be visible.");

        UserFormPage userFormPage = systemUsersPage.openAddUserForm();
        Assert.assertTrue(userFormPage.isLoaded(), "Add User form should open successfully.");
        userFormPage.addUserExpectingValidation(employeeName, invalidUser);

        Assert.assertTrue(userFormPage.waitForUsernameValidationContaining("least 5 characters")
                        || userFormPage.hasValidationMessageContaining("least 5 characters"),
                "The Add User form should reject usernames shorter than five characters.");
        ExtentReportManager.logPass("Minimum username length validation is displayed correctly on the Add User form.");
    }

    @Test(description = "Verify a user can add, edit, search, and delete a random admin user in OrangeHRM.")
    public void verifyAddEditAndDeleteUserLifecycle() {
        LoginTestData loginData = (LoginTestData) JsonDataReader.getValidLoginTestData()[0][0];
        Faker faker = new Faker(new Locale("en-IN"));
        EmployeeName employeeName = buildRandomEmployeeName(faker);
        AdminUserData createdUser = buildAdminUserData(faker, "create", "ESS", "Enabled");
        AdminUserData updatedUser = buildAdminUserData(faker, "edit", "Admin", "Disabled");

        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());
        PimPage pimPage = new PimPage(getDriver());
        AdminPage adminPage = new AdminPage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());
        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible after login.");
        ExtentReportManager.logPass("Logged into OrangeHRM successfully for Admin user lifecycle validation.");

        pimPage.openPimModule();
        Assert.assertTrue(pimPage.isModuleLoaded(), "PIM module should load successfully.");
        AddEmployeePage addEmployeePage = pimPage.openAddEmployee();
        Assert.assertTrue(addEmployeePage.isLoaded(), "Add Employee page should be visible.");
        String employeeId = addEmployeePage.addEmployee(
                employeeName.getFirstName(),
                employeeName.getMiddleName(),
                employeeName.getLastName()
        );
        ExtentReportManager.logPass("Created random employee for Admin user mapping. Employee ID: " + employeeId);

        adminPage.openAdminModule();
        Assert.assertTrue(adminPage.isModuleLoaded(), "Admin module should load successfully.");
        ExtentReportManager.logPass("Admin module opened successfully.");

        SystemUsersPage systemUsersPage = adminPage.openSystemUsersPage();
        Assert.assertTrue(systemUsersPage.isLoaded(), "System Users page should be visible.");

        UserFormPage userFormPage = systemUsersPage.openAddUserForm();
        Assert.assertTrue(userFormPage.isLoaded(), "Add User form should open successfully.");
        userFormPage.addUser(employeeName, createdUser);
        ExtentReportManager.logPass("Created system user successfully for employee " + employeeName.getFullName()
                + " using username " + createdUser.getUsername() + ".");

        systemUsersPage.searchByUsername(createdUser.getUsername());
        Assert.assertTrue(systemUsersPage.isUserPresent(createdUser.getUsername(), createdUser),
                "Newly created admin user should be found in System Users.");
        ExtentReportManager.logPass("Created system user was found successfully in System Users.");

        userFormPage = systemUsersPage.openFirstSearchResultForEdit();
        Assert.assertTrue(userFormPage.isLoaded(), "Edit User form should open successfully.");
        Assert.assertEquals(userFormPage.getCurrentUsername(), createdUser.getUsername(),
                "Expected the created user to open in edit mode.");
        userFormPage.updateUser(updatedUser);
        ExtentReportManager.logPass("System user updated successfully with a new random username and status.");

        systemUsersPage.searchByUsername(updatedUser.getUsername());
        Assert.assertTrue(systemUsersPage.isUserPresent(updatedUser.getUsername(), updatedUser),
                "Updated admin user should be found in System Users.");
        ExtentReportManager.logPass("Updated system user was found successfully in System Users.");

        systemUsersPage.deleteFirstSearchResult();
        ExtentReportManager.logPass("System user delete action completed successfully.");

        systemUsersPage.searchByUsername(updatedUser.getUsername());
        Assert.assertTrue(systemUsersPage.isNoRecordsFoundDisplayed(),
                "Deleted admin user should no longer appear in System Users.");
        ExtentReportManager.logPass("Deleted system user is no longer present in System Users.");
    }

    private AdminUserData buildAdminUserData(Faker faker, String suffixLabel, String role, String status) {
        String uniqueSuffix = String.valueOf(System.nanoTime()).substring(8);
        String username = "imran" + suffixLabel + sanitizeNamePart(faker.name().firstName()).toLowerCase() + uniqueSuffix;
        return new AdminUserData(username, "Orange@12345", role, status);
    }

    private void createEmployeeForAdminValidation(PimPage pimPage, EmployeeName employeeName) {
        pimPage.openPimModule();
        Assert.assertTrue(pimPage.isModuleLoaded(), "PIM module should load successfully.");
        AddEmployeePage addEmployeePage = pimPage.openAddEmployee();
        Assert.assertTrue(addEmployeePage.isLoaded(), "Add Employee page should be visible.");
        addEmployeePage.addEmployee(
                employeeName.getFirstName(),
                employeeName.getMiddleName(),
                employeeName.getLastName()
        );
    }

    private EmployeeName buildRandomEmployeeName(Faker faker) {
        String uniqueSuffix = String.valueOf(System.currentTimeMillis()).substring(7);
        String firstName = sanitizeNamePart(faker.name().firstName()) + "Admin" + uniqueSuffix;
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
