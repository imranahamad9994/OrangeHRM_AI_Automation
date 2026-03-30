package com.imran.automation.listeners;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.imran.automation.constants.FrameworkConstants;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;

public final class ExtentReportManager {

    private static final ExtentReports EXTENT_REPORTS = createExtentReports();
    private static final ThreadLocal<ExtentTest> EXTENT_TEST = new ThreadLocal<>();

    private ExtentReportManager() {
    }

    private static ExtentReports createExtentReports() {
        ExtentSparkReporter sparkReporter = new ExtentSparkReporter(FrameworkConstants.EXTENT_REPORT_PATH);
        sparkReporter.config().setReportName("OrangeHRM Automation Report");
        sparkReporter.config().setDocumentTitle("OrangeHRM Test Execution");

        ExtentReports extentReports = new ExtentReports();
        extentReports.attachReporter(sparkReporter);
        extentReports.setSystemInfo("Framework", "Selenium + TestNG + POM + Data Driven");
        extentReports.setSystemInfo("Application", "OrangeHRM Demo");
        extentReports.setSystemInfo("Tester", "Imran Ahamad");
        return extentReports;
    }

    public static void createTest(String testName) {
        EXTENT_TEST.set(EXTENT_REPORTS.createTest(testName));
    }

    public static ExtentTest getTest() {
        return EXTENT_TEST.get();
    }

    public static void attachScreenshot(String screenshotPath) {
        try {
            getTest().addScreenCaptureFromPath(screenshotPath);
        } catch (Exception exception) {
            getTest().warning("Screenshot was captured but could not be attached: " + exception.getMessage());
        }
    }

    public static void unload() {
        EXTENT_TEST.remove();
    }

    public static void flushReport() {
        EXTENT_REPORTS.flush();
    }

    public static void openReport() {
        File reportFile = new File(FrameworkConstants.EXTENT_REPORT_PATH);
        if (!reportFile.exists() || !Desktop.isDesktopSupported()) {
            return;
        }

        try {
            Desktop.getDesktop().open(reportFile);
        } catch (IOException exception) {
            System.out.println("Extent report generated but could not be opened automatically: " + exception.getMessage());
        }
    }
}
