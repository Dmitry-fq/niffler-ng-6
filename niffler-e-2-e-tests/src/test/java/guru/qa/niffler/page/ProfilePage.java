package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage {

    private final SelenideElement usernameInput = $x("//input[@id='username']");

    private final SelenideElement nameInput = $x("//input[@id='name']");

    private final SelenideElement saveChangesButton = $x("//button[text()='Save changes']");

    private final SelenideElement showArchivedCheckbox = $x("//input[contains(@type, 'checkbox')]");

    public void checkUsername(String currentUsername) {
        usernameInput.shouldHave(attribute("value", currentUsername));
    }

    public ProfilePage setNewName(String newName) {
        nameInput.setValue(newName);

        return this;
    }

    public ProfilePage checkName(String name) {
        nameInput.shouldHave(attribute("value", name));

        return this;
    }

    public ProfilePage clickSaveChangesButton() {
        saveChangesButton.click();

        return this;
    }

    public ProfilePage clickShowArchivedCheckbox() {
        showArchivedCheckbox.click();

        return this;
    }

    public void checkCategoryArchived(String categoryName) {
        SelenideElement category = getCategoryByCategoryName(categoryName);
        SelenideElement unarchiveButton = category.$x("../..//button[@aria-label='Unarchive category']");
        unarchiveButton.shouldBe(visible);
    }

    public void checkCategoryActive(String categoryName) {
        SelenideElement category = getCategoryByCategoryName(categoryName);
        SelenideElement editCategoryNameButton = category.$x("../..//button[@aria-label='Edit category']");
        editCategoryNameButton.shouldBe(visible);
        SelenideElement archiveButton = category.$x("../..//button[@aria-label='Archive category']");
        archiveButton.shouldBe(visible);
    }

    private SelenideElement getCategoryByCategoryName(String categoryName) {
        return $x(String.format("//*[text()='%s']", categoryName));
    }
}