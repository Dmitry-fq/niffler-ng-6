package guru.qa.niffler.service.impl;

import com.google.common.base.Stopwatch;
import guru.qa.niffler.api.UsersApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import io.qameta.allure.Step;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;
import static org.assertj.core.api.Assertions.assertThat;

public class UsersApiClient extends RestClient implements UsersClient {

    private static final String DEFAULT_PASSWORD = "12345";

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
                    return userJson.addTestData(
                            new TestData(password)
                    );
                } else {
                    Thread.sleep(1000);
                }
            }
        } catch (IOException | InterruptedException e) {
            throw new RuntimeException(e);
        }
        throw new AssertionError("Пользователь " + username + " не был найден за отведённое время");
    }

    @Nonnull
    @Override
    public Optional<UserJson> findUserById(@NotNull UUID id) {
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
    public List<UserJson> addIncomeInvitation(@NotNull UserJson targetUser, int count) {
        if (count > 0) {
            List<UserJson> incomeUsers = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String randomUserName = randomUsername();
                UserJson createdUser;
                createdUser = createUser(randomUserName, DEFAULT_PASSWORD);
                sendInvitation(createdUser.username(), targetUser.username());
                incomeUsers.add(createdUser);

                targetUser.testData().incomeInvitation().add(createdUser);
            }
            return incomeUsers;
        }
        return Collections.emptyList();
    }

    @Nonnull
    @Override
    public List<UserJson> addOutcomeInvitation(@NotNull UserJson targetUser, int count) {
        if (count > 0) {
            List<UserJson> outcomeUsers = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String randomUserName = randomUsername();
                UserJson createdUser;
                createdUser = createUser(randomUserName, DEFAULT_PASSWORD);
                sendInvitation(targetUser.username(), createdUser.username());
                outcomeUsers.add(createdUser);

                createdUser.testData().outcomeInvitation().add(createdUser);
            }
            return outcomeUsers;
        }
        return Collections.emptyList();
    }

    @Step("Отправка приглашения от пользователя {username} пользователю {targetUsername}")
    @Nullable
    public UserJson sendInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersApi.sendInvitation(username, targetUsername).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    @Nonnull
    @Override
    public List<UserJson> addFriends(@NotNull UserJson targetUser, int count) {
        if (count > 0) {
            List<UserJson> friends = new ArrayList<>();
            for (int i = 0; i < count; i++) {
                String randomUserName = randomUsername();
                UserJson createdUser;
                createdUser = createUser(randomUserName, DEFAULT_PASSWORD);
                sendInvitation(targetUser.username(), createdUser.username());
                acceptInvitation(createdUser.username(), targetUser.username());
                friends.add(createdUser);

                targetUser.testData().friends().addAll(friends);
            }
            return friends;
        }
        return Collections.emptyList();
    }

    @Step("Принятие приглашения от пользователя {username} пользователем {targetUsername}")
    @Nullable
    public UserJson acceptInvitation(@Nonnull String username, @Nonnull String targetUsername) {
        final Response<UserJson> response;
        try {
            response = usersApi.acceptInvitation(username, targetUsername)
                               .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
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
