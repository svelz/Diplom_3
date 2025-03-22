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

public class ProfileTest {
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

        UserCredentials user = new UserCredentials(email, password, "ProfileTestUser");
        UserClient.createUser(user);
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
    @DisplayName("Переход в личный кабинет")
    @Description("Переход по кнопке 'Личный кабинет'")
    public void testNavigateToProfile() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.PERSONAL_ACCOUNT_BUTTON));
        personalAccountButton.click();

        WebElement logoutButton = wait.until(
                ExpectedConditions.visibilityOfElementLocated(Locators.LOGOUT_BUTTON));
        Assert.assertTrue("Кнопка выхода не найдена!", logoutButton.isDisplayed());
    }
}
