package com.imran.automation.tests.leave;

import com.github.javafaker.Faker;
import com.imran.automation.base.BaseTest;
import com.imran.automation.listeners.ExtentReportManager;
import com.imran.automation.models.LoginTestData;
import com.imran.automation.pages.auth.LoginPage;
import com.imran.automation.pages.dashboard.DashboardPage;
import com.imran.automation.pages.leave.AddEntitlementPage;
import com.imran.automation.pages.leave.ApplyLeavePage;
import com.imran.automation.pages.leave.LeavePage;
import com.imran.automation.pages.leave.MyLeavePage;
import com.imran.automation.utils.JsonDataReader;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.Locale;

public class LeaveTest extends BaseTest {

    private static final Logger LOGGER = LogManager.getLogger(LeaveTest.class);

    @Test(description = "Exploratory Leave workflow. OrangeHRM demo currently renders Apply Leave without selectable leave types for the demo user.")
    public void verifyApplyVerifyAndCancelLeave() {
        LoginTestData loginData = (LoginTestData) JsonDataReader.getValidLoginTestData()[0][0];
        LocalDate leaveDate = getNextWorkingDay();
        Faker faker = new Faker(new Locale("en-IN"));
        String comment = "AutoLeave-" + faker.number().digits(6);
        LOGGER.info("Starting Leave workflow test. Leave date: {}, comment marker: {}", leaveDate, comment);

        LoginPage loginPage = new LoginPage(getDriver());
        DashboardPage dashboardPage = new DashboardPage(getDriver());
        LeavePage leavePage = new LeavePage(getDriver());

        loginPage.login(loginData.getUsername(), loginData.getPassword());
        Assert.assertTrue(dashboardPage.isLoaded(), "Dashboard should be visible after login.");
        ExtentReportManager.logPass("Logged into OrangeHRM successfully for Leave workflow.");
        String loggedInUserName = dashboardPage.getLoggedInUserName();
        System.out.println("[Leave Debug] Logged In User: " + loggedInUserName);
        LOGGER.info("Logged into OrangeHRM with visible user name: {}", loggedInUserName);

        leavePage.openLeaveModule();
        Assert.assertTrue(leavePage.isModuleLoaded(), "Leave module should load successfully.");
        ExtentReportManager.logPass("Leave module opened successfully.");
        LOGGER.info("Leave module opened successfully.");

        AddEntitlementPage addEntitlementPage = leavePage.openAddEntitlements();
        System.out.println("[Leave Debug] Add Entitlement URL: " + addEntitlementPage.getCurrentUrl());
        System.out.println("[Leave Debug] Add Entitlement Page Text: " + addEntitlementPage.getPageText());
        LOGGER.info("Add Entitlement URL: {}", addEntitlementPage.getCurrentUrl());
        LOGGER.info("Add Entitlement page text length: {}", addEntitlementPage.getPageText().length());
        writeAddEntitlementPageSource(addEntitlementPage.getPageSource());
        Assert.assertTrue(addEntitlementPage.isLoaded(), "Add Entitlement page should be visible.");

        ApplyLeavePage applyLeavePage = leavePage.openApplyLeave();
        Assert.assertTrue(applyLeavePage.isLoaded(), "Apply Leave page should be visible.");
        System.out.println("[Leave Debug] Current URL: " + applyLeavePage.getCurrentUrl());
        System.out.println("[Leave Debug] Page Text: " + applyLeavePage.getPageText());
        System.out.println("[Leave Debug] DOM Diagnostics: " + applyLeavePage.getDomDiagnostics());
        LOGGER.info("Apply Leave URL: {}", applyLeavePage.getCurrentUrl());
        LOGGER.info("Apply Leave DOM diagnostics: {}", applyLeavePage.getDomDiagnostics());
        writeLeavePageSource(applyLeavePage.getPageSource());
        ExtentReportManager.logInfo("Apply Leave page diagnostic text: " + applyLeavePage.getPageText());
        String leaveType = applyLeavePage.applyLeave(leaveDate, comment);
        LOGGER.info("Leave type selected for apply flow: {}", leaveType);
        ExtentReportManager.logPass("Leave applied successfully. Type: " + leaveType + ", Date: " + leaveDate);

        MyLeavePage myLeavePage = leavePage.openMyLeave();
        Assert.assertTrue(myLeavePage.isLoaded(), "My Leave page should be visible.");
        myLeavePage.searchLeaveByDate(leaveDate);
        LOGGER.info("Searched My Leave using date: {}", leaveDate);
        Assert.assertTrue(myLeavePage.isLeaveRequestPresent(leaveType),
                "Applied leave request should appear in My Leave.");
        ExtentReportManager.logPass("Applied leave request is visible in My Leave.");

        myLeavePage.cancelFirstLeaveRequest();
        LOGGER.info("Triggered cancel action for first leave request.");
        ExtentReportManager.logPass("Leave request cancel action completed successfully.");

        myLeavePage.searchLeaveByDate(leaveDate);
        Assert.assertTrue(myLeavePage.isCancelledRequestPresent(leaveType) || !myLeavePage.isLeaveRequestPresent(leaveType),
                "Cancelled leave request should no longer remain in pending state.");
        LOGGER.info("Validated leave cancellation state for type {} on date {}", leaveType, leaveDate);
        ExtentReportManager.logPass("Leave request cancellation verified successfully.");
    }

    private LocalDate getNextWorkingDay() {
        LocalDate date = LocalDate.now().plusDays(1);
        while (date.getDayOfWeek() == DayOfWeek.SATURDAY || date.getDayOfWeek() == DayOfWeek.SUNDAY) {
            date = date.plusDays(1);
        }
        return date;
    }

    private void writeLeavePageSource(String pageSource) {
        try {
            Files.writeString(Path.of("target", "leave-apply-page-source.html"), pageSource);
            LOGGER.info("Saved Apply Leave page source to target/leave-apply-page-source.html");
        } catch (IOException exception) {
            System.out.println("[Leave Debug] Could not write page source: " + exception.getMessage());
            LOGGER.error("Could not write Apply Leave page source.", exception);
        }
    }

    private void writeAddEntitlementPageSource(String pageSource) {
        try {
            Files.writeString(Path.of("target", "leave-add-entitlement-page-source.html"), pageSource);
            LOGGER.info("Saved Add Entitlement page source to target/leave-add-entitlement-page-source.html");
        } catch (IOException exception) {
            System.out.println("[Leave Debug] Could not write add entitlement page source: " + exception.getMessage());
            LOGGER.error("Could not write Add Entitlement page source.", exception);
        }
    }
}
