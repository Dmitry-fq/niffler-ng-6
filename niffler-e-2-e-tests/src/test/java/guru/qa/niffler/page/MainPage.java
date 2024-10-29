package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage implements Header {

    private final SelenideElement searchInput = $x("//input[@aria-label= 'search']");

    private final ElementsCollection tableRows = $("#spendings tbody").$$("tr");

    private final SelenideElement statisticText = $x("//h2[text()='Statistics']");

    private final SelenideElement statisticsDiagram = $x("//canvas");

    private final SelenideElement historyOfSpendingsText = $x("//h2[text()='History of Spendings']");

    private final SelenideElement historyOfSpendingsTable = $x("//div[contains(@class, 'MuiTableContainer-root')]");

    public MainPage checkElementsMainPage() {
        statisticText.shouldBe(visible);
        historyOfSpendingsText.shouldBe(visible);
        statisticsDiagram.shouldBe(visible);
        historyOfSpendingsTable.shouldBe(visible);

        return this;
    }

    public EditSpendingPage editSpending(String spendingDescription) {
        searchInput.setValue(spendingDescription)
                .pressEnter();
        tableRows.find(text(spendingDescription)).$$("td").get(5).click();

        return new EditSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        tableRows.find(text(spendingDescription)).should(visible);
    }
}
