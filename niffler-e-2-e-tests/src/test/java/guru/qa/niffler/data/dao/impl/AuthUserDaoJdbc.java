package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.AuthUserDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import org.springframework.security.crypto.factory.PasswordEncoderFactories;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthUserDaoJdbc implements AuthUserDao {

    private static final Config CFG = Config.getInstance();

    private static final PasswordEncoder passwordEncoder = PasswordEncoderFactories.createDelegatingPasswordEncoder();

    private final Connection connection;

    public AuthUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthUserEntity createUser(AuthUserEntity user) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO \"user\" (username, password, enabled, account_non_expired, account_non_locked, credentials_non_expired) " +
                        "VALUES (?, ?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setString(1, user.getUsername());
            ps.setString(2, passwordEncoder.encode(user.getPassword()));
            ps.setBoolean(3, user.isEnabled());
            ps.setBoolean(4, user.isAccountNonExpired());
            ps.setBoolean(5, user.isAccountNonLocked());
            ps.setBoolean(6, user.isCredentialsNonExpired());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            user.setId(generatedKey);
            return user;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findById(UUID id) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet resultSet = ps.getResultSet()) {
                    if (resultSet.next()) {
                        AuthUserEntity authUserEntity = getAuthUserEntity(resultSet);

                        return Optional.of(authUserEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthUserEntity> findByUsername(String username) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE username = ?"
            )) {
                ps.setString(1, username);
                ps.execute();
                try (ResultSet resultSet = ps.getResultSet()) {
                    if (resultSet.next()) {
                        AuthUserEntity categoryEntity = getAuthUserEntity(resultSet);

                        return Optional.of(categoryEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void delete(AuthUserEntity user) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM \"user\" WHERE id = ?"
            )) {
                ps.setObject(1, user.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthUserEntity> findAll() {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\""
            )) {
                ps.execute();
                List<AuthUserEntity> resultList = new ArrayList<>();
                try (ResultSet resultSet = ps.getResultSet()) {
                    while (resultSet.next()) {
                        resultList.add(getAuthUserEntity(resultSet));
                    }

                    return resultList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthUserEntity getAuthUserEntity(ResultSet resultSet) throws SQLException {
        return new AuthUserEntity(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("username"),
                resultSet.getString("password"),
                resultSet.getBoolean("enabled"),
                resultSet.getBoolean("account_non_expired"),
                resultSet.getBoolean("account_non_locked"),
                resultSet.getBoolean("credentials_non_expired")
        );
    }
}
