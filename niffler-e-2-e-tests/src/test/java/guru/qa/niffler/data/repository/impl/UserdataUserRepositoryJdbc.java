package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UserdataUserDaoJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.Connections.holder;

@ParametersAreNonnullByDefault
public class UserdataUserRepositoryJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    private final UserdataUserDaoJdbc udUserDaoJdbc = new UserdataUserDaoJdbc();

    @Nonnull
    @Override
    public UserEntity create(UserEntity user) {
        return udUserDaoJdbc.create(user);
    }

    @Nonnull
    @Override
    public UserEntity update(UserEntity user) {
        return null;
    }

    @Nonnull
    @Override
    public Optional<UserEntity> findById(UUID id) {
        return udUserDaoJdbc.findById(id);
    }

    @Nonnull
    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return udUserDaoJdbc.findByUsername(username);
    }

    @Override
    public void addFriendshipRequest(UserEntity requester, UserEntity addressee) {
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

    @Override
    public void remove(UserEntity user) {
        udUserDaoJdbc.deleteUser(user);
    }

    public UserEntity getUserOrCreateIfAbsent(UserEntity userEntity) {
        return findByUsername(userEntity.getUsername())
                .orElseGet(() -> create(userEntity));
    }
}
