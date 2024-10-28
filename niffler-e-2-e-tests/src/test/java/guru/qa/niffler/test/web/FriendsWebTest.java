package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

@ExtendWith(BrowserExtension.class)
public class FriendsWebTest {

    private static final Config CFG = Config.getInstance();

    @Test
    @User(
            friends = 1
    )
    void friendsShouldBePresentInFriendsTable(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        List<String> friendNameList = user.testData().friends();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickFriends()
                .checkFriends(friendNameList.getFirst());
    }

    @Test
    @User
    void friendsTableShouldBeEmptyForNewUser(UserJson user) {
        String username = user.username();
        String password = user.testData().password();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickFriends()
                .checkFriendsListEmpty();
    }

    @Test
    @User(
            incomeInvitation = 1
    )
    void incomeInvitationBePresentInFriendsTable(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        List<String> incomeInvitationUsernameList = user.testData().incomeInvitation();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickFriends()
                .checkIncomeInvitation(incomeInvitationUsernameList.getFirst());
    }

    @Test
    @User(
            outcomeInvitation = 1
    )
    void outcomeInvitationBePresentInAllPeoplesTable(UserJson user) {
        String username = user.username();
        String password = user.testData().password();
        List<String> outcomeInvitationUsernameList = user.testData().outcomeInvitation();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickFriends()
                .clickAllPeopleTab()
                .checkOutcomeFriendRequest(outcomeInvitationUsernameList.getFirst());
    }
}
