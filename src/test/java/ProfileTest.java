import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.AccountPage;
import pageobjects.LoginPage;
import pageobjects.WebDriverFactory;
import api.Endpoints;
import pageobjects.Locators;
import java.time.Duration;

public class ProfileTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        driver.get(Endpoints.LOGIN_URL);

        wait = new WebDriverWait(driver, Duration.ofSeconds(10)); // Увеличенное ожидание
        loginPage = new LoginPage(driver);
        accountPage = new AccountPage(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(Locators.EMAIL_FIELD));

        loginPage.enterEmail("testirovanieui@yandex.ru");
        loginPage.enterPassword("password");
        loginPage.clickLoginButton();

        boolean isLoggedIn = wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(Locators.PERSONAL_ACCOUNT_BUTTON),
                ExpectedConditions.presenceOfElementLocated(Locators.LOGOUT_BUTTON)
        )) != null;

        Assert.assertTrue("Вход в систему не выполнен!", isLoggedIn);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Переход в личный кабинет")
    @Description("Проверка перехода в личный кабинет")
    public void testNavigateToProfile() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.PERSONAL_ACCOUNT_BUTTON)
        );
        personalAccountButton.click();

        WebElement logoutButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                Locators.LOGOUT_BUTTON
        ));

        Assert.assertTrue("Не удалось перейти в личный кабинет!", logoutButton.isDisplayed());
    }

    @Test
    @DisplayName("Переход из личного кабинета в конструктор")
    @Description("Проверка перехода из личного кабинета в конструктор")
    public void testNavigateFromProfileToConstructor() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.PERSONAL_ACCOUNT_BUTTON)
        );
        personalAccountButton.click();

        WebElement constructorButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.CONSTRUCTOR_BUTTON)
        );
        constructorButton.click();

        boolean isConstructorPage = wait.until(ExpectedConditions.presenceOfElementLocated(
                Locators.CONSTRUCTOR_HEADER
        )) != null;

        Assert.assertTrue("Не удалось вернуться в конструктор через кнопку", isConstructorPage);
    }

    @Test
    @DisplayName("Переход в конструктор через логотип")
    @Description("Проверка перехода в конструктор через логотип")
    public void testNavigateFromProfileToConstructorViaLogo() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.PERSONAL_ACCOUNT_BUTTON)
        );
        personalAccountButton.click();

        WebElement logoButton = wait.until(
                ExpectedConditions.elementToBeClickable(Locators.LOGO_BUTTON)
        );
        logoButton.click();

        boolean isConstructorPage = wait.until(ExpectedConditions.presenceOfElementLocated(
                Locators.CONSTRUCTOR_HEADER
        )) != null;

        Assert.assertTrue("Не удалось вернуться в конструктор через логотип", isConstructorPage);
    }
}
