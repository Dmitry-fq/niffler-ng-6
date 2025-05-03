package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;

@ParametersAreNonnullByDefault
public class LoginPage extends BasePage<LoginPage> {

    public static final String URL = Config.getInstance().authUrl() + "login";

    private final SelenideElement usernameInput;

    private final SelenideElement passwordInput;

    private final SelenideElement submitButton;

    private final SelenideElement registerButton;

    private final SelenideElement error;

    public LoginPage() {
        this.usernameInput = Selenide.$("input[name='username']");
        this.passwordInput = Selenide.$("input[name='password']");
        this.submitButton = Selenide.$("button[type='submit']");
        this.registerButton = Selenide.$("a[href='/register']");
        this.error = Selenide.$(".form__error");
    }

    public LoginPage(SelenideDriver driver) {
        super(driver);
        this.usernameInput = driver.$("input[name='username']");
        this.passwordInput = driver.$("input[name='password']");
        this.submitButton = driver.$("button[type='submit']");
        this.registerButton = driver.$("a[href='/register']");
        this.error = driver.$(".form__error");
    }

    public MainPage login(String username, String password) {

        return setUsername(username)
                .setPassword(password)
                .clickSubmitButton();
    }

    @Nonnull
    @Step("Установка username")
    public LoginPage setUsername(String username) {
        usernameInput.setValue(username);

        return this;
    }

    @Nonnull
    @Step("Установка пароля")
    public LoginPage setPassword(String password) {
        passwordInput.setValue(password);

        return this;
    }

    @Nonnull
    @Step("Нажатие на кнопку Submit")
    public MainPage clickSubmitButton() {
        submitButton.click();

        return new MainPage();
    }

    @Nonnull
    @Step("Нажатие на кнопку Create new account")
    public RegisterPage clickCreateNewAccount() {
        registerButton.click();

        return new RegisterPage();
    }

    @Nonnull
    @Step("Проверка сообщения об ошибке")
    public LoginPage checkErrorText() {
        String expectedText = "Неверные учетные данные пользователя";

        error.shouldHave(text(expectedText));

        return this;
    }
}
