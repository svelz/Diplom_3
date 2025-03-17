import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.RegisterPage;
import pageobjects.WebDriverFactory;
import api.Endpoints;

import java.util.UUID;

import static io.restassured.RestAssured.given;

public class RegisterTest {
    private WebDriver driver;
    private RegisterPage registerPage;
    private String uniqueEmail;
    private String password = "Test@1234";
    private String accessToken;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        driver.get(Endpoints.REGISTER_URL);
        uniqueEmail = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        System.out.println("Используем email: " + uniqueEmail);

        registerPage = new RegisterPage(driver);
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            Response deleteResponse = given()
                    .header("Authorization", accessToken)
                    .delete(Endpoints.API_USER_DELETE);

            Assert.assertEquals("Ошибка при удалении пользователя!", 202, deleteResponse.statusCode());
        }

        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    @Description("Проверка успешной регистрации нового пользователя с уникальным email")
    public void testSuccessfulRegistration() {
        registerPage.enterName("Тестовый Пользователь");
        registerPage.enterEmail(uniqueEmail);
        registerPage.enterPassword(password);
        registerPage.clickRegisterButton();

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));

        boolean isLoginPage = wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("login"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[text()='Вход']"))
        ));

        System.out.println("Фактический URL после регистрации: " + driver.getCurrentUrl());

        Assert.assertTrue("Регистрация не удалась! Ожидался редирект на страницу логина.", isLoginPage);

        Response loginResponse = given()
                .header("Content-Type", "application/json")
                .body("{ \"email\": \"" + uniqueEmail + "\", \"password\": \"" + password + "\" }")
                .post(Endpoints.API_USER_LOGIN)
                .then()
                .extract()
                .response();

        Assert.assertEquals("Ошибка при авторизации пользователя", 200, loginResponse.statusCode());
        accessToken = loginResponse.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Ошибка при вводе короткого пароля")
    @Description("Проверка обработки ошибки при регистрации с коротким паролем")
    public void testRegistrationWithShortPassword() {
        registerPage.enterName("shortpassuser");
        registerPage.enterEmail("shortpass@example.com");
        registerPage.enterPassword("12345");
        registerPage.clickRegisterButton();

        String errorMessage = registerPage.getPasswordErrorMessage();
        Assert.assertEquals("Некорректное сообщение об ошибке!", "Некорректный пароль", errorMessage);
    }
}
