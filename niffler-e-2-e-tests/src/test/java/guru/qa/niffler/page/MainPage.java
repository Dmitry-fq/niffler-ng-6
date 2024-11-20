package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;
import guru.qa.niffler.page.component.StatComponent;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Condition.exactText;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;
import static guru.qa.niffler.utils.ScreenshotAssertions.imagesShouldBeEquals;

@ParametersAreNonnullByDefault
public class MainPage extends BasePage<MainPage> {

    private static final Config CFG = Config.getInstance();

    public static final String URL = CFG.frontUrl() + "main";

    protected final Header header = new Header();

    protected final SelenideElement statisticText = $x("//h2[text()='Statistics']");

    protected final SelenideElement statisticsDiagram = $x("//canvas");

    protected final SelenideElement statisticChartBars = $x("//ul");

    protected final SelenideElement historyOfSpendingsText = $x("//h2[text()='History of Spendings']");

    protected final SelenideElement alertDeleteButton = $x("//div[h2[contains(text(), 'spending')]]//button[text() = 'Delete']");

    protected final SpendingTable spendingTable = new SpendingTable();

    protected final StatComponent statComponent = new StatComponent();

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

    @Step("Проверка отсутствия плашек диаграммы статистики")
    public MainPage statisticChartBarsShouldNotExist() {
        statisticChartBars.shouldNotBe(visible);

        return this;
    }

    @Nonnull
    public StatComponent getStatComponent() {
        statComponent.getSelf().scrollIntoView(true);
        return statComponent;
    }

    @Nonnull
    public SpendingTable getSpendingTable() {
        spendingTable.getSelf().scrollIntoView(true);
        return spendingTable;
    }

    @Step("Check that page is loaded")
    @Nonnull
    public MainPage checkThatPageLoaded() {
        header.getSelf().should(visible).shouldHave(text("Niffler"));
        statComponent.getSelf().should(visible).shouldHave(text("Statistics"));
        historyOfSpendingsText.should(visible);
        return this;
    }
}
