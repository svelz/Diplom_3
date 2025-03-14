import io.qameta.allure.Description;
import io.qameta.allure.junit4.DisplayName;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import pageobjects.ConstructorPage;
import pageobjects.WebDriverFactory;

public class ConstructorTest {
    private WebDriver driver;
    private ConstructorPage constructorPage;

    @Before
    public void setUp() {
        driver = WebDriverFactory.createDriver(System.getProperty("browser", "chrome"));
        driver.get("https://stellarburgers.nomoreparties.site");
        constructorPage = new ConstructorPage(driver);
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    @Test
    @Description("Открытие страницы и переход в раздел 'Соусы', затем ожидание 1 секунду")
    public void testNavigateToSauces() throws InterruptedException {
        constructorPage.clickSauces();
        Thread.sleep(1000);
        WebElement activeTab = driver.findElement(By.xpath("//div[contains(@class, 'tab_tab_type_current') and span[text()='Соусы']]"));
        Assert.assertNotNull("Раздел 'Соусы' не стал активным!", activeTab);
    }

    @Test
    @DisplayName("Переход в раздел 'Начинки'")
    @Description("Открытие страницы и переход в раздел 'Начинки', затем ожидание 1 секунду")
    public void testNavigateToFillings() throws InterruptedException {
        constructorPage.clickFillings();
        Thread.sleep(1000);
        WebElement activeTab = driver.findElement(By.xpath("//div[contains(@class, 'tab_tab_type_current') and span[text()='Начинки']]"));
        Assert.assertNotNull("Раздел 'Начинки' не стал активным!", activeTab);
    }

    @Test
    @DisplayName("Переход в раздел 'Начинки', затем 'Булки'")
    @Description("Открытие страницы, переход в 'Начинки', ожидание 1 секунду, затем в 'Булки', ожидание 1 секунду")
    public void testNavigateToFillingsThenBuns() throws InterruptedException {
        constructorPage.clickFillings();
        Thread.sleep(1000);

        WebElement fillingsTab = driver.findElement(By.xpath("//div[contains(@class, 'tab_tab_type_current') and span[text()='Начинки']]"));
        Assert.assertNotNull("Раздел 'Начинки' не стал активным!", fillingsTab);

        constructorPage.clickBuns();
        Thread.sleep(1000);

        WebElement bunsTab = driver.findElement(By.xpath("//div[contains(@class, 'tab_tab_type_current') and span[text()='Булки']]"));
        Assert.assertNotNull("Раздел 'Булки' не стал активным!", bunsTab);
    }
}
