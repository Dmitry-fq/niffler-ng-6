package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.$x;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfilePage implements Header {

    private final SelenideElement username = $("input[name='username']");

    private final SelenideElement showArchivedCheckbox = $x("//input[contains(@type, 'checkbox')]");

    public void checkUsername(String currentUsername) {
        assertThat(username.getValue())
                .as("username некорректный")
                .isEqualTo(currentUsername);
    }

    public ProfilePage clickShowArchivedCheckbox() {
        showArchivedCheckbox.click();

        return this;
    }

    public void checkCategoryArchived(boolean archived, String categoryName) {
        SelenideElement category = $x(String.format("//*[text()='%s']", categoryName));
        
        if (archived) {
            SelenideElement unarchiveButton = category.$x("../..//button[@aria-label='Unarchive category']");
            unarchiveButton.shouldBe(visible);
        } else {
            SelenideElement editCategoryNameButton = category.$x("../..//button[@aria-label='Edit category']");
            editCategoryNameButton.shouldBe(visible);
            SelenideElement archiveButton = category.$x("../..//button[@aria-label='Archive category']");
            archiveButton.shouldBe(visible);
        }
    }
}