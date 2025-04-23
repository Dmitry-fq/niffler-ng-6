package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.SpendJson;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.List;

@WebTest
public class SpendingWebTest {

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 79990
                    )
            }
    )
    @ApiLogin
    @Test
    void categoryDescriptionShouldBeChangedFromTable(UserJson user) {
        String description = user.testData().spendings().getFirst().description();

        final String newDescription = "Обучение Niffler Next Generation";

        Selenide.open(MainPage.URL, MainPage.class)
                .editSpending(description)
                .setSpendingDescription(newDescription)
                .save();

        new MainPage().checkThatTableContainsSpending(newDescription);
    }

    @User
    @ApiLogin
    @Test
    void newSpendingShouldBeCreated(UserJson user) {
        Double amount = RandomDataUtils.randomAmount();
        String categoryName = RandomDataUtils.randomCategoryName();
        String description = RandomDataUtils.randomSentence(2);

        Selenide.open(MainPage.URL, MainPage.class)
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
    @ApiLogin
    @ScreenShotTest(expected = "expected-stat.png")
    void checkStatComponentTest(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(MainPage.URL, MainPage.class)
                .checkStatDiagramByScreenshot(expected)
                .checkStatisticChartBars("Обучение 79990 ₽")
                .getStatComponent()
                .checkFirstBubble(new Bubble(Color.yellow, "Обучение 79990 ₽"));
    }

    @User(
            spendings = @Spending(
                    category = "Обучение",
                    description = "Обучение Advanced 2.0",
                    amount = 79990
            )
    )
    @ApiLogin
    @ScreenShotTest(expected = "expected-stat-edit.png")
    void checkStatComponentWithEditSpendingTest(UserJson user, BufferedImage expected) throws IOException {
        double newAmount = 100.0;

        Selenide.open(MainPage.URL, MainPage.class)
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
    @ApiLogin
    @ScreenShotTest(expected = "expected-stat-clear.png")
    void checkStatComponentWithoutSpendingsTest(UserJson user, BufferedImage expected) throws IOException {
        Selenide.open(MainPage.URL, MainPage.class)
                .deleteSpending(user.testData().spendings().getFirst().description())
                .checkStatDiagramByScreenshot(expected)
                .statisticChartBarsShouldNotExist();
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 79990
                    ),
                    @Spending(category = "Развлечения",
                            description = "Рыбалка",
                            amount = 89990
                    ),
                    @Spending(category = "123",
                            description = "123",
                            amount = 123
                    )
            }

    )
    @ApiLogin
    @Test
    void checkStatComponentContainsBubblesTest() {
        Selenide.open(MainPage.URL, MainPage.class)
                .getStatComponent()
                .checkBubblesContains(
                        new Bubble(Color.green, "Обучение 79990 ₽"),
                        new Bubble(Color.yellow, "Развлечения 89990 ₽")
                );
    }

    @User(
            spendings = {
                    @Spending(
                            category = "Обучение",
                            description = "Обучение Advanced 2.0",
                            amount = 79990
                    ),
                    @Spending(category = "Развлечения",
                            description = "Рыбалка",
                            amount = 89990
                    )
            }

    )
    @ApiLogin
    @Test
    void checkSpendExistTest(UserJson user) {
        List<SpendJson> expectedSpends = user.testData().spendings();

        Selenide.open(MainPage.URL, MainPage.class)
                .getSpendingTable()
                .checkSpendings(expectedSpends);
    }
}
