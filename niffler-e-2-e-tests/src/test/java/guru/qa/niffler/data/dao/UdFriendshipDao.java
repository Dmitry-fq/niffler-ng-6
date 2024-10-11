package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UdFriendshipDao {
    UserEntity createUser(UserEntity user);

    Optional<UserEntity> findUserById(UUID id);

    Optional<UserEntity> findUserByUsername(String username);

    void deleteUser(UserEntity user);

    List<UserEntity> findAllUsers();
}