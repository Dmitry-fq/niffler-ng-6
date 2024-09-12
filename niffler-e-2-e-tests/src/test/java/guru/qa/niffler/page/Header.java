package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;

public interface Header {

    SelenideElement settingsButton = $(".MuiAvatar-root");

    ElementsCollection settingsRows = $("ul[role='menu']").$$("li");

    int profileIndex = 0;

    default Header clickSettingsButton() {
        settingsButton.click();

        return this;
    }

    default ProfilePage clickProfile() {
        settingsRows.get(profileIndex).click();

        return new ProfilePage();
    }
}
