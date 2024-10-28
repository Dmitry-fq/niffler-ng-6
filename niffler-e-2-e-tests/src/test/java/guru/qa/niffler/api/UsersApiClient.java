package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class UsersApiClient implements UsersClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().userdataUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final UsersApi usersApi = retrofit.create(UsersApi.class);

    @Override
    public UserJson createUser(String username, String password) {
        throw new UnsupportedOperationException("Действие не поддерживается в API");
    }

    @Override
    public Optional<UserJson> findUserById(UUID id) {
        throw new UnsupportedOperationException("Действие не поддерживается в API");
    }

    @Override
    public Optional<UserJson> findUserByUsername(String username) {
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

    @Override
    public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Действие не поддерживается в API, т.к. невозможно создать юзера");
    }

    @Override
    public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Действие не поддерживается в API, т.к. невозможно создать юзера");
    }

    @Override
    public List<UserJson> addFriends(UserJson targetUser, int count) {
        throw new UnsupportedOperationException("Действие не поддерживается в API, т.к. невозможно создать юзера");
    }
}
