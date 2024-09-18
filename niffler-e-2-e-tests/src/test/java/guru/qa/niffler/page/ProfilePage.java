package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.attribute;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;

public class ProfilePage implements Header {

    private final SelenideElement username = $("input[name='username']");

    private final SelenideElement showArchivedCheckbox = $x("//input[contains(@type, 'checkbox')]");

    public void checkUsername(String currentUsername) {
        username.shouldHave(attribute("value", currentUsername));
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