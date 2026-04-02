package com.imran.automation.pages.leave;

import com.imran.automation.constants.FrameworkConstants;
import com.imran.automation.pages.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.time.Instant;
import java.time.format.DateTimeFormatter;

public class ApplyLeavePage extends BasePage {

    private static final Logger LOGGER = LogManager.getLogger(ApplyLeavePage.class);
    private static final By LOADING_SPINNER = By.cssSelector(".oxd-loading-spinner");
    private static final By APPLY_LEAVE_HEADER = By.xpath("//h6[normalize-space()='Apply Leave']");
    private static final By FROM_DATE_INPUT = By.xpath("(//input[@placeholder='yyyy-dd-mm'])[1]");
    private static final By TO_DATE_INPUT = By.xpath("(//input[@placeholder='yyyy-dd-mm'])[2]");
    private static final By COMMENTS_TEXTAREA = By.tagName("textarea");
    private static final By APPLY_BUTTON = By.xpath("//button[@type='submit' and normalize-space()='Apply']");
    private static final By TOAST_TITLE = By.xpath("//p[contains(@class,'oxd-text--toast-title')]");
    private static final By PAGE_BODY = By.tagName("body");
    private static final DateTimeFormatter ORANGE_HRM_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-dd-MM");

    public ApplyLeavePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            waitForLoaderToDisappear();
            return isVisible(APPLY_LEAVE_HEADER);
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public String applyLeave(LocalDate leaveDate, String comment) {
        LOGGER.info("Applying leave. Date: {}, comment: {}", leaveDate, comment);
        String leaveType = selectFirstAvailableLeaveType();

        String formattedDate = leaveDate.format(ORANGE_HRM_DATE_FORMAT);
        setValueUsingJs(FROM_DATE_INPUT, formattedDate);
        setValueUsingJs(TO_DATE_INPUT, formattedDate);
        type(wait.until(ExpectedConditions.visibilityOfElementLocated(COMMENTS_TEXTAREA)), comment);
        clickUsingJs(APPLY_BUTTON);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.visibilityOfElementLocated(TOAST_TITLE));
        LOGGER.info("Leave apply flow completed with leave type: {}", leaveType);
        return leaveType;
    }

    public String getPageText() {
        return getText(PAGE_BODY);
    }

    public String getPageSource() {
        return driver.getPageSource();
    }

    public String getDomDiagnostics() {
        Object diagnostics = executeScript(
                "return JSON.stringify({"
                        + "readyState: document.readyState,"
                        + "bodyTextIncludesApplyLeave: document.body.innerText.includes('Apply Leave'),"
                        + "selectWrapperCount: document.querySelectorAll('.oxd-select-wrapper').length,"
                        + "selectTextInputCount: document.querySelectorAll('.oxd-select-text-input').length,"
                        + "dateInputCount: document.querySelectorAll('input[placeholder=\"yyyy-dd-mm\"]').length,"
                        + "iframeCount: document.querySelectorAll('iframe').length"
                        + "});"
        );
        return diagnostics == null ? "{}" : diagnostics.toString();
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }

    private String selectFirstAvailableLeaveType() {
        waitForScriptCondition(
                "return document.querySelectorAll('.oxd-select-text-input').length > 0;",
                "Leave type dropdown was not available. Diagnostics: " + getDomDiagnostics()
        );
        LOGGER.info("Leave type dropdown is present. Opening dropdown now.");
        executeScript(
                "const activeDropdown = document.querySelector('.oxd-select-text--active');"
                        + "const input = document.querySelector('.oxd-select-text-input');"
                        + "if (activeDropdown) {"
                        + "  activeDropdown.dispatchEvent(new MouseEvent('mousedown', { bubbles: true }));"
                        + "  activeDropdown.dispatchEvent(new MouseEvent('mouseup', { bubbles: true }));"
                        + "  activeDropdown.click();"
                        + "}"
                        + "if (input) {"
                        + "  input.focus();"
                        + "  input.dispatchEvent(new KeyboardEvent('keydown', { key: 'ArrowDown', bubbles: true }));"
                        + "  input.dispatchEvent(new KeyboardEvent('keyup', { key: 'ArrowDown', bubbles: true }));"
                        + "}"
        );
        waitForScriptCondition(
                "return document.querySelectorAll('[role=\"listbox\"] [role=\"option\"]').length > 0;",
                "Leave type options did not appear after opening the dropdown."
        );
        LOGGER.info("Leave type options are visible. Selecting first available option.");

        Object selectedText = executeScript(
                "const options = [...document.querySelectorAll('[role=\"listbox\"] [role=\"option\"]')];"
                        + "const selectedOption = options.find(option => option.innerText.trim() !== '-- Select --');"
                        + "if (!selectedOption) { return 'MISSING_OPTION'; }"
                        + "const label = selectedOption.innerText.trim();"
                        + "selectedOption.click();"
                        + "return label;"
        );

        String leaveType = selectedText == null ? "" : selectedText.toString().trim();
        if (leaveType.isBlank() || "MISSING_OPTION".equals(leaveType)) {
            LOGGER.error("Could not select a leave type. Script result: {}", leaveType);
            throw new IllegalStateException("Could not select a leave type. Result: " + leaveType);
        }
        LOGGER.info("Selected leave type: {}", leaveType);
        return leaveType;
    }

    private boolean getBooleanResult(String script) {
        Object result = executeScript(script);
        return Boolean.parseBoolean(String.valueOf(result));
    }

    private void waitForScriptCondition(String script, String failureMessage) {
        Instant timeoutAt = Instant.now().plusSeconds(FrameworkConstants.EXPLICIT_WAIT_SECONDS);
        while (Instant.now().isBefore(timeoutAt)) {
            if (getBooleanResult(script)) {
                return;
            }

            try {
                Thread.sleep(500);
            } catch (InterruptedException exception) {
                Thread.currentThread().interrupt();
                throw new IllegalStateException("Interrupted while waiting for Leave page condition.", exception);
            }
        }

        LOGGER.error("Script wait failed. Message: {}", failureMessage);
        throw new IllegalStateException(failureMessage);
    }
}
