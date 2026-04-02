package com.imran.automation.listeners;

import com.imran.automation.utils.ScreenshotUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.testng.IExecutionListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class TestListener implements ITestListener, IExecutionListener {

    private static final Logger LOGGER = LogManager.getLogger(TestListener.class);

    @Override
    public void onStart(ITestContext context) {
        System.out.println("Starting suite execution: " + context.getName());
        LOGGER.info("Starting TestNG context: {}", context.getName());
    }

    @Override
    public void onTestStart(ITestResult result) {
        String testName = result.getMethod().getMethodName();
        ExtentReportManager.createTest(testName);
        ExtentReportManager.getTest().info("Test execution started.");
        LOGGER.info("Test started: {}", testName);
    }

    @Override
    public void onTestSuccess(ITestResult result) {
        ExtentReportManager.getTest().pass("Test passed successfully.");
        LOGGER.info("Test passed: {}", result.getMethod().getMethodName());
        ExtentReportManager.unload();
    }

    @Override
    public void onTestFailure(ITestResult result) {
        LOGGER.error("Test failed: {}", result.getMethod().getMethodName(), result.getThrowable());
        ExtentReportManager.getTest().fail(result.getThrowable());
        String screenshotPath = ScreenshotUtils.captureScreenshot(result.getMethod().getMethodName());
        if (screenshotPath != null) {
            LOGGER.info("Attached failure screenshot for {} at {}", result.getMethod().getMethodName(), screenshotPath);
            ExtentReportManager.attachScreenshot(screenshotPath);
        } else {
            LOGGER.warn("No screenshot captured for failed test: {}", result.getMethod().getMethodName());
        }
        ExtentReportManager.unload();
    }

    @Override
    public void onTestSkipped(ITestResult result) {
        LOGGER.warn("Test skipped: {}", result.getMethod().getMethodName(), result.getThrowable());
        ExtentReportManager.getTest().skip(result.getThrowable());
        ExtentReportManager.unload();
    }

    @Override
    public void onFinish(ITestContext context) {
        LOGGER.info(
                "Finishing TestNG context: {}. Passed: {}, Failed: {}, Skipped: {}",
                context.getName(),
                context.getPassedTests().size(),
                context.getFailedTests().size(),
                context.getSkippedTests().size()
        );
        ExtentReportManager.flushReport();
    }

    @Override
    public void onExecutionFinish() {
        LOGGER.info("TestNG execution finished. Opening consolidated Extent report once.");
        ExtentReportManager.flushReport();
        ExtentReportManager.openReport();
    }
}
