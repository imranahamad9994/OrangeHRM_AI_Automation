package com.imran.automation.pages.pim;

import com.imran.automation.models.EmployeeName;
import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class EmployeeDetailsPage extends BasePage {

    private static final By LOADING_SPINNER = By.cssSelector(".oxd-form-loader");

    @FindBy(xpath = "//h6[normalize-space()='Personal Details']")
    private WebElement personalDetailsHeader;

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "middleName")
    private WebElement middleNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(xpath = "(//button[@type='submit'])[1]")
    private WebElement saveButton;

    public EmployeeDetailsPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void updateEmployeeName(EmployeeName employeeName) {
        waitForLoaderToDisappear();
        type(firstNameInput, employeeName.getFirstName());
        type(middleNameInput, employeeName.getMiddleName());
        type(lastNameInput, employeeName.getLastName());
        waitForLoaderToDisappear();
        clickUsingJs(saveButton);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader));
    }

    public boolean isUpdatedNameDisplayed(EmployeeName employeeName) {
        return getInputValue(firstNameInput).equals(employeeName.getFirstName())
                && getInputValue(middleNameInput).equals(employeeName.getMiddleName())
                && getInputValue(lastNameInput).equals(employeeName.getLastName());
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }
}
