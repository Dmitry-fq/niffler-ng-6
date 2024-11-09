package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class SpendingWebTest {

    private static final Config CFG = Config.getInstance();

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 79990
                    )
            }
    )
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        String username = user.username();
        String password = "12345";
        String description = user.testData().spendings().getFirst().description();

        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .editSpending(description)
                .setSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @Test
    void newSpendingShouldBeCreated(UserJson user) {
        String username = user.username();
        String password = "12345";
        Double amount = RandomDataUtils.randomAmount();
        String categoryName = RandomDataUtils.randomCategoryName();
        String description = RandomDataUtils.randomSentence(2);

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .toAddSpendingPage()
                .addAmount(amount)
                .addCategory(categoryName)
                .addDescription(description)
                .clickAddButton();

        new MainPage().checkThatTableContainsSpending(description);
    }
}

