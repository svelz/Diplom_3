import api.Endpoints;
import api.UserClient;
import api.UserCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.Locators;
import pageobjects.RegisterPage;
import pageobjects.WebDriverFactory;
import org.openqa.selenium.WebDriver;

import java.time.Duration;
import java.util.UUID;

public class RegisterTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private RegisterPage registerPage;
    private String email;
    private final String password = "Test@1234";
    private String accessToken;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        driver.get(Endpoints.REGISTER_URL);
        registerPage = new RegisterPage(driver);
        email = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken).then().statusCode(202);
        }
        WebDriverFactory.closeDriver(driver);
    }

    @Test
    @DisplayName("Успешная регистрация нового пользователя")
    @Description("Регистрация нового пользователя через UI")
    public void testSuccessfulRegistration() {
        registerPage.enterName("Test Name");
        registerPage.enterEmail(email);
        registerPage.enterPassword(password);
        registerPage.clickRegisterButton();

        boolean redirected = wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("login"),
                ExpectedConditions.presenceOfElementLocated(Locators.LOGIN_HEADER)
        )) != null;

        Assert.assertTrue("Не произошло перенаправление на логин", redirected);

        Response login = UserClient.loginUser(new UserCredentials(email, password, null));
        Assert.assertEquals(200, login.getStatusCode());
        accessToken = login.jsonPath().getString("accessToken");
    }

    @Test
    @DisplayName("Ошибка при коротком пароле")
    @Description("Регистрация с некорректным паролем")
    public void testRegistrationWithShortPassword() {
        registerPage.enterName("Short Pass");
        registerPage.enterEmail("short@example.com");
        registerPage.enterPassword("123");
        registerPage.clickRegisterButton();

        String error = registerPage.getPasswordErrorMessage();
        Assert.assertEquals("Некорректный пароль", error);
    }
}
