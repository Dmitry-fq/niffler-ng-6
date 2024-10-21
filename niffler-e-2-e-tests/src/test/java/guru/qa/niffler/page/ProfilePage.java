package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class ProfilePage extends BasePage<ProfilePage> {

    public static String url = Config.getInstance().frontUrl() + "profile";

    private final SelenideElement usernameInput = $x("//input[@id='username']");

    private final SelenideElement nameInput = $x("//input[@id='name']");

    private final SelenideElement saveChangesButton = $x("//button[text()='Save changes']");

    private final SelenideElement showArchivedCheckbox = $x("//input[contains(@type, 'checkbox')]");

    public void checkUsername(String currentUsername) {
        usernameInput.shouldHave(attribute("value", currentUsername));
    }

    @Nonnull
    @Step("Установка нового имени")
    public ProfilePage setNewName(String newName) {
        nameInput.setValue(newName);

        return this;
    }

    @Nonnull
    @Step("Проверка имени")
    public ProfilePage checkName(String name) {
        nameInput.shouldHave(attribute("value", name));

        return this;
    }

    @Nonnull
    @Step("нажатие на кнопку Save Changes")
    public ProfilePage clickSaveChangesButton() {
        saveChangesButton.click();

        return this;
    }

    @Nonnull
    @Step("Нажатие на радиобаттон Show Archived")
    public ProfilePage clickShowArchivedCheckbox() {
        showArchivedCheckbox.click();

        return this;
    }

    @Step("Проверка, что категория архивная")
    public void checkCategoryArchived(String categoryName) {
        SelenideElement category = getCategoryByCategoryName(categoryName);
        SelenideElement unarchiveButton = category.$x("../..//button[@aria-label='Unarchive category']");
        unarchiveButton.shouldBe(visible);
    }

    @Step("Проверка, что категория активная")
    public void checkCategoryActive(String categoryName) {
        SelenideElement category = getCategoryByCategoryName(categoryName);
        SelenideElement editCategoryNameButton = category.$x("../..//button[@aria-label='Edit category']");
        editCategoryNameButton.shouldBe(visible);
        SelenideElement archiveButton = category.$x("../..//button[@aria-label='Archive category']");
        archiveButton.shouldBe(visible);
    }

    @Nonnull
    @Step("Получение элемента категории по имени категории")
    private SelenideElement getCategoryByCategoryName(String categoryName) {
        return $x(String.format("//*[text()='%s']", categoryName));
    }
}