package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

    private static final Config CFG = Config.getInstance();

    private Connection connection;

    public AuthAuthorityDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public AuthorityEntity createAuthority(AuthorityEntity authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) " +
                        "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            ps.setObject(1, authority.getUser().getId());
            ps.setString(2, authority.getAuthority().name());

            ps.executeUpdate();

            final UUID generatedKey;
            try (ResultSet resultSet = ps.getGeneratedKeys()) {
                if (resultSet.next()) {
                    generatedKey = resultSet.getObject("id", UUID.class);
                } else {
                    throw new SQLException("Can`t find id in ResultSet");
                }
            }
            authority.setId(generatedKey);
            return authority;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void createAuthorities(AuthorityEntity... authority) {
        try (PreparedStatement ps = connection.prepareStatement(
                "INSERT INTO authority (user_id, authority) " +
                        "VALUES (?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUser().getId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthorityEntity> findById(UUID id) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM authority WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet resultSet = ps.getResultSet()) {
                    if (resultSet.next()) {
                        AuthorityEntity authorityEntity = getAuthAuthorityEntity(resultSet);

                        return Optional.of(authorityEntity);
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
    public Optional<AuthorityEntity> findByUserId(UUID userId) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM authority WHERE user_id = ?"
            )) {
                ps.setObject(1, userId);
                ps.execute();
                try (ResultSet resultSet = ps.getResultSet()) {
                    if (resultSet.next()) {
                        AuthorityEntity categoryEntity = getAuthAuthorityEntity(resultSet);

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
    public void delete(AuthorityEntity user) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM authority WHERE id = ?"
            )) {
                ps.setObject(1, user.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAll() {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM authority"
            )) {
                ps.execute();
                List<AuthorityEntity> resultList = new ArrayList<>();
                try (ResultSet resultSet = ps.getResultSet()) {
                    while (resultSet.next()) {
                        resultList.add(getAuthAuthorityEntity(resultSet));
                    }

                    return resultList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private AuthorityEntity getAuthAuthorityEntity(ResultSet resultSet) throws SQLException {
        return new AuthorityEntity(
                resultSet.getObject("id", UUID.class),
                resultSet.getObject("user_id", AuthUserEntity.class),
                resultSet.getObject("authority", Authority.class)
        );
    }
}
