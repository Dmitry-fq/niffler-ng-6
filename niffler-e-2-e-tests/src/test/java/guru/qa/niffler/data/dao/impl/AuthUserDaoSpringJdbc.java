package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.mapper.AuthUserEntityExtractor;
import guru.qa.niffler.data.mapper.AuthUserEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.jetbrains.annotations.NotNull;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoSpringJdbc implements AuthUserDao {

    private static final Config CFG = Config.getInstance();

    @NotNull
    @Override
    public AuthUserEntity createUser(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                            "VALUES (?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getPassword());
            ps.setBoolean(3, user.getEnabled());
            ps.setBoolean(4, user.getAccountNonExpired());
            ps.setBoolean(5, user.getAccountNonLocked());
            ps.setBoolean(6, user.getCredentialsNonExpired());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) Objects.requireNonNull(kh.getKeys()).get("id");
        user.setId(generatedKey);
        return user;
    }

    @NotNull
    @Override
    public Optional<AuthUserEntity> findUserById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE id = ?",
                            AuthUserEntityRowMapper.instance,
                            id
                    )
            );
        } catch (
                EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    public Optional<AuthUserEntity> findUserWithAuthorityByUserId(UUID userId) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return Optional.ofNullable(
                jdbcTemplate.query(
                        """
                                SELECT a.id as authority_id,
                                    authority,
                                    user_id as id,
                                    u.username,
                                    u.password,
                                    u.enabled,
                                    u.account_non_expired,
                                    u.account_non_locked,
                                    u.credentials_non_expired
                                FROM "user" u
                                JOIN public.authority a on u.id = a.user_id
                                WHERE u.id = ?
                                """,
                        AuthUserEntityExtractor.instance,
                        userId
                )
        );
    }

    @NotNull
    @Override
    public Optional<AuthUserEntity> findUserByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE username = ?",
                            AuthUserEntityRowMapper.instance,
                            username
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteUser(AuthUserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        jdbcTemplate.queryForObject(
                "DELETE FROM \"user\" WHERE id = ?",
                AuthUserEntityRowMapper.instance,
                user.getId()
        );
    }

    @NotNull
    @Override
    public List<AuthUserEntity> findAllUsers() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.authJdbcUrl()));
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                AuthUserEntityRowMapper.instance
        );
    }
}
