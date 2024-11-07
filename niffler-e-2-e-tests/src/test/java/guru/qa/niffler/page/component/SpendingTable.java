package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import io.qameta.allure.Step;

import java.util.Arrays;

import static com.codeborne.selenide.CollectionCondition.size;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class SpendingTable extends BaseComponent {

    private final SelenideElement searchInput = $x("//input[@aria-label= 'search']");

    private final SelenideElement inputClearButton = $x("//button[@id = 'input-clear']");

    private final SelenideElement periodDropdown = $x("//div[@id = 'period']");

    private final ElementsCollection tableRows = $$x("//tbody/tr");

    public SpendingTable() {
        super($x("//tbody"));
    }

    @Step("Выбор периода")
    public SpendingTable selectPeriod(DataFilterValues period) {
        periodDropdown.selectOption(period.value);

        return new SpendingTable();
    }

    @Step("Редактирование траты")
    public EditSpendingPage editSpending(String description) {
        searchSpendingByDescription(description);

        String editButtonXpath = ".//button";
        tableRows.find(text(description))
                .$x(editButtonXpath)
                .click();

        return new EditSpendingPage();
    }

    @Step("Удаление траты")
    public SpendingTable deleteSpending(String description) {
        searchSpendingByDescription(description);

        String checkboxXpath = ".//input";
        tableRows.find(text(description))
                .$x(checkboxXpath)
                .click();

        return new SpendingTable();
    }

    @Step("Поиск траты по описанию")
    private SpendingTable searchSpendingByDescription(String description) {
        searchInput.setValue(description)
                .pressEnter();

        return new SpendingTable();
    }

    @Step("Проверка, что таблица трат содержит траты")
    public SpendingTable checkTableContains(String... expectedSpends) {
        Arrays.stream(expectedSpends)
                .forEach(
                        description -> {
                            searchSpendingByDescription(description);
                            tableRows.find(text(description)).shouldBe(visible);
                            inputClearButton.click();
                        }
                );

        return new SpendingTable();
    }

    @Step("Проверка размера таблицы трат")
    public SpendingTable checkTableSize(int expectedSize) {
        tableRows.shouldHave(size(expectedSize));

        return new SpendingTable();
    }
}
