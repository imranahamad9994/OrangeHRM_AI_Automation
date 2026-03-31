package com.imran.automation.pages.pim;

import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class AddEmployeePage extends BasePage {

    @FindBy(name = "firstName")
    private WebElement firstNameInput;

    @FindBy(name = "middleName")
    private WebElement middleNameInput;

    @FindBy(name = "lastName")
    private WebElement lastNameInput;

    @FindBy(xpath = "(//label[normalize-space()='Employee Id']/ancestor::div[contains(@class,'oxd-input-group')]//input)[1]")
    private WebElement employeeIdInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement saveButton;

    @FindBy(xpath = "//h6[normalize-space()='Add Employee']")
    private WebElement addEmployeeHeader;

    @FindBy(xpath = "//h6[normalize-space()='Personal Details']")
    private WebElement personalDetailsHeader;

    public AddEmployeePage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(addEmployeeHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public String addEmployee(String firstName, String middleName, String lastName) {
        type(firstNameInput, firstName);
        type(middleNameInput, middleName);
        type(lastNameInput, lastName);
        String employeeId = getInputValue(employeeIdInput);
        click(saveButton);
        wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader));
        return employeeId;
    }

    public boolean isPersonalDetailsPageLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }
}
