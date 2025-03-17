import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.LoginPage;
import pageobjects.WebDriverFactory;
import api.Endpoints;
import pageobjects.Locators;

import java.time.Duration;
import java.util.UUID;

import static io.restassured.RestAssured.given;

public class LoginTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private WebDriverWait wait;
    private String email;
    private String password;
    private String accessToken;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        email = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        password = "Test@1234";

        Response response = given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\", \"name\": \"TestUser\" }")
                .post(Endpoints.API_USER_CREATE)
                .then()
                .extract()
                .response();

        Assert.assertEquals("Ошибка при создании пользователя", 200, response.statusCode());
        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"" + email + "\", \"password\": \"" + password + "\" }")
                .post(Endpoints.API_USER_LOGIN)
                .then()
                .extract()
                .response();

        Assert.assertEquals("Ошибка при авторизации пользователя", 200, loginResponse.statusCode());
        accessToken = loginResponse.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            given()
                    .header("Authorization", accessToken)
                    .delete(Endpoints.API_USER_DELETE);
        }

        WebDriverFactory.closeDriver(driver);
    }

    @Test
    @DisplayName("Вход через главную страницу")
    @Description("Проверка входа по кнопке 'Войти в аккаунт' на главной странице")
    public void testLoginFromHomePage() {
        driver.get(Endpoints.LOGIN_URL);
        performLogin();
        verifyLoginSuccess();
    }

    @Test
    @DisplayName("Вход через личный кабинет")
    @Description("Проверка входа через кнопку 'Личный кабинет'")
    public void testLoginFromPersonalAccount() {
        driver.get(Endpoints.BASE_URL);
        loginPage.clickPersonalAccountButton();
        performLogin();
        verifyLoginSuccess();
    }

    @Test
    @DisplayName("Вход через форму регистрации")
    @Description("Проверка входа по кнопке 'Войти' на странице регистрации")
    public void testLoginFromRegisterPage() {
        driver.get(Endpoints.REGISTER_URL);
        loginPage.clickRegisterLoginButton();
        performLogin();
        verifyLoginSuccess();
    }

    @Test
    @DisplayName("Вход через форму восстановления пароля")
    @Description("Проверка входа по кнопке 'Войти' на странице восстановления пароля")
    public void testLoginFromForgotPasswordPage() {
        driver.get(Endpoints.FORGOT_PASSWORD_URL);
        loginPage.clickForgotPasswordLoginButton();
        performLogin();
        verifyLoginSuccess();
    }

    private void performLogin() {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(Locators.LOGIN_BUTTON));

        try {
            loginButton.click();
        } catch (ElementClickInterceptedException e) {
            ((JavascriptExecutor) driver).executeScript("arguments[0].click();", loginButton);
        }
    }

    private void verifyLoginSuccess() {
        boolean isErrorPresent = driver.findElements(Locators.ERROR_MESSAGE).size() > 0;

        if (isErrorPresent) {
            Assert.fail("Ошибка входа! Проверьте учетные данные.");
        }

        boolean isLoggedIn = driver.findElements(Locators.PERSONAL_ACCOUNT_BUTTON).size() > 0;

        Assert.assertTrue("Вход в аккаунт не подтверждён, но ошибки нет.", isLoggedIn);
        System.out.println("Авторизация выполнена успешно!");
    }
}
