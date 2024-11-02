package guru.qa.niffler.data.repository;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import lombok.NonNull;

import java.util.Optional;
import java.util.UUID;

public interface AuthUserRepository {

    @NonNull
    AuthUserEntity create(AuthUserEntity user);

    @NonNull
    AuthUserEntity update(AuthUserEntity user);

    @NonNull
    Optional<AuthUserEntity> findById(UUID id);

    @NonNull
    Optional<AuthUserEntity> findByUsername(String username);

    void remove(AuthUserEntity user);

}
