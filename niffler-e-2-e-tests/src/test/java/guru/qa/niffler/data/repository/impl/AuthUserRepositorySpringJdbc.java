package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoSpringJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.repository.AuthUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Optional;
import java.util.UUID;

public class AuthUserRepositorySpringJdbc implements AuthUserRepository {

    private static final Config CFG = Config.getInstance();

    private final AuthUserDaoSpringJdbc authUserDaoSpringJdbc = new AuthUserDaoSpringJdbc();

    private final AuthAuthorityDaoSpringJdbc authAuthorityDaoSpringJdbc = new AuthAuthorityDaoSpringJdbc();

    @NotNull
    @Override
    public AuthUserEntity create(AuthUserEntity user) {
        AuthUserEntity authUserEntity = authUserDaoSpringJdbc.createUser(user);
        authAuthorityDaoSpringJdbc.createAuthority(authUserEntity.getAuthorities().toArray(new AuthorityEntity[0]));

        return authUserEntity;
    }

    @NotNull
    @Override
    public AuthUserEntity update(AuthUserEntity user) {
        return null;
    }

    @NotNull
    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.queryForObject(
                        "SELECT * FROM \"user\" u join authority a on u.id = a.user_id where u.id = ?",
                        AuthUserEntityRowMapper.instance,
                        id
                )
        );
    }

    @NotNull
    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        return authUserDaoSpringJdbc.findUserByUsername(username);
    }

    @Override
    public void remove(AuthUserEntity user) {
        authUserDaoSpringJdbc.deleteUser(user);
    }
}
