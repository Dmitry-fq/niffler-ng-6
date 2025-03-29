package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.Header;
import org.junit.jupiter.api.Test;

@WebTest
public class FriendsWebTest {

    private final Header header = new Header();

    @User(friends = 1)
    @ApiLogin
    @Test
    void friendsShouldBePresentInFriendsTable(UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class);
        header.toFriendsPage()
              .checkFriends(user.testData()
                                .friends()
                                .getFirst()
                                .username());
    }

    @User
    @ApiLogin
    @Test
    void friendsTableShouldBeEmptyForNewUser() {
        Selenide.open(MainPage.URL, MainPage.class);
        header.toFriendsPage()
              .checkFriendsListEmpty();
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class);
        header.toFriendsPage()
              .checkIncomeInvitation(user.testData()
                                         .incomeInvitations()
                                         .getFirst()
                                         .username());
    }

    @User(outcomeInvitations = 1)
    @ApiLogin
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class);
        header.toFriendsPage()
              .clickAllPeopleTab()
              .checkOutcomeFriendRequest(user.testData()
                                             .outcomeInvitations()
                                             .getFirst()
                                             .username());
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void friendRequestsShouldBeAccept(UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class);
        String incomeInvitationUsername = user.testData()
                                              .incomeInvitations()
                                              .getFirst()
                                              .username();
        header.toFriendsPage()
              .acceptFriendRequestByLoginOrName(incomeInvitationUsername)
              .userIsFriend(incomeInvitationUsername);
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void friendRequestsShouldBeDecline(UserJson user) {
        Selenide.open(MainPage.URL, MainPage.class);
        header.toFriendsPage()
              .declineFriendRequestByLoginOrName(user.testData()
                                                     .incomeInvitations()
                                                     .getFirst()
                                                     .username())
              .checkFriendsListEmpty();
    }
}
