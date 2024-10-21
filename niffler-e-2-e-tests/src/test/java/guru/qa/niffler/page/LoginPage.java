package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class LoginPage {

    private final SelenideElement error = $x("//div[contains(@class, 'form__error')]");

    private final SelenideElement usernameInput = $("input[name='username']");

    private final SelenideElement passwordInput = $("input[name='password']");

    private final SelenideElement submitButton = $("button[type='submit']");

    private final SelenideElement registerButton = $("a[href='/register']");

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
