package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.userdata.FriendshipEntity;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UdUserRepository;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class UdUserRepositoryJdbc implements UdUserRepository {

    private static final Config CFG = Config.getInstance();

    @Override
    public UserEntity createUser(UserEntity user) {
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

    public Optional<UserEntity> findUserByUsername(String username) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE username = ?"
        )) {
            ps.setString(1, username);
            ps.execute();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    UserEntity categoryEntity = convertToUserEntity(resultSet);

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
    public Optional<UserEntity> findUserById(UUID id) {
        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM \"user\" WHERE id = ? ")) {
            ps.setObject(1, id);

            ps.execute();
            ResultSet resultSet = ps.getResultSet();
            if (resultSet.next()) {
                UserEntity userEntity = convertToUserEntity(resultSet);

                return Optional.of(userEntity);
            } else {
                return Optional.empty();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addInvitation(UserEntity requester, UserEntity addressee) {
        requester = getUserOrCreateIfAbsent(requester);
        addressee = getUserOrCreateIfAbsent(addressee);

        try (PreparedStatement ps = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                "INSERT INTO friendship (requester_id, addressee_id, status) VALUES (?, ?, ?)"
        )) {
            ps.setObject(1, requester.getId());
            ps.setObject(2, addressee.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addFriend(UserEntity requester, UserEntity addressee) {
        UUID requesterId = getUserOrCreateIfAbsent(requester).getId();
        UUID addresseeId = getUserOrCreateIfAbsent(addressee).getId();
        String acceptedFriendshipStatus = FriendshipStatus.ACCEPTED.name();

        try (
                PreparedStatement outgoingRequestPs = holder(CFG.userdataJdbcUrl()).connection().prepareStatement(
                        "INSERT INTO friendship (requester_id, addressee_id, status) VALUES (?, ?, ?)")
        ) {
            outgoingRequestPs.setObject(1, requesterId);
            outgoingRequestPs.setObject(2, addresseeId);
            outgoingRequestPs.setString(3, acceptedFriendshipStatus);
            outgoingRequestPs.executeUpdate();

            outgoingRequestPs.setObject(1, addresseeId);
            outgoingRequestPs.setObject(2, requesterId);
            outgoingRequestPs.setString(3, acceptedFriendshipStatus);
            outgoingRequestPs.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private UserEntity convertToUserEntity(ResultSet resultSet) throws SQLException {
        String currency = resultSet.getString("currency");
        List<FriendshipEntity> friendshipRequests = new ArrayList<>();
        List<FriendshipEntity> friendshipAddressees = new ArrayList<>();

        return new UserEntity(
                resultSet.getObject("id", UUID.class),
                resultSet.getString("username"),
                CurrencyValues.valueOf(currency),
                resultSet.getString("firstname"),
                resultSet.getString("surname"),
                resultSet.getString("full_name"),
                resultSet.getBytes("photo"),
                resultSet.getBytes("photo_small"),
                friendshipRequests,
                friendshipAddressees
        );
    }

    public UserEntity getUserOrCreateIfAbsent(UserEntity userEntity) {
        return findUserByUsername(userEntity.getUsername())
                .orElseGet(() -> createUser(userEntity));
    }
}
