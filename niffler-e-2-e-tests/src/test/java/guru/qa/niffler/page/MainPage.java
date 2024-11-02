package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.page.component.SpendingTable;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class MainPage {

    private final Header header = new Header();

    private final SelenideElement statisticText = $x("//h2[text()='Statistics']");

    private final SelenideElement statisticsDiagram = $x("//canvas");

    private final SelenideElement historyOfSpendingsText = $x("//h2[text()='History of Spendings']");

    private final SpendingTable spendingTable = new SpendingTable();

    public MainPage checkElementsMainPage() {
        statisticText.shouldBe(visible);
        historyOfSpendingsText.shouldBe(visible);
        statisticsDiagram.shouldBe(visible);

        return this;
    }

    public EditSpendingPage editSpending(String spendingDescription) {
        spendingTable.editSpending(spendingDescription);

        return new EditSpendingPage();
    }

    public AddNewSpendingPage toAddSpendingPage() {
        header.toAddSpendingPage();

        return new AddNewSpendingPage();
    }

    public void checkThatTableContainsSpending(String spendingDescription) {
        spendingTable.checkTableContains(spendingDescription);
    }
}
