package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ExtendWith(BrowserExtension.class)
public class RegistrationWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    void shouldRegisterNewUser() {
        final String username = randomUsername();
        final String password = "test";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .register(username, password)
                .login(username, password)
                .clickSettingsButton()
                .clickProfile()
                .checkUsername(username);
    }

    @Test
    void shouldNotRegisterUserWithExistingUsername() {
        final String username = randomUsername();
        final String password = "test";
        final String errorText = String.format("Username `%s` already exists", username);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .register(username, password)
                .clickCreateNewAccount()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(password)
                .submitRegistration()
                .checkErrorText(errorText);
    }
}
