package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.RandomDataUtils.getRandomName;

@ExtendWith(BrowserExtension.class)
public class LoginWebTest {

    private static final Config CFG = Config.getInstance();

    private final LoginPage loginPage = new LoginPage();

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordNotEqual() {
        final String username = getRandomName(3, 50);
        final String password = "test";
        final String incorrectPassword = "1234";
        final String errorText = "Passwords should be equal";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .setUsername(username)
                .setPassword(password)
                .setPasswordSubmit(incorrectPassword)
                .submitRegistration()
                .checkErrorText(errorText);
    }

    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin() {
        final String username = getRandomName(3, 50);
        final String password = "test";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .clickCreateNewAccount()
                .register(username, password)
                .login(username, password)
                .checkElementsMainPage();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        final String username = getRandomName(3, 50);
        final String incorrectPassword = "1234";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(username)
                .setPassword(incorrectPassword)
                .clickSubmitButton();

        loginPage.checkErrorText();
    }
}