package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface UdUserDao {

    @NonNull
    UserEntity createUser(UserEntity user);

    @NonNull
    Optional<UserEntity> findUserById(UUID id);

    @NonNull
    Optional<UserEntity> findUserByUsername(String username);

    void deleteUser(UserEntity user);

    @NonNull
    List<UserEntity> findAllUsers();
}
