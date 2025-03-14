import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.LoginPage;
import pageobjects.WebDriverFactory;
import java.time.Duration;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private WebDriverWait wait;

    private final String email = "testirovanieui@yandex.ru";
    private final String password = "password";

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));  // Увеличил время ожидания
        loginPage = new LoginPage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Вход через главную страницу")
    @Description("Проверка входа по кнопке 'Войти в аккаунт' на главной странице")
    public void testLoginFromHomePage() {
        driver.get("https://stellarburgers.nomoreparties.site/login");
        performLogin();
        verifyLoginSuccess("Вход через главную страницу");
    }

    @Test
    @DisplayName("Вход через личный кабинет")
    @Description("Проверка входа через кнопку 'Личный кабинет'")
    public void testLoginFromPersonalAccount() {
        driver.get("https://stellarburgers.nomoreparties.site/");
        WebElement accountButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/account')]")));
        accountButton.click();
        performLogin();
        verifyLoginSuccess("Вход через личный кабинет");
    }

    @Test
    @DisplayName("Вход через кнопку 'Войти' в форме регистрации")
    @Description("Проверка входа по кнопке 'Войти' на странице регистрации")
    public void testLoginFromRegisterPage() {
        driver.get("https://stellarburgers.nomoreparties.site/register");
        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p/a[contains(text(), 'Войти')]")));
        loginLink.click();
        performLogin();
        verifyLoginSuccess("Вход через форму регистрации");
    }

    @Test
    @DisplayName("Вход через кнопку 'Войти' в форме восстановления пароля")
    @Description("Проверка входа по кнопке 'Войти' на странице восстановления пароля")
    public void testLoginFromForgotPasswordPage() {
        driver.get("https://stellarburgers.nomoreparties.site/forgot-password");
        WebElement loginLink = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//p/a[contains(text(), 'Войти')]")));
        loginLink.click();
        performLogin();
        verifyLoginSuccess("Вход через форму восстановления пароля");
    }

    private void performLogin() {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Войти']")));
        loginButton.click();
    }

    private void verifyLoginSuccess(String testName) {
        try {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//a[contains(@href, '/account')]")));
            System.out.println("Тест пройден: " + testName);
        } catch (TimeoutException e) {
            System.out.println("Тест провален: " + testName);
            System.out.println("Текущий URL: " + driver.getCurrentUrl());
            throw e;
        }
    }
}
