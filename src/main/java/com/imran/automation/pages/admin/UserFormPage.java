package com.imran.automation.pages.admin;

import com.imran.automation.models.AdminUserData;
import com.imran.automation.models.EmployeeName;
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
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserFormPage extends BasePage {

    private static final By LOADING_SPINNER = By.cssSelector(".oxd-form-loader");
    private static final By SAVE_SUCCESS_TOAST = By.cssSelector(".oxd-toast");
    private static final By DROPDOWN_OPTIONS = By.cssSelector(".oxd-select-dropdown .oxd-select-option");
    private static final By AUTOCOMPLETE_OPTIONS = By.cssSelector(".oxd-autocomplete-dropdown .oxd-autocomplete-option");
    private static final By FIELD_ERROR_MESSAGES = By.cssSelector(".oxd-input-field-error-message");
    private static final By USERNAME_FIELD_ERROR = By.xpath("(//label[normalize-space()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//span[contains(@class,'oxd-input-field-error-message')])[1]");
    private static final Logger LOGGER = LogManager.getLogger(UserFormPage.class);

    @FindBy(xpath = "//h6[normalize-space()='Add User' or normalize-space()='Edit User']")
    private WebElement userFormHeader;

    @FindBy(xpath = "(//label[normalize-space()='User Role']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text-input')])[1]")
    private WebElement userRoleDropdown;

    @FindBy(xpath = "//input[@placeholder='Type for hints...']")
    private WebElement employeeNameInput;

    @FindBy(xpath = "(//label[normalize-space()='Status']/ancestor::div[contains(@class,'oxd-input-group')]//div[contains(@class,'oxd-select-text-input')])[1]")
    private WebElement statusDropdown;

    @FindBy(xpath = "(//label[normalize-space()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input)[1]")
    private WebElement usernameInput;

    @FindBy(xpath = "//label[normalize-space()='Password']/ancestor::div[contains(@class,'oxd-input-group')]//input")
    private WebElement passwordInput;

    @FindBy(xpath = "//label[normalize-space()='Confirm Password']/ancestor::div[contains(@class,'oxd-input-group')]//input")
    private WebElement confirmPasswordInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement saveButton;

    public UserFormPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(userFormHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void addUser(EmployeeName employeeName, AdminUserData userData) {
        waitForLoaderToDisappear();
        populateNewUserForm(employeeName, userData);
        submitForm();
        waitForSaveCompletion();
    }

    public void addUserExpectingValidation(EmployeeName employeeName, AdminUserData userData) {
        waitForLoaderToDisappear();
        populateNewUserForm(employeeName, userData);
        submitForm();
        waitForValidationMessages();
        LOGGER.info("Validation messages after Admin user submission: {}", getValidationMessages());
    }

    public void updateUser(AdminUserData updatedUserData) {
        waitForLoaderToDisappear();
        selectDropdownOption(userRoleDropdown, updatedUserData.getUserRole());
        selectDropdownOption(statusDropdown, updatedUserData.getStatus());
        type(usernameInput, updatedUserData.getUsername());
        submitForm();
        waitForSaveCompletion();
    }

    public void submitEmptyFormExpectingRequiredValidation() {
        waitForLoaderToDisappear();
        submitForm();
        waitForValidationMessages();
    }

    public String getCurrentUsername() {
        waitForLoaderToDisappear();
        return getInputValue(usernameInput);
    }

    public boolean hasValidationMessage(String expectedMessage) {
        return getValidationMessages().stream()
                .anyMatch(message -> message.equalsIgnoreCase(expectedMessage));
    }

    public boolean hasValidationMessageContaining(String expectedText) {
        String normalizedExpectedText = expectedText == null ? "" : expectedText.trim().toLowerCase();
        return getValidationMessages().stream()
                .map(message -> message.toLowerCase())
                .anyMatch(message -> message.contains(normalizedExpectedText));
    }

    public int getValidationMessageCount() {
        return getValidationMessages().size();
    }

    public boolean waitForUsernameValidationContaining(String expectedText) {
        String normalizedExpectedText = expectedText == null ? "" : expectedText.trim().toLowerCase();
        try {
            return wait.until(driver -> {
                List<WebElement> errorElements = driver.findElements(USERNAME_FIELD_ERROR);
                if (errorElements.isEmpty()) {
                    return false;
                }
                String message = errorElements.get(0).getText().trim().toLowerCase();
                return !message.isBlank() && message.contains(normalizedExpectedText);
            });
        } catch (TimeoutException exception) {
            LOGGER.warn("Username validation did not contain '{}' within the wait time. Current messages: {}",
                    expectedText, getValidationMessages());
            return false;
        }
    }

    public List<String> getValidationMessages() {
        waitForLoaderToDisappear();
        return driver.findElements(FIELD_ERROR_MESSAGES)
                .stream()
                .map(element -> element.getText().trim())
                .filter(text -> !text.isBlank())
                .collect(Collectors.toCollection(ArrayList::new));
    }

    private void populateNewUserForm(EmployeeName employeeName, AdminUserData userData) {
        selectDropdownOption(userRoleDropdown, userData.getUserRole());
        selectEmployeeName(employeeName);
        selectDropdownOption(statusDropdown, userData.getStatus());
        type(usernameInput, userData.getUsername());
        type(passwordInput, userData.getPassword());
        type(confirmPasswordInput, userData.getPassword());
    }

    private void submitForm() {
        clickUsingJs(saveButton);
    }

    private void selectDropdownOption(WebElement dropdown, String optionText) {
        waitForLoaderToDisappear();
        click(dropdown);
        List<WebElement> options = wait.until(ExpectedConditions.visibilityOfAllElementsLocatedBy(DROPDOWN_OPTIONS));
        for (WebElement option : options) {
            if (option.getText().trim().equalsIgnoreCase(optionText)) {
                option.click();
                return;
            }
        }
        throw new IllegalStateException("Unable to locate dropdown option: " + optionText);
    }

    private void selectEmployeeName(EmployeeName employeeName) {
        waitForLoaderToDisappear();
        String employeeSearchText = employeeName.getFirstName();
        type(employeeNameInput, employeeSearchText);

        List<WebElement> options = wait.until(driver -> {
            List<WebElement> autocompleteOptions = driver.findElements(AUTOCOMPLETE_OPTIONS);
            if (autocompleteOptions.isEmpty()) {
                return null;
            }

            boolean onlySearchingState = autocompleteOptions.stream()
                    .map(option -> option.getText().trim())
                    .allMatch(text -> text.isBlank() || "Searching....".equalsIgnoreCase(text));
            return onlySearchingState ? null : autocompleteOptions;
        });

        List<String> optionTexts = options.stream()
                .map(option -> option.getText().trim())
                .collect(Collectors.toList());
        LOGGER.info("Employee autocomplete options displayed for search text '{}': {}", employeeSearchText, optionTexts);

        for (WebElement option : options) {
            String optionText = option.getText().trim();
            if (optionText.contains(employeeName.getFirstName()) && optionText.contains(employeeName.getLastName())) {
                click(option);
                wait.until(driver -> {
                    String currentValue = employeeNameInput.getAttribute("value");
                    return currentValue != null && currentValue.contains(employeeName.getFirstName());
                });
                return;
            }
        }

        LOGGER.warn("Exact employee autocomplete match was not found. Selecting the first available option instead.");
        click(options.get(0));
        waitForLoaderToDisappear();
        wait.until(driver -> {
            String currentValue = employeeNameInput.getAttribute("value");
            return currentValue != null && !currentValue.isBlank();
        });
    }

    private void waitForSaveCompletion() {
        waitForLoaderToDisappear();
        try {
            wait.until(ExpectedConditions.invisibilityOfElementLocated(SAVE_SUCCESS_TOAST));
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h5[normalize-space()='System Users']")));
        } catch (TimeoutException exception) {
            logFormDiagnostics();
            throw exception;
        }
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }

    private void waitForValidationMessages() {
        wait.until(driver -> !driver.findElements(FIELD_ERROR_MESSAGES).isEmpty());
    }

    private void logFormDiagnostics() {
        List<String> fieldErrors = driver.findElements(FIELD_ERROR_MESSAGES)
                .stream()
                .map(element -> element.getText().trim())
                .filter(text -> !text.isBlank())
                .collect(Collectors.toList());

        LOGGER.error(
                "Add/Edit User form did not save successfully. URL: {} | Selected role: {} | Employee field: {} | Selected status: {} | Username: {} | Field errors: {}",
                driver.getCurrentUrl(),
                userRoleDropdown.getText().trim(),
                employeeNameInput.getAttribute("value"),
                statusDropdown.getText().trim(),
                usernameInput.getAttribute("value"),
                fieldErrors
        );

        try {
            Path outputPath = Path.of("target", "admin-user-form-page-source.html");
            Files.writeString(outputPath, driver.getPageSource());
            LOGGER.error("Captured admin user form page source at {}", outputPath.toAbsolutePath());
        } catch (IOException ioException) {
            LOGGER.error("Unable to write admin user form page source for diagnostics.", ioException);
        }
    }
}
