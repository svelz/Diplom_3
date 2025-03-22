package pageobjects;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import java.util.Optional;

public class WebDriverFactory {

    public static WebDriver createDriver(String browser) {
        WebDriver driver;

        switch (browser.toLowerCase()) {
            case "chrome":
                WebDriverManager.chromedriver().setup();
                ChromeOptions chromeOptions = new ChromeOptions();
                chromeOptions.addArguments("--disable-notifications");
                chromeOptions.addArguments("--start-maximized");
                driver = new ChromeDriver(chromeOptions);
                break;

            case "yandex":
                String yandexDriverPath = Optional.ofNullable(System.getProperty("yandex.driver.path"))
                        .orElseThrow(() -> new IllegalStateException(
                                "Ошибка: путь к Yandex WebDriver не задан. " +
                                        "Используйте -Dyandex.driver.path=/путь/к/драйверу"));
                System.setProperty("webdriver.chrome.driver", yandexDriverPath);
                driver = new ChromeDriver();
                break;

            default:
                throw new IllegalArgumentException("Ошибка: Браузер '" + browser + "' не поддерживается.");
        }

        return driver;
    }

    public static void closeDriver(WebDriver driver) {
        if (driver != null) {
            driver.quit();
        }
    }
}
