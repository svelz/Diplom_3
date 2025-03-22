package pageobjects;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class AccountPage {
    private final WebDriver driver;
    private final By logoutButton = By.xpath("//button[text()='Выйти']");
    public AccountPage(WebDriver driver) {
        this.driver = driver;
    }

    @Step("Нажать кнопку выхода")
    public void clickLogoutButton() {
        driver.findElement(logoutButton).click();
    }
}
