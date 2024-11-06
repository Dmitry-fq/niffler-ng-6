package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.StatComponent;
import guru.qa.niffler.utils.RandomDataUtils;
import guru.qa.niffler.utils.ScreenDiffResult;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertFalse;

@WebTest
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

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest("img/expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .checkStatDiagramByScreenshot(expected)
                .checkStatisticChartBars("Обучение 79990 ₽")
                .getStatComponent()
                .checkBubbles(Color.yellow);
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest("img/expected-stat-edit.png")
    void checkStatComponentWithEditSpendingTest(UserJson user, BufferedImage expected) throws IOException {
        double newAmount = 100.0;

        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .editSpending(user.testData().spendings().getFirst().description())
                .setAmountDescription(Double.toString(newAmount))
                .save()
                .checkStatDiagramByScreenshot(expected)
                .checkStatisticChartBars("Обучение 100 ₽");
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ScreenShotTest("img/expected-stat-clear.png")
    void checkStatComponentWithoutSpendingsTest(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .deleteSpending(user.testData().spendings().getFirst().description())
                .checkStatDiagramByScreenshot(expected)
                .statisticChartBarsShouldNotExist();
    }

    @ScreenShotTest("img/expected-stat.png")
    void checkStatComponentTest2(UserJson user, BufferedImage expected) throws IOException, InterruptedException {
        StatComponent statComponent = Selenide.open(LoginPage.URL, LoginPage.class)
                .login(user.username(), user.testData().password())
                .getStatComponent();

        Thread.sleep(3000);

        assertFalse(new ScreenDiffResult(
                expected,
                statComponent.chartScreenshot()
        ), "Screen comparison failure");

        statComponent.checkBubbles(Color.yellow);
    }
}

