package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.utils.ScreenshotAssertions.imagesShouldBeEquals;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

    private final Header header = new Header();

    private final SelenideElement statisticText = $x("//h2[text()='Statistics']");

    private final SelenideElement statisticsDiagram = $x("//canvas");

    private final SelenideElement statisticChartBars = $x("//ul");

    private final SelenideElement historyOfSpendingsText = $x("//h2[text()='History of Spendings']");

    private final SelenideElement alertDeleteButton = $x("//div[h2[contains(text(), 'spending')]]//button[text() = 'Delete']");

    private final SpendingTable spendingTable = new SpendingTable();

    @Nonnull
    @Step("Проверка элементов на главной странице")
    public MainPage checkElementsMainPage() {
        statisticText.shouldBe(visible);
        historyOfSpendingsText.shouldBe(visible);
        statisticsDiagram.shouldBe(visible);

        return this;
    }

    @Nonnull
    @Step("Редактирование траты по описанию")
    public EditSpendingPage editSpending(String spendingDescription) {
        spendingTable.editSpending(spendingDescription);

        return new EditSpendingPage();
    }

    @Nonnull
    @Step("Удаление траты по описанию")
    public MainPage deleteSpending(String spendingDescription) {
        spendingTable.deleteSpending(spendingDescription);
        alertDeleteButton.click();

        return new MainPage();
    }

    @Nonnull
    @Step("Переход на страницу добавления траты")
    public AddNewSpendingPage toAddSpendingPage() {
        header.toAddSpendingPage();

        return new AddNewSpendingPage();
    }

    @Step("Проверка, что таблица трат содержит трату по описанию")
    public void checkThatTableContainsSpending(String spendingDescription) {
        spendingTable.checkTableContains(spendingDescription);
    }

    @Step("Проверка диаграммы статистики по картинке")
    public MainPage checkStatDiagramByScreenshot(BufferedImage expectedImage) throws IOException {
        imagesShouldBeEquals(expectedImage, statisticsDiagram);

        return this;
    }

    @Step("Проверка плашек диаграммы статистики")
    public MainPage checkStatisticChartBars(String... barTexts) {
        for (String s : barTexts) {
            statisticChartBars.shouldHave(exactText(s));
        }

        return this;
    }

    @Step("Проверка плашек диаграммы статистики")
    public MainPage statisticChartBarsShouldNotExist() {
        statisticChartBars.shouldNotBe(visible);

        return this;
    }
}
