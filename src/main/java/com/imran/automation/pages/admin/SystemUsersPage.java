package com.imran.automation.pages.admin;

import com.imran.automation.models.AdminUserData;
import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.List;

public class SystemUsersPage extends BasePage {

    private static final By LOADING_SPINNER = By.cssSelector(".oxd-loading-spinner");
    private static final By NO_RECORDS_MESSAGE = By.xpath("//span[contains(normalize-space(),'No Records Found')]");
    private static final By TOAST_MESSAGE = By.cssSelector(".oxd-toast");

    @FindBy(xpath = "//h5[normalize-space()='System Users']")
    private WebElement systemUsersHeader;

    @FindBy(xpath = "//button[normalize-space()='Add']")
    private WebElement addButton;

    @FindBy(xpath = "(//label[normalize-space()='Username']/ancestor::div[contains(@class,'oxd-input-group')]//input)[1]")
    private WebElement usernameSearchInput;

    @FindBy(xpath = "//button[@type='submit']")
    private WebElement searchButton;

    @FindBy(css = ".oxd-table-card")
    private List<WebElement> resultRows;

    @FindBy(xpath = "//button[normalize-space()='Yes, Delete']")
    private WebElement confirmDeleteButton;

    public SystemUsersPage(WebDriver driver) {
        super(driver);
    }

    public boolean isLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(systemUsersHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }

    public UserFormPage openAddUserForm() {
        waitForLoaderToDisappear();
        click(addButton);
        return new UserFormPage(driver);
    }

    public void searchByUsername(String username) {
        waitForLoaderToDisappear();
        type(usernameSearchInput, username);
        click(searchButton);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.or(
                ExpectedConditions.visibilityOfAllElementsLocatedBy(By.cssSelector(".oxd-table-card")),
                ExpectedConditions.visibilityOfElementLocated(NO_RECORDS_MESSAGE)
        ));
    }

    public boolean isUserPresent(String username, AdminUserData userData) {
        waitForLoaderToDisappear();
        if (driver.findElements(NO_RECORDS_MESSAGE).size() > 0 || resultRows.isEmpty()) {
            return false;
        }

        for (WebElement row : resultRows) {
            String rowText = row.getText();
            if (rowText.contains(username)
                    && rowText.contains(userData.getUserRole())
                    && rowText.contains(userData.getStatus())) {
                return true;
            }
        }

        return false;
    }

    public UserFormPage openFirstSearchResultForEdit() {
        waitForLoaderToDisappear();
        WebElement firstRow = wait.until(ExpectedConditions.visibilityOfAllElements(resultRows)).get(0);
        WebElement editButton = firstRow.findElement(By.xpath(".//button[i[contains(@class,'bi-pencil-fill')]]"));
        click(editButton);
        return new UserFormPage(driver);
    }

    public void deleteFirstSearchResult() {
        waitForLoaderToDisappear();
        WebElement firstRow = wait.until(ExpectedConditions.visibilityOfAllElements(resultRows)).get(0);
        WebElement deleteButton = firstRow.findElement(By.xpath(".//button[i[contains(@class,'bi-trash')]]"));
        click(deleteButton);
        click(confirmDeleteButton);
        waitForLoaderToDisappear();
        wait.until(ExpectedConditions.invisibilityOfElementLocated(TOAST_MESSAGE));
    }

    public boolean isNoRecordsFoundDisplayed() {
        waitForLoaderToDisappear();
        return driver.findElements(NO_RECORDS_MESSAGE).size() > 0;
    }

    private void waitForLoaderToDisappear() {
        wait.until(ExpectedConditions.invisibilityOfElementLocated(LOADING_SPINNER));
    }
}
