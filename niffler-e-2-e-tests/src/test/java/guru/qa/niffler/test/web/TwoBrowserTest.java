package guru.qa.niffler.test.web;

import com.codeborne.selenide.SelenideDriver;
import guru.qa.niffler.jupiter.converter.Browser;
import guru.qa.niffler.jupiter.converter.BrowserConverter;
import guru.qa.niffler.jupiter.extension.NonStaticBrowserExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.converter.ConvertWith;
import org.junit.jupiter.params.provider.EnumSource;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ExtendWith(NonStaticBrowserExtension.class)
public class TwoBrowserTest {

    @ParameterizedTest
    @EnumSource(Browser.class)
    void browserParameterizedTest(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        final String username = randomUsername();
        final String incorrectPassword = "1234";

        driver.open(LoginPage.URL);
        new LoginPage(driver)
                .setUsername(username)
                .setPassword(incorrectPassword)
                .clickSubmitButton();
    }

    @ParameterizedTest
    @EnumSource(Browser.class)
    void browserParameterizedTest2(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        final String username = randomUsername();
        final String incorrectPassword = "1234";

        driver.open(LoginPage.URL);
        new LoginPage(driver)
                .setUsername(username)
                .setPassword(incorrectPassword)
                .clickSubmitButton();
    }

    @ParameterizedTest
    @EnumSource(Browser.class)
    void browserParameterizedTest3(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        final String username = randomUsername();
        final String incorrectPassword = "1234";

        driver.open(LoginPage.URL);
        new LoginPage(driver)
                .setUsername(username)
                .setPassword(incorrectPassword)
                .clickSubmitButton();
    }

    @ParameterizedTest
    @EnumSource(Browser.class)
    void browserParameterizedTest4(@ConvertWith(BrowserConverter.class) SelenideDriver driver) {
        final String username = randomUsername();
        final String incorrectPassword = "1234";

        driver.open(LoginPage.URL);
        new LoginPage(driver)
                .setUsername(username)
                .setPassword(incorrectPassword)
                .clickSubmitButton();
    }
}

