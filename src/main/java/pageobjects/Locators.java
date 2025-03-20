package pageobjects;

import org.openqa.selenium.By;

public class Locators {
    public static final By EMAIL_FIELD = By.xpath("//label[text()='Email']/following-sibling::input");
    public static final By PASSWORD_FIELD = By.xpath("//label[text()='Пароль']/following-sibling::input");
    public static final By LOGIN_BUTTON = By.xpath("//button[text()='Войти']");
    public static final By PERSONAL_ACCOUNT_BUTTON = By.xpath("//a[contains(@href, '/account')]");
    public static final By LOGOUT_BUTTON = By.xpath("//button[text()='Выход']");
    public static final By LOGIN_HEADER = By.xpath("//h2[text()='Вход']");
    public static final By ERROR_MESSAGE = By.xpath("//p[contains(text(), 'email or password are incorrect')]");
    public static final By CONSTRUCTOR_BUTTON = By.xpath("//p[text()='Конструктор']");
    public static final By CONSTRUCTOR_HEADER = By.xpath("//h1[text()='Соберите бургер']");
    public static final By LOGO_BUTTON = By.xpath("//div[@class='AppHeader_header__logo__2D0X2']");
}
