package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.UserJson;
import org.springframework.jdbc.support.JdbcTransactionManager;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.support.TransactionTemplate;

import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.DataSources.dataSource;

public class UsersDbClient {

    private static final Config CFG = Config.getInstance();

    private static final PasswordEncoder pe = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();

    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

    private final UdUserDao udUserDao = new UdUserDaoSpringJdbc();

    private final TransactionTemplate txTemplate = new TransactionTemplate(
            new JdbcTransactionManager(
                    dataSource(CFG.authJdbcUrl())
            )
    );

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public UserJson createUser(UserJson user) {
        return xaTransactionTemplate.execute(() -> {
                    AuthUserEntity authUser = new AuthUserEntity();
                    authUser.setUsername(user.username());
                    authUser.setPassword(pe.encode("12345"));
                    authUser.setEnabled(true);
                    authUser.setAccountNonExpired(true);
                    authUser.setAccountNonLocked(true);
                    authUser.setCredentialsNonExpired(true);

                    AuthUserEntity createdAuthUser = authUserDao.createUser(authUser);

                    AuthorityEntity[] authorityEntities = Arrays.stream(Authority.values()).map(
                            e -> {
                                AuthorityEntity ae = new AuthorityEntity();
                                ae.setUser(createdAuthUser);
                                ae.setAuthority(e);
                                return ae;
                            }
                    ).toArray(AuthorityEntity[]::new);

                    try {
                        authAuthorityDao.createAuthority(authorityEntities);
                    } catch (SQLException e) {
                        throw new RuntimeException(e);
                    }
                    return UserJson.fromEntity(
                            udUserDao.createUser(UserEntity.fromJson(user)),
                            null
                    );
                }
        );
    }

    public Optional<UserEntity> findUserById(UUID id) {
        return udUserDao.findUserById(id);
    }

    public Optional<UserEntity> findUserByUsername(String username) {
        return udUserDao.findUserByUsername(username);
    }

    public void deleteUser(UserEntity user) {
        udUserDao.deleteUser(user);
    }

    public List<UserEntity> findAllUsers() {
        return udUserDao.findAllUsers();
    }
}
