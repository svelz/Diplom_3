package pageobjects;

import org.openqa.selenium.By;

public class Locators {

    public static final By EMAIL_FIELD = By.xpath("//label[text()='Email']/following-sibling::input");
    public static final By PASSWORD_FIELD = By.xpath("//label[text()='Пароль']/following-sibling::input");
    public static final By LOGIN_BUTTON = By.xpath("//button[text()='Войти']");
    public static final By PERSONAL_ACCOUNT_BUTTON = By.xpath("//a[contains(@href, '/account')]");
    public static final By ERROR_MESSAGE = By.xpath("//p[contains(text(), 'email or password are incorrect')]");
    public static final By REGISTER_LOGIN_BUTTON = By.xpath("//p/a[contains(text(), 'Войти')]");
    public static final By FORGOT_PASSWORD_LOGIN_BUTTON = By.xpath("//p/a[contains(text(), 'Войти')]");
}
