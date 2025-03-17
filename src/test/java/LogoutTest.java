import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.LoginPage;
import pageobjects.AccountPage;
import pageobjects.WebDriverFactory;
import java.time.Duration;

public class LogoutTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        driver.get("https://stellarburgers.nomoreparties.site/login");

        wait = new WebDriverWait(driver, Duration.ofSeconds(3));
        loginPage = new LoginPage(driver);
        accountPage = new AccountPage(driver);
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Вход']")));
        loginPage.enterEmail("testirovanieui@yandex.ru");
        loginPage.enterPassword("password");

        WebElement loginButton = wait.until(ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Войти']")));
        loginButton.click();

        boolean isLoggedIn = wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, '/account')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Выход']"))
        )) != null;

        System.out.println(" Текущий URL после входа: " + driver.getCurrentUrl());

        Assert.assertTrue(" Вход не выполнен!", isLoggedIn);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @DisplayName("Выход из аккаунта")
    @Description("Проверка выхода пользователя из аккаунта")
    public void testLogout() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/account')]"))
        );
        personalAccountButton.click();

        WebElement logoutButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//button[text()='Выход']"))
        );
        logoutButton.click();
        boolean isLoggedOut = wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[text()='Вход']"))) != null;
        System.out.println(" Текущий URL после выхода: " + driver.getCurrentUrl());
        Assert.assertTrue("  Выход не выполнен!", isLoggedOut);
    }
}
