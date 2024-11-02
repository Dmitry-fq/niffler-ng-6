package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class EditSpendingPage {

    private final SelenideElement title = $x("//h2[contains(text(), 'spending')]");

    private final SelenideElement descriptionInput = $("#description");

    private final SelenideElement saveBtn = $("#save");

    public EditSpendingPage setNewSpendingDescription(String description) {
        descriptionInput.setValue(description);

        return this;
    }

    public void save() {
        saveBtn.click();
    }
}
