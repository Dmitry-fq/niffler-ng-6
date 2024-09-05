package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface AuthAuthorityDao {
    void createAuthority(AuthorityEntity... authority) throws SQLException;

    Optional<AuthorityEntity> findAuthorityById(UUID id);

    List<AuthorityEntity> findAuthoritiesByUserId(UUID userId);

    void deleteAuthority(AuthorityEntity user);

    List<AuthorityEntity> findAllAuthorities();
}
