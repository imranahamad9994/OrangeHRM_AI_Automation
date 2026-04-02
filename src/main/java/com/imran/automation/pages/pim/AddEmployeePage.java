package com.imran.automation.pages.pim;

import com.imran.automation.pages.base.BasePage;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

public class AddEmployeePage extends BasePage {

    private static final By LOADING_SPINNER = By.cssSelector(".oxd-form-loader");
    private static final Logger LOGGER = LogManager.getLogger(AddEmployeePage.class);

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
        waitForLoaderToDisappear();
        type(firstNameInput, firstName);
        type(middleNameInput, middleName);
        type(lastNameInput, lastName);
        String employeeId = getInputValue(employeeIdInput);
        waitForLoaderToDisappear();
        clickUsingJs(saveButton);
        waitForLoaderToDisappear();
        try {
            wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader));
        } catch (TimeoutException exception) {
            logAddEmployeeDiagnostics(employeeId);
            throw exception;
        }
        return employeeId;
    }

    public boolean isPersonalDetailsPageLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public EmployeeDetailsPage goToEmployeeDetailsPage() {
        wait.until(ExpectedConditions.visibilityOf(personalDetailsHeader));
        return new EmployeeDetailsPage(driver);
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }

    private void logAddEmployeeDiagnostics(String employeeId) {
        LOGGER.error(
                "Add Employee did not navigate to Personal Details. URL: {} | Employee ID: {} | First name: {} | Middle name: {} | Last name: {}",
                driver.getCurrentUrl(),
                employeeId,
                firstNameInput.getAttribute("value"),
                middleNameInput.getAttribute("value"),
                lastNameInput.getAttribute("value")
        );

        try {
            Path outputPath = Path.of("target", "add-employee-page-source.html");
            Files.writeString(outputPath, driver.getPageSource());
            LOGGER.error("Captured add employee page source at {}", outputPath.toAbsolutePath());
        } catch (IOException ioException) {
            LOGGER.error("Unable to write add employee page source for diagnostics.", ioException);
        }
    }
}
