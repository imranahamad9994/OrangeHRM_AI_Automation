package com.imran.automation.pages.pim;

import com.imran.automation.pages.base.BasePage;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.ui.ExpectedConditions;

public class PimPage extends BasePage {

    @FindBy(xpath = "//span[normalize-space()='PIM']")
    private WebElement pimMenu;

    @FindBy(xpath = "//h6[normalize-space()='PIM']")
    private WebElement pimHeader;

    @FindBy(xpath = "//a[normalize-space()='Add Employee']")
    private WebElement addEmployeeTab;

    @FindBy(xpath = "//a[normalize-space()='Employee List']")
    private WebElement employeeListTab;

    public PimPage(WebDriver driver) {
        super(driver);
    }

    public void openPimModule() {
        click(pimMenu);
        wait.until(ExpectedConditions.visibilityOf(pimHeader));
    }

    public AddEmployeePage openAddEmployee() {
        click(addEmployeeTab);
        return new AddEmployeePage(driver);
    }

    public EmployeeListPage openEmployeeList() {
        click(employeeListTab);
        return new EmployeeListPage(driver);
    }

    public boolean isModuleLoaded() {
        try {
            return wait.until(ExpectedConditions.visibilityOf(pimHeader)).isDisplayed();
        } catch (TimeoutException exception) {
            return false;
        }
    }
}
