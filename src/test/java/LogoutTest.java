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
import pageobjects.AccountPage;
import pageobjects.Locators;
import pageobjects.LoginPage;
import pageobjects.WebDriverFactory;

import java.time.Duration;
import java.util.UUID;

public class LogoutTest {
    private WebDriver driver;
    private WebDriverWait wait;
    private LoginPage loginPage;
    private AccountPage accountPage;

    private String email;
    private String password;
    private String accessToken;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        loginPage = new LoginPage(driver);
        accountPage = new AccountPage(driver);

        email = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        password = "Test@1234";

        UserCredentials user = new UserCredentials(email, password, "LogoutTestUser");
        Assert.assertEquals(200, UserClient.createUser(user).getStatusCode());

        Response login = UserClient.loginUser(new UserCredentials(email, password, null));
        accessToken = login.jsonPath().getString("accessToken");

        driver.get(Endpoints.LOGIN_URL);
        loginPage.enterEmail(email);
        loginPage.enterPassword(password);
        loginPage.clickLoginButton();

        wait.until(ExpectedConditions.presenceOfElementLocated(Locators.PERSONAL_ACCOUNT_BUTTON));
    }

    @After
    public void tearDown() {
        if (accessToken != null) {
            UserClient.deleteUser(accessToken).then().statusCode(202);
        }
        WebDriverFactory.closeDriver(driver);
    }

    @Test
    @DisplayName("Выход из аккаунта")
    @Description("Проверка выхода пользователя")
    public void testLogout() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.PERSONAL_ACCOUNT_BUTTON));
        personalAccountButton.click();

        WebElement logoutButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.LOGOUT_BUTTON));
        logoutButton.click();

        WebElement loginHeader = wait.until(
                ExpectedConditions.presenceOfElementLocated(Locators.LOGIN_HEADER));
        Assert.assertTrue("Выход не выполнен!", loginHeader.isDisplayed());
    }
}
