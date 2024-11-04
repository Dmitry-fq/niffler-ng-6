package guru.qa.niffler.service;

import guru.qa.niffler.model.UserJson;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UsersClient {

    @NonNull
    UserJson createUser(String username, String password);

    @NonNull
    Optional<UserJson> findUserById(UUID id);

    @NonNull
    Optional<UserJson> findUserByUsername(String username);

    @NonNull
    List<UserJson> addIncomeInvitation(UserJson targetUser, int count);

    @NonNull
    List<UserJson> addOutcomeInvitation(UserJson targetUser, int count);

    @NonNull
    List<UserJson> addFriends(UserJson targetUser, int count);
}
