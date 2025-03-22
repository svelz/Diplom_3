import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import pageobjects.ConstructorPage;
import pageobjects.WebDriverFactory;
import api.Endpoints;

import java.time.Duration;

public class ConstructorTest {
    private WebDriver driver;
    private ConstructorPage constructorPage;
    private WebDriverWait wait;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        driver.get(Endpoints.CONSTRUCTOR_URL);
        constructorPage = new ConstructorPage(driver);
        wait = new WebDriverWait(driver, Duration.ofSeconds(5));
    }

    @After
    public void tearDown() {
        WebDriverFactory.closeDriver(driver);
    }

    @Test
    @DisplayName("Переход в раздел 'Соусы'")
    @Description("Открытие страницы и переход в раздел 'Соусы'")
    public void testNavigateToSauces() {
        constructorPage.clickSauces();
        WebElement activeTab = wait.until(ExpectedConditions.visibilityOfElementLocated(ConstructorPage.saucesActiveTab));
        Assert.assertNotNull("Раздел 'Соусы' не стал активным!", activeTab);
    }

    @Test
    @DisplayName("Переход в раздел 'Начинки'")
    @Description("Открытие страницы и переход в раздел 'Начинки'")
    public void testNavigateToFillings() {
        constructorPage.clickFillings();
        WebElement activeTab = wait.until(ExpectedConditions.visibilityOfElementLocated(ConstructorPage.fillingsActiveTab));
        Assert.assertNotNull("Раздел 'Начинки' не стал активным!", activeTab);
    }

    @Test
    @DisplayName("Переход в раздел 'Начинки', затем 'Булки'")
    @Description("Открытие страницы, переход в 'Начинки', затем в 'Булки'")
    public void testNavigateToFillingsThenBuns() {
        constructorPage.clickFillings();
        WebElement fillingsTab = wait.until(ExpectedConditions.visibilityOfElementLocated(ConstructorPage.fillingsActiveTab));
        Assert.assertNotNull("Раздел 'Начинки' не стал активным!", fillingsTab);

        constructorPage.clickBuns();
        WebElement bunsTab = wait.until(ExpectedConditions.visibilityOfElementLocated(ConstructorPage.bunsActiveTab));
        Assert.assertNotNull("Раздел 'Булки' не стал активным!", bunsTab);
    }
}
