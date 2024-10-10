package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.mapper.UdUserEntityRowMapper;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;

import javax.annotation.Nonnull;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UdUserDaoSpringJdbc implements UdUserDao {

    private static final Config CFG = Config.getInstance();

    private final String url = CFG.userdataJdbcUrl();

    @Override
    public UserEntity createUser(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        KeyHolder kh = new GeneratedKeyHolder();
        jdbcTemplate.update(con -> {
            PreparedStatement ps = con.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?,?,?,?,?,?,?)",
                    Statement.RETURN_GENERATED_KEYS
            );
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().name());
            ps.setString(3, user.getFirstname());
            ps.setString(4, user.getSurname());
            ps.setBytes(5, user.getPhoto());
            ps.setBytes(6, user.getPhotoSmall());
            ps.setString(7, user.getFullname());
            return ps;
        }, kh);

        final UUID generatedKey = (UUID) kh.getKeys().get("id");
        user.setId(generatedKey);
        return user;
    }

    @Override
    public Optional<UserEntity> findUserById(UUID id) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE id = ?",
                            UdUserEntityRowMapper.instance,
                            id
                    )
            );
        } catch (
                EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<UserEntity> findUserByUsername(String username) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        try {
            return Optional.ofNullable(
                    jdbcTemplate.queryForObject(
                            "SELECT * FROM \"user\" WHERE username = ?",
                            UdUserEntityRowMapper.instance,
                            username
                    )
            );
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public void deleteUser(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        jdbcTemplate.queryForObject(
                "DELETE FROM \"user\" WHERE id = ?",
                UdUserEntityRowMapper.instance,
                user.getId()
        );
    }

    @Override
    public List<UserEntity> findAllUsers() {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        return jdbcTemplate.query(
                "SELECT * FROM \"user\"",
                UdUserEntityRowMapper.instance
        );
    }

    public UserEntity update(UserEntity user) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(url));
        jdbcTemplate.update("""
                                      UPDATE "user"
                                        SET currency    = ?,
                                            firstname   = ?,
                                            surname     = ?,
                                            photo       = ?,
                                            photo_small = ?
                                        WHERE id = ?
                        """,
                user.getCurrency().name(),
                user.getFirstname(),
                user.getSurname(),
                user.getPhoto(),
                user.getPhotoSmall(),
                user.getId());

        jdbcTemplate.batchUpdate("""
                                         INSERT INTO friendship (requester_id, addressee_id, status)
                                         VALUES (?, ?, ?)
                                         ON CONFLICT (requester_id, addressee_id)
                                             DO UPDATE SET status = ?
                        """,
                new BatchPreparedStatementSetter() {
                    @Override
                    public void setValues(@Nonnull PreparedStatement ps, int i) throws SQLException {
                        ps.setObject(1, user.getId());
                        ps.setObject(2, user.getFriendshipRequests().get(i).getAddressee().getId());
                        ps.setString(3, user.getFriendshipRequests().get(i).getStatus().name());
                        ps.setString(4, user.getFriendshipRequests().get(i).getStatus().name());
                    }

                    @Override
                    public int getBatchSize() {
                        return user.getFriendshipRequests().size();
                    }
                });
        return user;
    }
}
