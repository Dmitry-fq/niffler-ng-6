package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthUserDao {

    @NonNull
    AuthUserEntity createUser(AuthUserEntity user);

    @NonNull
    Optional<AuthUserEntity> findUserById(UUID id);

    @NonNull
    Optional<AuthUserEntity> findUserByUsername(String username);

    void deleteUser(AuthUserEntity user);

    @NonNull
    List<AuthUserEntity> findAllUsers();
}
