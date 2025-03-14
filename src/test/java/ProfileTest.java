import io.qameta.allure.Description;
import org.junit.*;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.AccountPage;
import pageobjects.LoginPage;
import pageobjects.WebDriverFactory;

import java.time.Duration;

public class ProfileTest {
    private WebDriver driver;
    private LoginPage loginPage;
    private AccountPage accountPage;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        driver.get("https://stellarburgers.nomoreparties.site/login");

        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
        loginPage = new LoginPage(driver);
        accountPage = new AccountPage(driver);

        wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//h2[text()='Вход']")));

        loginPage.enterEmail("testirovanieui@yandex.ru");
        loginPage.enterPassword("password");
        loginPage.clickLoginButton();

        boolean isLoggedIn = wait.until(ExpectedConditions.or(
                ExpectedConditions.presenceOfElementLocated(By.xpath("//a[contains(@href, '/account')]")),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//button[text()='Выход']"))
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
    @Description("Проверка перехода в личный кабинет")
    public void testNavigateToProfile() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/account')]"))
        );
        personalAccountButton.click();

        WebElement logoutButton = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//button[text()='Выход']")
        ));

        Assert.assertTrue("Не удалось перейти в личный кабинет!", logoutButton.isDisplayed());
    }

    @Test
    @Description("Проверка перехода из личного кабинета в конструктор")
    public void testNavigateFromProfileToConstructor() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/account')]"))
        );
        personalAccountButton.click();

        WebElement constructorButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//p[text()='Конструктор']"))
        );
        constructorButton.click();

        boolean isConstructorPage = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h1[text()='Соберите бургер']")
        )) != null;

        Assert.assertTrue("Не удалось вернуться в конструктор через кнопку", isConstructorPage);
    }

    @Test
    @Description("Проверка перехода в конструктор через логотип")
    public void testNavigateFromProfileToConstructorViaLogo() {
        WebElement personalAccountButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//a[contains(@href, '/account')]"))
        );
        personalAccountButton.click();

        WebElement logoButton = wait.until(
                ExpectedConditions.elementToBeClickable(By.xpath("//div[@class='AppHeader_header__logo__2D0X2']"))
        );
        logoButton.click();

        boolean isConstructorPage = wait.until(ExpectedConditions.presenceOfElementLocated(
                By.xpath("//h1[text()='Соберите бургер']")
        )) != null;

        Assert.assertTrue("Не удалось вернуться в конструктор через логотип", isConstructorPage);
    }
}