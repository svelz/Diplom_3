import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.LoginPage;
import pageobjects.AccountPage;
import pageobjects.WebDriverFactory;
import api.Endpoints;
import pageobjects.Locators;
import java.time.Duration;

public class LogoutTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        driver.get(Endpoints.LOGIN_URL);
        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        loginPage = new LoginPage(driver);
        accountPage = new AccountPage(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.EMAIL_FIELD));

        loginPage.enterEmail("testirovanieui@yandex.ru");
        loginPage.enterPassword("password");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(Locators.LOGIN_BUTTON));
        loginButton.click();

        boolean isLoggedIn = wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(Locators.PERSONAL_ACCOUNT_BUTTON),
                ExpectedConditions.presenceOfElementLocated(Locators.LOGIN_BUTTON)
        )) != null;

        System.out.println("Текущий URL после входа: " + driver.getCurrentUrl());
        Assert.assertTrue("Вход не выполнен!", isLoggedIn);
    }

    @After
    public void tearDown() {
        Endpoints.deleteUser("testirovanieui@yandex.ru");
        WebDriverFactory.closeDriver(driver);
    }

    @Test
    @DisplayName("Выход из аккаунта")
    @Description("Проверка выхода пользователя из аккаунта")
    public void testLogout() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.PERSONAL_ACCOUNT_BUTTON)
        );
        personalAccountButton.click();

        WebElement logoutButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.LOGOUT_BUTTON)
        );
        logoutButton.click();

        boolean isLoggedOut = wait.until(ExpectedConditions.presenceOfElementLocated(Locators.LOGIN_HEADER)) != null;
        System.out.println("Текущий URL после выхода: " + driver.getCurrentUrl());
        Assert.assertTrue("Выход не выполнен!", isLoggedOut);
    }
}
