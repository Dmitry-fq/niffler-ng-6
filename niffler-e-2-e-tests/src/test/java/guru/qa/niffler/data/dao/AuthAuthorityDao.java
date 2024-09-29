package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
    AuthorityEntity createAuthority(AuthorityEntity authority) throws SQLException;

    Optional<AuthorityEntity> findById(UUID id);

    Optional<AuthorityEntity> findByUserId(UUID userId);

    void delete(AuthorityEntity user);
}
