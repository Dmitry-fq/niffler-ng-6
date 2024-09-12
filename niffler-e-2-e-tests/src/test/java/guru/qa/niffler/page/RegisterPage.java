package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class RegisterPage {

    private final SelenideElement usernameInput = $("#username");

    private final SelenideElement passwordInput = $("#password");

    private final SelenideElement passwordSubmitInput = $("#passwordSubmit");

    private final SelenideElement submitRegistrationButton = $("button[type='submit']");

    private final SelenideElement error = $(".form__error");

    private final SelenideElement signInButton = $(".form_sign-in");

    public LoginPage register(String username, String password) {

        return this.setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .signIn();
    }

    public RegisterPage setUsername(String username) {
        usernameInput.setValue(username);

        return this;
    }

    public RegisterPage setPassword(String password) {
        passwordInput.setValue(password);

        return this;
    }

    public RegisterPage setPasswordSubmit(String passwordSubmit) {
        passwordSubmitInput.setValue(passwordSubmit);

        return this;
    }

    public RegisterPage submitRegistration() {
        submitRegistrationButton.click();

        return this;
    }

    public LoginPage signIn() {
        signInButton.click();

        return new LoginPage();
    }

    public RegisterPage checkErrorText(String expectedText) {
        assertThat(error.getText())
                .as("Текст ошибки некорректный")
                .isEqualTo(expectedText);

        return this;
    }
}
