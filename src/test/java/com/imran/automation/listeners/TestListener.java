package com.imran.automation.listeners;

import com.imran.automation.utils.ScreenshotUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener {

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting suite execution: " + context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentReportManager.createTest(testName);
        ExtentReportManager.getTest().info("Test execution started.");
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().pass("Test passed successfully.");
        ExtentReportManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        ExtentReportManager.getTest().fail(result.getThrowable());
        String screenshotPath = ScreenshotUtils.captureScreenshot(result.getMethod().getMethodName());
        if (screenshotPath != null) {
            ExtentReportManager.attachScreenshot(screenshotPath);
        }
        ExtentReportManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        ExtentReportManager.getTest().skip(result.getThrowable());
        ExtentReportManager.unload();
    }

    @Override
    public void onFinish(ITestContext context) {
        ExtentReportManager.flushReport();

        boolean allTestsPassed = context.getFailedTests().size() == 0
                && context.getSkippedTests().size() == 0;

        if (allTestsPassed) {
            ExtentReportManager.openReport();
        }
    }
}
