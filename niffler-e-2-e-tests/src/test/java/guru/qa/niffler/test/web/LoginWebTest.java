package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@WebTest
public class LoginWebTest {

    private static final Config CFG = Config.getInstance();

    private final LoginPage loginPage = new LoginPage();

    @Test
    void shouldShowErrorIfPasswordAndConfirmPasswordNotEqual() {
        final String username = randomUsername();
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

    @User(
            categories = {
                    @Category(name = "cat_1"),
                    @Category(name = "cat_2", archived = true),
            },
            spendings = {
                    @Spending(
                            category = "cat_3",
                            description = "test_spend",
                            amount = 100
                    )
            }
    )
    @Test
    void mainPageShouldBeDisplayedAfterSuccessLogin(UserJson user) {
        final String username = user.username();
        final String password = user.testData().password();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .checkElementsMainPage();
    }

    @Test
    void userShouldStayOnLoginPageAfterLoginWithBadCredentials() {
        final String username = randomUsername();
        final String incorrectPassword = "1234";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .setUsername(username)
                .setPassword(incorrectPassword)
                .clickSubmitButton();

        loginPage.checkErrorText();
    }
}
