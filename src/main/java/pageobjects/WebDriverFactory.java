package pageobjects;

import io.github.bonigarcia.wdm.WebDriverManager;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.WebDriver;
import java.io.File;

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
                String yandexDriverPath = System.getProperty("yandex.driver.path", "C:\\YandexDriver\\yandexdriver.exe");
                File driverFile = new File(yandexDriverPath);

                if (!driverFile.exists() || !driverFile.isFile()) {
                    throw new IllegalStateException("❌ Файл драйвера не найден или не является файлом: " + yandexDriverPath);
                }

                System.setProperty("webdriver.chrome.driver", yandexDriverPath);
                driver = new ChromeDriver();
                break;

            default:
                throw new IllegalArgumentException("❌ Браузер не поддерживается: " + browser);
        }

        return driver;
    }

    public static void closeDriver() {
    }
}
