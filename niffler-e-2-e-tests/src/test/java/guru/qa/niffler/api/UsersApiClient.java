package guru.qa.niffler.api;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersApiClient extends RestClient implements UsersClient {

    private final UsersApi usersApi;

    private final AuthApiClient authApiClient = new AuthApiClient();

    public UsersApiClient() {
        super(CFG.userdataUrl());
        this.usersApi = retrofit.create(UsersApi.class);
    }

    @Nonnull
    @Override
    public UserJson createUser(@NotNull String username, @NotNull String password) {
        try {
            authApiClient.getRegisterPage();
            authApiClient.registerUser(
                    username,
                    password,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")
            );
            int maxWaitTime = 3000;
            Stopwatch sw = Stopwatch.createStarted();
            while (sw.elapsed(TimeUnit.MILLISECONDS) < maxWaitTime) {
                UserJson userJson = usersApi.currentUser(username).execute().body();
                if (userJson != null && userJson.id() != null) {
                    return userJson;
                } else {
                    Thread.sleep(100);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new AssertionError("Пользователь " + username + " не был найден за отведённое время");
    }

    @Nonnull
    @Override
    public Optional<UserJson> findUserById(UUID id) {
        throw new UnsupportedOperationException("Действие не поддерживается в API");
    }

    @Nonnull
    @Override
    public Optional<UserJson> findUserByUsername(@NotNull String username) {
        final Response<UserJson> response;
        try {
            response = usersApi.currentUser(username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return Optional.ofNullable(response.body());
    }

    @Nonnull
    @Override
    public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Действие не поддерживается в API, т.к. невозможно создать юзера");
    }

    @Nonnull
    @Override
    public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Действие не поддерживается в API, т.к. невозможно создать юзера");
    }

    @Nonnull
    @Override
    public List<UserJson> addFriends(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Действие не поддерживается в API, т.к. невозможно создать юзера");
    }

    @Nonnull
    public List<UserJson> getAllUsersByUsernameAndSearchQuery(@Nonnull String username, @Nullable String searchQuery) {
        final Response<List<UserJson>> response;
        try {
            response = usersApi.allUsers(username, searchQuery)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        List<UserJson> userJsons = response.body();

        return userJsons != null
                ? userJsons
                : Collections.emptyList();
    }
}
