package com.imran.automation.pages.pim;

import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class EmployeeListPage extends BasePage {

    private static final By LOADING_SPINNER = By.cssSelector(".oxd-loading-spinner");
    private static final By NO_RECORDS_MESSAGE = By.xpath("//span[contains(normalize-space(),'No Records Found')]");

    @FindBy(xpath = "//h5[normalize-space()='Employee Information']")
    private WebElement employeeInformationHeader;

    @FindBy(xpath = "(//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input)[1]")
    private WebElement employeeIdSearchInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(css = ".oxd-table-card")
    private List<WebElement> resultRows;

    public EmployeeListPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(employeeInformationHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void searchByEmployeeId(String employeeId) {
        waitForLoaderToDisappear();
        type(employeeIdSearchInput, employeeId);
        click(searchButton);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".oxd-table-card")),
                ExpectedConditions.visibilityOfElementLocated(NO_RECORDS_MESSAGE)
        ));
    }

    public boolean isEmployeePresent(String employeeId, String firstName, String lastName) {
        waitForLoaderToDisappear();
        if (driver.findElements(NO_RECORDS_MESSAGE).size() > 0 || resultRows.isEmpty()) {
            return false;
        }

        for (WebElement row : resultRows) {
            String rowText = row.getText();
            if (rowText.contains(employeeId)
                    && rowText.contains(firstName)
                    && rowText.contains(lastName)) {
                return true;
            }
        }

        return false;
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }
}
