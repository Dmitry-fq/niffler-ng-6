package guru.qa.niffler.test.rest;

import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.RestTest;
import guru.qa.niffler.jupiter.extension.ApiLoginExtension;
import guru.qa.niffler.model.rest.FriendJson;
import guru.qa.niffler.model.rest.FriendshipStatus;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.GatewayApiClient;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RestTest
public class FriendsTest {

    @RegisterExtension
    private static final ApiLoginExtension apiLoginExtension = ApiLoginExtension.rest();

    private final GatewayApiClient gatewayApiClient = new GatewayApiClient();

    private final AuthApiClient authApiClient = new AuthApiClient();

    @User(friends = 2, incomeInvitations = 1)
    @ApiLogin
    @Test
    void allFriendsAndIncomeInvitationsShouldBeReturnedFroUser(UserJson user, @Token String token) {
        final List<String> expectedFriendUsernames = user.testData().friends().stream()
                                                         .map(UserJson::username)
                                                         .toList();
        final List<UserJson> expectedInvitations = user.testData().incomeInvitations();

        final List<UserJson> result = gatewayApiClient.allFriends(
                token,
                null
        );

        Assertions.assertNotNull(result);
        Assertions.assertEquals(3, result.size());

        final List<UserJson> friendsFromResponse = result.stream().filter(
                u -> u.friendshipStatus() == FriendshipStatus.FRIEND
        ).toList();

        final List<UserJson> invitationsFromResponse = result.stream().filter(
                u -> u.friendshipStatus() == FriendshipStatus.INVITE_RECEIVED
        ).toList();

        Assertions.assertEquals(2, friendsFromResponse.size());
        Assertions.assertEquals(1, invitationsFromResponse.size());

        Assertions.assertEquals(
                expectedInvitations.getFirst().username(),
                invitationsFromResponse.getFirst().username()
        );

        List<String> friendUsernamesFromResponse = friendsFromResponse.stream().map(UserJson::username).toList();
        assertThat(expectedFriendUsernames).containsExactlyInAnyOrderElementsOf(friendUsernamesFromResponse);
    }

    @User(friends = 1)
    @ApiLogin
    @Test
    void friendshipShouldBeDeleted(UserJson user, @Token String token) {
        String friend = user.testData().friends().getFirst().username();
        gatewayApiClient.removeFriend(token, friend);
        final List<UserJson> friendListAfterRemoving = gatewayApiClient.allFriends(token, null);

        assertThat(friendListAfterRemoving).isEmpty();
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void friendshipShouldBeAccepted(UserJson user, @Token String token) {
        final String usernameToInvite = user.testData().incomeInvitations().getFirst().username();
        gatewayApiClient.acceptInvitation(token, new FriendJson(usernameToInvite));
        final List<UserJson> friendListAfterAccepted = gatewayApiClient.allFriends(token, null);

        assertThat(friendListAfterAccepted.getFirst().username()).isEqualTo(usernameToInvite);
    }

    @User(incomeInvitations = 1)
    @ApiLogin
    @Test
    void friendshipShouldBeDeclined(UserJson user, @Token String token) {
        final String usernameToInvite = user.testData().incomeInvitations().getFirst().username();
        gatewayApiClient.declineInvitation(token, new FriendJson(usernameToInvite));
        final List<UserJson> friendListAfterDeclined = gatewayApiClient.allFriends(token, null);

        assertThat(friendListAfterDeclined).isEmpty();
    }

    @User(outcomeInvitations = 1)
    @ApiLogin
    @Test
    void afterSentFriendshipShouldBeIncomeAndOutcomeInvitations(UserJson user, @Token String token) {
        final String usernameToInvite = user.testData().outcomeInvitations().getFirst().username();

        ThreadSafeCookieStore.INSTANCE.removeAll();
        String userToInviteToken = authApiClient.login(usernameToInvite, "12345");
        final List<UserJson> allFriends = gatewayApiClient.allFriends("Bearer " + userToInviteToken, null);

        final List<UserJson> incomeInvitationUsers = allFriends.stream()
                                                               .filter(u -> u.friendshipStatus() == FriendshipStatus.INVITE_RECEIVED)
                                                               .toList();

        assertThat(incomeInvitationUsers.size()).isEqualTo(1);
        assertThat(incomeInvitationUsers.getFirst().username()).isEqualTo(user.username());
    }
}
