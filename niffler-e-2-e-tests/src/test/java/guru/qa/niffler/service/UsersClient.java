package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersClient {

    UserJson createUser(String username, String password);

    Optional<UserJson> findUserById(UUID id);

    Optional<UserJson> findUserByUsername(String username);

    List<UserJson> addIncomeInvitation(UserJson targetUser, int count);

    List<UserJson> addOutcomeInvitation(UserJson targetUser, int count);

    List<UserJson> addFriends(UserJson targetUser, int count);
}
