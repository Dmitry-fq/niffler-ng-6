package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$$x;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsTable {

    private final SelenideElement searchInput = $x("//input[@aria-label= 'search']");

    private final SelenideElement inputClearButton = $x("//button[@id = 'input-clear']");

    private final ElementsCollection tableRows = $$x("//tbody[@id='requests']/tr");

    private final SelenideElement acceptButton = $x("//button[text() = 'Accept']");

    private final SelenideElement declineButton = $x("//button[text() = 'Decline']");

    private final SelenideElement declineOnDialogButton = $x("//div[contains(@role, \"dialog\")]//button[text() = 'Decline']");

    private final SelenideElement unfriendButton = $x("//button[text() = 'Unfriend']");

    private final SelenideElement deleteButton = $x("//button[text() = 'Delete']");

    @Step("Поиск друзей по логину или имени")
    private FriendsTable searchFriendByLoginOrName(String loginOrName) {
        searchInput.setValue(loginOrName)
                .pressEnter();

        return this;
    }

    @Step("Принятие запроса дружбы по логину или имени")
    public FriendsTable acceptFriendRequestByLoginOrName(String loginOrName) {
        searchFriendByLoginOrName(loginOrName);
        acceptButton.click();
        inputClearButton.click();

        return this;
    }

    @Step("Отклонение запроса дружбы по логину или имени")
    public FriendsTable declineFriendRequestByLoginOrName(String loginOrName) {
        searchFriendByLoginOrName(loginOrName);
        declineButton.click();
        declineOnDialogButton.click();
        inputClearButton.click();

        return this;
    }

    @Step("Проверка, что друг существует по логину или имени")
    public FriendsTable userIsFriend(String loginOrName) {
        searchFriendByLoginOrName(loginOrName);
        unfriendButton.shouldBe(visible);
        inputClearButton.click();

        return this;
    }
}