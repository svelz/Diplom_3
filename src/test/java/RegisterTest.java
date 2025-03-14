import io.qameta.allure.Description;
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

import java.util.UUID;

public class RegisterTest {
    private WebDriver driver;
    private RegisterPage registerPage;
    private String uniqueEmail;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        driver.get("https://stellarburgers.nomoreparties.site/register");
        uniqueEmail = "test" + UUID.randomUUID().toString().substring(0, 8) + "@example.com";
        System.out.println("Используем email: " + uniqueEmail);

        registerPage = new RegisterPage(driver);
    }

    @After
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    @Test
    @Description("Проверка успешной регистрации нового пользователя с уникальным email")
    public void testSuccessfulRegistration() throws InterruptedException {
        registerPage.enterName("Тестовый Пользователь");
        registerPage.enterEmail(uniqueEmail);
        registerPage.enterPassword("Test@1234");
        registerPage.clickRegisterButton();

        WebDriverWait wait = new WebDriverWait(driver, java.time.Duration.ofSeconds(5));

        boolean isLoginPage = wait.until(ExpectedConditions.or(
                ExpectedConditions.urlContains("login"),
                ExpectedConditions.presenceOfElementLocated(By.xpath("//h2[text()='Вход']"))
        ));

        System.out.println("Фактический URL после регистрации: " + driver.getCurrentUrl());

        Assert.assertTrue("Регистрация не удалась! Ожидался редирект на страницу логина.", isLoginPage);
    }
    @Test
    @Description("Ошибка при вводе короткого пароля")
    public void testRegistrationWithShortPassword() {
        registerPage.enterName("shortpassuser");
        registerPage.enterEmail("shortpass@example.com");
        registerPage.enterPassword("12345");
        registerPage.clickRegisterButton();

        String errorMessage = registerPage.getPasswordErrorMessage();
        Assert.assertEquals("Некорректное сообщение об ошибке!", "Некорректный пароль", errorMessage);
    }
}

