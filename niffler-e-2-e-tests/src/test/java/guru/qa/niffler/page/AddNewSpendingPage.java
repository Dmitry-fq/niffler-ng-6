package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.model.CurrencyValues;
import io.qameta.allure.Step;
import lombok.NonNull;

import java.time.LocalDate;

import static com.codeborne.selenide.Selenide.$x;

public class AddNewSpendingPage extends BasePage<AddNewSpendingPage> {

    private final SelenideElement title = $x("//h2[contains(text(), 'spending')]");

    private final SelenideElement amountInput = $x("//input[@name = 'amount']");

    private final SelenideElement currencyDropdown = $x("//div[div[input[@name = 'currency']]]");

    private final SelenideElement categoryInput = $x("//input[@name = 'category']");

    private final SelenideElement calendarInput = $x("//input[@name = 'date']");

    private final SelenideElement calendarButton = $x("//img[@alt= 'Calendar']");

    private final SelenideElement descriptionInput = $x("//input[@name = 'description']");

    private final SelenideElement dateInput = $x("//input[@name = 'date']");

    private final SelenideElement cancelButton = $x("//button[text() = 'Cancel']");

    private final SelenideElement addButton = $x("//button[text() = 'Add']");

    @NonNull
    @Step("Добавление суммы")
    public AddNewSpendingPage addAmount(Double amount) {
        amountInput.setValue(amount.toString());

        return this;
    }

    @NonNull
    @Step("Добавление валюты")
    public AddNewSpendingPage addCurrency(CurrencyValues currency) {
        currencyDropdown.selectOptionByValue(currency.name());

        return this;
    }

    @NonNull
    @Step("Добавление категории")
    public AddNewSpendingPage addCategory(String categoryName) {
        categoryInput.setValue(categoryName);

        return this;
    }

    @NonNull
    @Step("Добавление даты")
    public AddNewSpendingPage addDate(LocalDate date) {
        dateInput.setValue(String.valueOf(date.getDayOfMonth()));
        dateInput.setValue(String.valueOf(date.getMonthValue()));
        dateInput.setValue(String.valueOf(date.getYear()));

        return this;
    }

    @NonNull
    @Step("Добавление описания")
    public AddNewSpendingPage addDescription(String description) {
        descriptionInput.setValue(description);

        return this;
    }

    @Step("Нажатие на кнопку Добавить")
    public void clickAddButton() {
        addButton.click();
    }
}
