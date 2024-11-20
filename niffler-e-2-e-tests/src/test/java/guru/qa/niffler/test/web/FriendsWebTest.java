package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.component.Header;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    private final Header header = new Header();

    @User(
            friends = 1
    )
    @Test
    void friendsShouldBePresentInFriendsTable(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        List<UserJson> friendNameList = user.testData().friends();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toFriendsPage()
              .checkFriends(friendNameList.getFirst().firstname());
    }

    @User
    @Test
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        String username = user.username();
        String password = user.testData().password();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toFriendsPage()
              .checkFriendsListEmpty();
    }

    @User(
            incomeInvitation = 1
    )
    @Test
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        List<UserJson> incomeInvitationUsernameList = user.testData().incomeInvitation();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toFriendsPage()
              .checkIncomeInvitation(incomeInvitationUsernameList.getFirst().username());
    }

    @User(
            outcomeInvitation = 1
    )
    @Test
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        List<UserJson> outcomeInvitationUsernameList = user.testData().outcomeInvitation();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toFriendsPage()
              .clickAllPeopleTab()
              .checkOutcomeFriendRequest(outcomeInvitationUsernameList.getFirst().username());
    }

    @User(
            incomeInvitation = 1
    )
    @Test
    void friendRequestsShouldBeAccept(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        String incomeInvitationUsername = user.testData().incomeInvitation().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toFriendsPage()
              .acceptFriendRequestByLoginOrName(incomeInvitationUsername)
              .userIsFriend(incomeInvitationUsername);
    }

    @User(
            incomeInvitation = 1
    )
    @Test
    void friendRequestsShouldBeDecline(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        String incomeInvitationUsername = user.testData().incomeInvitation().getFirst().username();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toFriendsPage()
              .declineFriendRequestByLoginOrName(incomeInvitationUsername)
              .checkFriendsListEmpty();
    }
}
