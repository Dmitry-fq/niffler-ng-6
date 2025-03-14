package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.repository.impl.AuthUserRepositoryHibernate;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.rest.CurrencyValues;
import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.UsersClient;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.utils.RandomDataUtils.randomUsername;

@ParametersAreNonnullByDefault
public class UsersDbClient implements UsersClient {

    private static final Config CFG = Config.getInstance();

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserRepository authUserRepository = new AuthUserRepositoryHibernate();

    private final UserdataUserRepository userdataUserRepository = new UserdataUserRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    @Nonnull
    @Override
    public UserJson createUser(String username, String password) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = authUserEntity(username, password);
                    authUserRepository.create(authUser);
                    return UserJson.fromEntity(
                            userdataUserRepository.create(userEntity(username)),
                            null
                    );
                }
        );
    }

    @Nonnull
    public Optional<UserJson> findUserById(UUID id) {
        return userdataUserRepository.findById(id)
                                     .map(userEntity -> UserJson.fromEntity(userEntity, null));
    }

    @Nonnull
    public Optional<UserJson> findUserByUsername(String username) {
        return userdataUserRepository.findByUsername(username)
                                     .map(userEntity -> UserJson.fromEntity(userEntity, null));
    }

    @Nonnull
    @Override
    public List<UserJson> addIncomeInvitation(UserJson targetUser, int count) {
        List<UserJson> incomeInvitations = new ArrayList<>();

        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            UserEntity user = createNewUser(randomUsername(), "12345");
                            userdataUserRepository.addFriendshipRequest(
                                    user,
                                    targetEntity
                            );
                            incomeInvitations.add(UserJson.fromEntity(user, null));

                            return null;
                        }
                );
            }
        }
        return incomeInvitations;
    }

    @Nonnull
    @Override
    public List<UserJson> addOutcomeInvitation(UserJson targetUser, int count) {
        List<UserJson> outcomeInvitations = new ArrayList<>();

        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            UserEntity user = createNewUser(randomUsername(), "12345");
                            userdataUserRepository.addFriendshipRequest(
                                    targetEntity,
                                    user
                            );
                            outcomeInvitations.add(UserJson.fromEntity(user, null));

                            return null;
                        }
                );
            }
        }
        return outcomeInvitations;
    }

    @Nonnull
    @Override
    public List<UserJson> addFriends(UserJson targetUser, int count) {
        List<UserJson> friends = new ArrayList<>();

        if (count > 0) {
            UserEntity targetEntity = userdataUserRepository.findById(
                    targetUser.id()
            ).orElseThrow();

            for (int i = 0; i < count; i++) {
                xaTransactionTemplate.execute(() -> {
                            UserEntity friend = createNewUser(randomUsername(), "12345");
                            userdataUserRepository.addFriend(
                                    targetEntity,
                                    friend
                            );
                            friends.add(UserJson.fromEntity(friend, null));

                            return null;
                        }
                );
            }
        }

        return friends;
    }

    @Nonnull
    private UserEntity createNewUser(String username, String password) {
        AuthUserEntity authUser = authUserEntity(username, password);
        authUserRepository.create(authUser);
        return userdataUserRepository.create(userEntity(username));
    }

    @Nonnull
    private UserEntity userEntity(String username) {
        UserEntity ue = new UserEntity();
        ue.setUsername(username);
        ue.setCurrency(CurrencyValues.RUB);
        return ue;
    }

    @Nonnull
    private AuthUserEntity authUserEntity(String username, String password) {
        AuthUserEntity authUser = new AuthUserEntity();
        authUser.setUsername(username);
        authUser.setPassword(pe.encode(password));
        authUser.setEnabled(true);
        authUser.setAccountNonExpired(true);
        authUser.setAccountNonLocked(true);
        authUser.setCredentialsNonExpired(true);
        authUser.setAuthorities(
                Arrays.stream(Authority.values()).map(
                        e -> {
                            AuthorityEntity ae = new AuthorityEntity();
                            ae.setUser(authUser);
                            ae.setAuthority(e);
                            return ae;
                        }
                ).toList()
        );
        return authUser;
    }
}
