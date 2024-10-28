package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.UdUserDaoSpringJdbc;
import guru.qa.niffler.data.entity.userdata.FriendshipStatus;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.UserdataUserRepository;
import guru.qa.niffler.data.tpl.DataSources;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.Connections.holder;

public class UserdataUserRepositorySpringJdbc implements UserdataUserRepository {

    private static final Config CFG = Config.getInstance();

    private final UdUserDaoSpringJdbc udUserDaoSpringJdbc = new UdUserDaoSpringJdbc();

    public UserEntity create(UserEntity user) {
        return udUserDaoSpringJdbc.createUser(user);
    }

    @Override
    public Optional<UserEntity> findByUsername(String username) {
        return udUserDaoSpringJdbc.findUserByUsername(username);
    }

    @Override
    public UserEntity update(UserEntity user) {
        return null;
    }

    @Override
    public Optional<UserEntity> findById(UUID id) {
        return udUserDaoSpringJdbc.findUserById(id);
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

        JdbcTemplate jdbcTemplate = new JdbcTemplate(DataSources.dataSource(CFG.userdataJdbcUrl()));
        String sql = "INSERT INTO friendship (requester_id, addressee_id, status) VALUES (?, ?, ?)";
        jdbcTemplate.batchUpdate(sql,
                List.of(
                        new Object[]{requesterId, addresseeId, acceptedFriendshipStatus},
                        new Object[]{addresseeId, requesterId, acceptedFriendshipStatus}
                ));
    }

    @Override
    public void remove(UserEntity user) {
        udUserDaoSpringJdbc.deleteUser(user);
    }

    public UserEntity getUserOrCreateIfAbsent(UserEntity userEntity) {
        return findByUsername(userEntity.getUsername())
                .orElseGet(() -> create(userEntity));
    }
}