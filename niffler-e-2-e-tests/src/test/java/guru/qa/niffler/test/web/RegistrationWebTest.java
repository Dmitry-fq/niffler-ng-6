package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.component.Header;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class RegistrationWebTest {

    private static final Config CFG = Config.getInstance();

    private final Header header = new Header();

    @Test
    void shouldRegisterNewUser() {
        final String username = randomUsername();
        final String password = "test";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .register(username, password)
                .login(username, password);

        header.toProfilePage()
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
