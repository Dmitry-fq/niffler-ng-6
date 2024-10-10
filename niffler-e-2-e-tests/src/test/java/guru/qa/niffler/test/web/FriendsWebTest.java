package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.jupiter.extension.UsersQueueExtension;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.StaticUser;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsShouldBePresentInFriendsTable(@UserType(value = WITH_FRIEND) StaticUser user) {
        String username = user.username();
        String password = user.password();
        String friend = user.friend();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickFriends()
                .checkFriends(friend);
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void friendsTableShouldBeEmptyForNewUser(@UserType(value = EMPTY) StaticUser user) {
        String username = user.username();
        String password = user.password();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickFriends()
                .checkFriendsListEmpty();
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void incomeInvitationBePresentInFriendsTable(@UserType(value = WITH_INCOME_REQUEST) StaticUser user) {
        String username = user.username();
        String password = user.password();
        String incomeFriendRequestUsername = user.income();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickFriends()
                .checkIncomeFriendRequest(incomeFriendRequestUsername);
    }

    @Test
    @ExtendWith(UsersQueueExtension.class)
    void outcomeInvitationBePresentInAllPeoplesTable(@UserType(value = WITH_OUTCOME_REQUEST) StaticUser user) {
        String username = user.username();
        String password = user.password();
        String outcomeFriendRequestUsername = user.outcome();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickFriends()
                .clickAllPeopleTab()
                .checkOutcomeFriendRequest(outcomeFriendRequestUsername);
    }
}
