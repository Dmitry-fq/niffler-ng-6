package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.UserdataUserDao;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.Connections.holder;

@ParametersAreNonnullByDefault
public class UserdataUserDaoJdbc implements UserdataUserDao {

    private static final Config CFG = Config.getInstance();

    @SuppressWarnings("resource")
    @Nonnull
    @Override
    public UserEntity create(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO \"user\" (username, currency) VALUES (?, ?)",
                PreparedStatement.RETURN_GENERATED_KEYS)) {
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getCurrency().toString());

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

    @SuppressWarnings("resource")
    @Nonnull
    @Override
    public Optional<UserEntity> findById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ? ")) {
            ps.setObject(1, id);

            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if (resultSet.next()) {
                UserEntity userEntity = convertToEntity(resultSet);

                return Optional.of(userEntity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("resource")
    @Nonnull
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setString(1, username);
            ps.execute();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    UserEntity categoryEntity = convertToEntity(resultSet);

                    return Optional.of(categoryEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteUser(UserEntity user) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "DELETE FROM \"user\" WHERE id = ?"
        )) {
            ps.setObject(1, user.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @SuppressWarnings("resource")
    @Nonnull
    @Override
    public List<UserEntity> findAllUsers() {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\""
        )) {
            ps.execute();
            List<UserEntity> resultList = new ArrayList<>();
            try (ResultSet resultSet = ps.getResultSet()) {
                while (resultSet.next()) {
                    resultList.add(convertToEntity(resultSet));
                }

                return resultList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity convertToEntity(ResultSet resultSet) throws SQLException {
        String currency = resultSet.getString("currency");

        return new UserEntity(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("username"),
                CurrencyValues.valueOf(currency),
                resultSet.getString("firstname"),
                resultSet.getString("surname"),
                resultSet.getString("full_name"),
                resultSet.getBytes("photo"),
                resultSet.getBytes("photo_small"),
                new ArrayList<>(),
                new ArrayList<>()
        );
    }
}