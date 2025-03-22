package pageobjects;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.openqa.selenium.support.FindBy;
import org.openqa.selenium.support.PageFactory;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import java.time.Duration;

public class ConstructorPage {
    private final WebDriver driver;
    private final WebDriverWait wait;

    @FindBy(xpath = "//span[text()='Булки']")
    private WebElement bunsTab;

    @FindBy(xpath = "//span[text()='Соусы']")
    private WebElement saucesTab;

    @FindBy(xpath = "//span[text()='Начинки']")
    private WebElement fillingsTab;
    public static final By bunsActiveTab = By.xpath("//div[contains(@class, 'tab_tab_type_current') and span[text()='Булки']]");
    public static final By saucesActiveTab = By.xpath("//div[contains(@class, 'tab_tab_type_current') and span[text()='Соусы']]");
    public static final By fillingsActiveTab = By.xpath("//div[contains(@class, 'tab_tab_type_current') and span[text()='Начинки']]");

    public ConstructorPage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        PageFactory.initElements(driver, this);
    }

    @Step("Переход в раздел Булки")
    public void clickBuns() {
        wait.until(ExpectedConditions.elementToBeClickable(bunsTab)).click();
    }

    @Step("Переход в раздел Соусы")
    public void clickSauces() {
        wait.until(ExpectedConditions.elementToBeClickable(saucesTab)).click();
    }

    @Step("Переход в раздел Начинки")
    public void clickFillings() {
        wait.until(ExpectedConditions.elementToBeClickable(fillingsTab)).click();
    }
}
