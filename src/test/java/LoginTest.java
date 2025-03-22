import api.Endpoints;
import api.UserClient;
import api.UserCredentials;
import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.Locators;
import pageobjects.LoginPage;
import pageobjects.WebDriverFactory;

import java.time.Duration;
import java.util.UUID;

public class LoginTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;

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

        UserCredentials user = new UserCredentials(email, password, "LoginTestUser");

        Response create = UserClient.createUser(user);
        Assert.assertEquals(200, create.getStatusCode());

        Response login = UserClient.loginUser(new UserCredentials(email, password, null));
        Assert.assertEquals(200, login.getStatusCode());
        accessToken = login.jsonPath().getString("accessToken");
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken).then().statusCode(202);
        }
        WebDriverFactory.closeDriver(driver);
    }

    @Test
    @DisplayName("Вход через главную страницу")
    @Description("Проверка входа через главную страницу")
    public void testLoginFromHomePage() {
        driver.get(Endpoints.LOGIN_URL);
        performLogin();
        verifyLoginSuccess();
    }

    private void performLogin() {
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        WebElement loginBtn = wait.until(ExpectedConditions.elementToBeClickable(Locators.LOGIN_BUTTON));
        loginBtn.click();
    }

    private void verifyLoginSuccess() {
        Assert.assertFalse(driver.findElements(Locators.ERROR_MESSAGE).size() > 0);
        Assert.assertTrue(driver.findElements(Locators.PERSONAL_ACCOUNT_BUTTON).size() > 0);
    }
}
