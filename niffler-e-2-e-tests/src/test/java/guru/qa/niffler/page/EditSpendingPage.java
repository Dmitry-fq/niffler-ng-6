package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class EditSpendingPage {

    private final SelenideElement title = $x("//h2[contains(text(), 'spending')]");

    private final SelenideElement descriptionInput = $("#description");

    private final SelenideElement saveBtn = $("#save");

    @Nonnull
    @Step("Установка описания трате")
    public EditSpendingPage setSpendingDescription(String description) {
        descriptionInput.setValue(description);

        return this;
    }

    @Step("Нажатие на кнопку Сохранить")
    public void save() {
        saveBtn.click();
    }
}
