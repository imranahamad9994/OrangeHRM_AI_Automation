package com.imran.automation.pages.practice;

import com.imran.automation.models.PracticeFormData;
import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;

public class AutomationPracticeFormPage extends BasePage {

    private static final DateTimeFormatter ISO_DATE_FORMAT = DateTimeFormatter.ISO_LOCAL_DATE;
    private static final DateTimeFormatter DISPLAY_DATE_FORMAT = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    @FindBy(id = "name")
    private WebElement nameInput;

    @FindBy(id = "email")
    private WebElement emailInput;

    @FindBy(id = "phone")
    private WebElement phoneInput;

    @FindBy(id = "textarea")
    private WebElement addressTextArea;

    @FindBy(id = "country")
    private WebElement countryDropdown;

    @FindBy(id = "colors")
    private WebElement colorsDropdown;

    @FindBy(id = "animals")
    private WebElement animalsDropdown;

    @FindBy(id = "datepicker")
    private WebElement datePickerOneInput;

    @FindBy(id = "txtDate")
    private WebElement datePickerTwoInput;

    @FindBy(id = "start-date")
    private WebElement startDateInput;

    @FindBy(id = "end-date")
    private WebElement endDateInput;

    @FindBy(xpath = "(//button[@type='submit'])[1]")
    private WebElement submitButton;

    public AutomationPracticeFormPage(WebDriver driver) {
        super(driver);
    }

    public AutomationPracticeFormPage open(String url) {
        driver.get(url);
        wait.until(ExpectedConditions.visibilityOf(nameInput));
        return this;
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(nameInput)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public void fillForm(PracticeFormData formData) {
        type(nameInput, formData.getName());
        type(emailInput, formData.getEmail());
        type(phoneInput, formData.getPhone());
        type(addressTextArea, formData.getAddress());

        selectGender(formData.getGender());
        selectDays(formData.getDays());
        selectByVisibleText(countryDropdown, formData.getCountry());
        selectMultipleByVisibleText(colorsDropdown, formData.getColors());
        selectMultipleByVisibleText(animalsDropdown, formData.getAnimals());

        enterDate(datePickerOneInput, formData.getDatePicker1());
        enterDate(datePickerTwoInput, formData.getDatePicker2());
        enterDate(startDateInput, formData.getStartDate());
        enterDate(endDateInput, formData.getEndDate());
    }

    public void submitForm() {
        clickUsingJs(submitButton);
    }

    public String getEnteredName() {
        return getInputValue(nameInput);
    }

    public String getEnteredEmail() {
        return getInputValue(emailInput);
    }

    public String getEnteredPhone() {
        return getInputValue(phoneInput);
    }

    public String getEnteredAddress() {
        return getInputValue(addressTextArea);
    }

    public boolean isGenderSelected(String gender) {
        return wait.until(ExpectedConditions.elementToBeClickable(By.id(gender.trim().toLowerCase()))).isSelected();
    }

    public boolean areDaysSelected(List<String> days) {
        return days.stream()
                .allMatch(day -> wait.until(ExpectedConditions.elementToBeClickable(By.id(day.trim().toLowerCase()))).isSelected());
    }

    public String getSelectedCountry() {
        return getSelectedOption(countryDropdown);
    }

    public List<String> getSelectedColors() {
        return getSelectedOptions(colorsDropdown);
    }

    public List<String> getSelectedAnimals() {
        return getSelectedOptions(animalsDropdown);
    }

    public String getDatePickerOneValue() {
        return getInputValue(datePickerOneInput);
    }

    public String getDatePickerTwoValue() {
        return getInputValue(datePickerTwoInput);
    }

    public String getStartDateValue() {
        return normalizeHtmlDateValue(getInputValue(startDateInput));
    }

    public String getEndDateValue() {
        return normalizeHtmlDateValue(getInputValue(endDateInput));
    }

    public boolean isSubmitButtonDisplayed() {
        return wait.until(ExpectedConditions.visibilityOf(submitButton)).isDisplayed();
    }

    public boolean isAlertPresent() {
        try {
            wait.until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public String consumeAlertText() {
        try {
            String alertText = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            return alertText;
        } catch (NoAlertPresentException exception) {
            return "";
        }
    }

    private void selectGender(String gender) {
        WebElement genderOption = wait.until(ExpectedConditions.elementToBeClickable(By.id(gender.trim().toLowerCase())));
        clickUsingJs(genderOption);
    }

    private void selectDays(List<String> days) {
        for (String day : days) {
            WebElement dayCheckbox = wait.until(ExpectedConditions.elementToBeClickable(By.id(day.trim().toLowerCase())));
            if (!dayCheckbox.isSelected()) {
                clickUsingJs(dayCheckbox);
            }
        }
    }

    private void enterDate(WebElement element, String value) {
        try {
            type(element, value);
        } catch (Exception exception) {
            setValueUsingJs(element, value);
        }
    }

    private String normalizeHtmlDateValue(String value) {
        try {
            return LocalDate.parse(value, ISO_DATE_FORMAT).format(DISPLAY_DATE_FORMAT);
        } catch (DateTimeParseException exception) {
            return value;
        }
    }
}
