package com.imran.automation.pages.leave;

import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class MyLeavePage extends BasePage {

    private static final By LOADING_SPINNER = By.cssSelector(".oxd-loading-spinner");
    private static final By NO_RECORDS_MESSAGE = By.xpath("//span[contains(normalize-space(),'No Records Found')]");
    private static final By LEAVE_LIST_HEADER = By.xpath("//h5[normalize-space()='Leave List']");
    private static final By FROM_DATE_INPUT = By.xpath("(//input[@placeholder='yyyy-dd-mm'])[1]");
    private static final By TO_DATE_INPUT = By.xpath("(//input[@placeholder='yyyy-dd-mm'])[2]");
    private static final By SEARCH_BUTTON = By.xpath("//button[@type='submit' and normalize-space()='Search']");
    private static final By RESULT_ROWS = By.cssSelector(".oxd-table-card");
    private static final By CONFIRM_CANCEL_BUTTON = By.xpath("//button[normalize-space()='Yes, Cancel']");
    private static final DateTimeFormatter ORANGE_HRM_DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-dd-MM");

    public MyLeavePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            waitForLoaderToDisappear();
            return isVisible(LEAVE_LIST_HEADER);
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void searchLeaveByDate(LocalDate leaveDate) {
        String formattedDate = leaveDate.format(ORANGE_HRM_DATE_FORMAT);
        waitForLoaderToDisappear();
        setValueUsingJs(FROM_DATE_INPUT, formattedDate);
        setValueUsingJs(TO_DATE_INPUT, formattedDate);
        clickUsingJs(SEARCH_BUTTON);
        waitForLoaderToDisappear();
    }

    public boolean isLeaveRequestPresent(String leaveType) {
        waitForLoaderToDisappear();
        List<WebElement> resultRows = driver.findElements(RESULT_ROWS);
        if (driver.findElements(NO_RECORDS_MESSAGE).size() > 0 || resultRows.isEmpty()) {
            return false;
        }

        for (WebElement row : resultRows) {
            String text = row.getText();
            if (text.contains(leaveType) && (text.contains("Pending Approval") || text.contains("Scheduled"))) {
                return true;
            }
        }
        return false;
    }

    public void cancelFirstLeaveRequest() {
        waitForLoaderToDisappear();
        List<WebElement> rows = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(RESULT_ROWS));
        WebElement firstRow = rows.get(0);
        WebElement cancelButton = firstRow.findElement(By.xpath(".//button[normalize-space()='Cancel']"));
        clickUsingJs(cancelButton);
        wait.until(ExpectedConditions.visibilityOfElementLocated(CONFIRM_CANCEL_BUTTON));
        click(By.xpath("//button[normalize-space()='Yes, Cancel']"));
        waitForLoaderToDisappear();
    }

    public boolean isCancelledRequestPresent(String leaveType) {
        waitForLoaderToDisappear();
        List<WebElement> resultRows = driver.findElements(RESULT_ROWS);
        if (driver.findElements(NO_RECORDS_MESSAGE).size() > 0 || resultRows.isEmpty()) {
            return false;
        }

        for (WebElement row : resultRows) {
            String text = row.getText();
            if (text.contains(leaveType) && text.contains("Cancelled")) {
                return true;
            }
        }
        return false;
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }
}
