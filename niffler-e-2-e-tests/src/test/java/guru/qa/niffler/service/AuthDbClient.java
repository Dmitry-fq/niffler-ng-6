package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();

    private final AuthUserDao authUserDao = new AuthUserDaoSpringJdbc();

    private final AuthAuthorityDao authAuthorityDao = new AuthAuthorityDaoSpringJdbc();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.authJdbcUrl(),
            CFG.userdataJdbcUrl()
    );

    public void createAuthority(AuthorityEntity... authority) throws SQLException {
        authAuthorityDao.createAuthority(authority);
    }

    public Optional<AuthorityEntity> findAuthorityById(UUID id) {
        return authAuthorityDao.findAuthorityById(id);
    }

    public List<AuthorityEntity> findAuthorityByUserId(UUID userId) {
        return authAuthorityDao.findAuthoritiesByUserId(userId);
    }

    public void deleteAuthority(AuthorityEntity user) {
        authAuthorityDao.deleteAuthority(user);
    }

    public List<AuthorityEntity> findAllAuthorities() {
        return authAuthorityDao.findAllAuthorities();
    }

    public AuthUserEntity createUser(AuthUserEntity user) {
        return authUserDao.createUser(user);
    }

    public Optional<AuthUserEntity> findUserById(UUID id) {
        return authUserDao.findUserById(id);
    }

    public Optional<AuthUserEntity> findUserByUsername(String username) {
        return authUserDao.findUserByUsername(username);
    }

    public void deleteUser(AuthUserEntity user) {
        authUserDao.deleteUser(user);
    }

    public List<AuthUserEntity> findAllUsers() {
        return authUserDao.findAllUsers();
    }
}
