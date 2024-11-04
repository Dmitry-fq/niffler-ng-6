package guru.qa.niffler.page;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.component.FriendsTable;
import io.qameta.allure.Step;
import lombok.NonNull;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class FriendsPage {

    private final SelenideElement friendsTab = $x("//h2[text()='Friends']");

    private final SelenideElement allPeopleTab = $x("//h2[text()='All people']");

    private final ElementsCollection friends = $x("//tbody['friends']").$$x("tr");

    private final SelenideElement searchInput = $x("//input[@aria-label= 'search']");

    private final ElementsCollection friendIncomeRequests = $x("//tbody['requests']").$$x("tr");

    private final ElementsCollection friendOutcomeRequests = $x("//tbody['all']").$$x("tr");

    private final SelenideElement noUserText = $x("//p[text()='There are no users yet']");

    private final SelenideElement previousButton = $x("//button[text()='Previous']");

    private final SelenideElement NextButton = $x("//button[text()='Next']");

    private final FriendsTable friendsTable = new FriendsTable();

    @NonNull
    @Step("Проверка, что друг присутствует по имени")
    public FriendsPage checkFriends(String friendName) {
        SelenideElement friend = friends.find(text(friendName));
        searchUserByText(friendName);
        friends.find(text(friendName)).shouldBe(visible);

        return this;
    }

    @NonNull
    @Step("Проверка, что список друзей пуст")
    public FriendsPage checkFriendsListEmpty() {
        noUserText.shouldBe(visible);

        return this;
    }

    @NonNull
    @Step("Проверка входящего запроса дружбы")
    public FriendsPage checkIncomeInvitation(String username) {
        SelenideElement incomeInvitation = friends.find(text(username));
        searchUserByText(username);
        friends.find(text(username)).shouldBe(visible);

        return this;
    }

    @NonNull
    @Step("Проверка исходящего запроса дружбы")
    public FriendsPage checkOutcomeFriendRequest(String username) {
        SelenideElement friendRow = friendOutcomeRequests.find(text(username));
        friendRow.shouldHave(text("Waiting..."));

        return this;
    }

    @NonNull
    @Step("Нажатие на вкладку All People")
    public FriendsPage clickAllPeopleTab() {
        allPeopleTab.click();

        return this;
    }

    @Step("Поиск пользователя по тексту")
    private void searchUserByText(String text) {
        searchInput.sendKeys(text);
        searchInput.pressEnter();
    }

    @NonNull
    @Step("Принятие запроса дружбы по логину или имени")
    public FriendsPage acceptFriendRequestByLoginOrName(String loginOrName) {
        friendsTable.acceptFriendRequestByLoginOrName(loginOrName);

        return this;
    }

    @NonNull
    @Step("Отклонение запроса дружбы по логину или имени")
    public FriendsPage declineFriendRequestByLoginOrName(String loginOrName) {
        friendsTable.declineFriendRequestByLoginOrName(loginOrName);

        return this;
    }

    @NonNull
    @Step("Проверка, что пользователь имеет друга по логину или имени")
    public FriendsPage userIsFriend(String loginOrName) {
        friendsTable.userIsFriend(loginOrName);

        return this;
    }
}
