package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;
import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class RegisterPage {

    private final SelenideElement usernameInput = $("#username");

    private final SelenideElement passwordInput = $("#password");

    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");

    private final SelenideElement submitRegistrationButton = $("button[type='submit']");

    private final SelenideElement error = $(".form__error");

    private final SelenideElement signInButton = $(".form_sign-in");

    @Nonnull
    @Step("Регистрация пользователя")
    public LoginPage register(String username, String password) {

        return this.setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .signIn();
    }

    @Nonnull
    @Step("Установка username")
    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);

        return this;
    }

    @Nonnull
    @Step("Установка пароля")
    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);

        return this;
    }

    @Nonnull
    @Step("Нажатие на кнопку Submit")
    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.setValue(passwordSubmit);

        return this;
    }

    @Nonnull
    @Step("Нажатие на кнопку Submit Registration")
    public RegisterPage submitRegistration() {
        submitRegistrationButton.click();

        return this;
    }

    @Nonnull
    @Step("Нажатие на кнопку Sign In")
    public LoginPage signIn() {
        signInButton.click();

        return new LoginPage();
    }

    @Nonnull
    @Step("Проверка, что ошибка содержит текст")
    public RegisterPage checkErrorText(String expectedText) {
        error.shouldHave(text(expectedText));

        return this;
    }
}
