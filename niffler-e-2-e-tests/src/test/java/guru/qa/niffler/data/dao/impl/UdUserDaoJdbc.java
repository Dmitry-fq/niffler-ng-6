package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.Databases;
import guru.qa.niffler.data.dao.UdUserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class UdUserDaoJdbc implements UdUserDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public UdUserDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public UserEntity createUser(UserEntity user) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO \"user\" (username, currency, firstname, surname, photo, photo_small, full_name) " +
                            "VALUES (?, ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, user.getUsername());
                ps.setString(2, user.getCurrency().toString());
                ps.setString(3, user.getFirstname());
                ps.setString(4, user.getSurname());
                ps.setBytes(5, user.getPhoto());
                ps.setBytes(6, user.getPhotoSmall());
                ps.setString(7, user.getFullname());

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
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE id = ?"
            )) {
                ps.setObject(1, id);
                ps.execute();
                try (ResultSet resultSet = ps.getResultSet()) {
                    if (resultSet.next()) {
                        UserEntity userEntity = getUserEntity(resultSet);

                        return Optional.of(userEntity);
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
    public Optional<UserEntity> findByUsername(String username) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.userdataJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\" WHERE username = ?"
            )) {
                ps.setString(1, username);
                ps.execute();
                try (ResultSet resultSet = ps.getResultSet()) {
                    if (resultSet.next()) {
                        UserEntity categoryEntity = getUserEntity(resultSet);

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
    public void delete(UserEntity user) {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.userdataJdbcUrl())) {
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
    public List<UserEntity> findAll() {
        try (Connection connection = Databases.connectionWithoutTransaction(CFG.authJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM \"user\""
            )) {
                ps.execute();
                List<UserEntity> resultList = new ArrayList<>();
                try (ResultSet resultSet = ps.getResultSet()) {
                    while (resultSet.next()) {
                        resultList.add(getUserEntity(resultSet));
                    }

                    return resultList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity getUserEntity(ResultSet resultSet) throws SQLException {
        String currency = resultSet.getString("currency");

        return new UserEntity(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("username"),
                CurrencyValues.valueOf(currency),
                resultSet.getString("firstname"),
                resultSet.getString("surname"),
                resultSet.getString("full_name"),
                resultSet.getBytes("photo"),
                resultSet.getBytes("photo_small")
        );
    }
}
