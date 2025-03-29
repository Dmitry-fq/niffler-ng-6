package guru.qa.niffler.service;

import guru.qa.niffler.model.rest.UserJson;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface UsersClient {

    @Nonnull
    UserJson createUser(String username, String password);

    @Nonnull
    Optional<UserJson> findUserById(UUID id);

    @Nonnull
    Optional<UserJson> findUserByUsername(String username);

    @Nonnull
    List<UserJson> addIncomeInvitation(UserJson targetUser, int count);

    @Nonnull
    List<UserJson> addOutcomeInvitation(UserJson targetUser, int count);

    @Nonnull
    List<UserJson> addFriends(UserJson targetUser, int count);
}
