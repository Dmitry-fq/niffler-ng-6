package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.AuthAuthorityDao;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.tpl.Connections.holder;

public class AuthAuthorityDaoJdbc implements AuthAuthorityDao {

  private static final Config CFG = Config.getInstance();
  private final String url = CFG.authJdbcUrl();

    @Override
    public void createAuthority(AuthorityEntity... authority) {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "INSERT INTO \"authority\" (user_id, authority) VALUES (?, ?)")) {
            for (AuthorityEntity a : authority) {
                ps.setObject(1, a.getUser().getId());
                ps.setString(2, a.getAuthority().name());
                ps.addBatch();
                ps.clearParameters();
            }
            ps.executeBatch();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<AuthorityEntity> findAuthorityById(UUID id) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority WHERE id = ?"
        )) {
            ps.setObject(1, id);
            ps.execute();
            try (ResultSet resultSet = ps.getResultSet()) {
                if (resultSet.next()) {
                    AuthorityEntity authorityEntity = convertToEntity(resultSet);

                    return Optional.of(authorityEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAuthoritiesByUserId(UUID userId) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM authority WHERE user_id = ?"
        )) {
            ps.setObject(1, userId);
            ps.execute();

            List<AuthorityEntity> authorityEntityList = new ArrayList<>();
            try (ResultSet resultSet = ps.getResultSet()) {
                while (resultSet.next()) {
                    AuthorityEntity authorityEntity = convertToEntity(resultSet);
                    authorityEntityList.add(authorityEntity);
                }

                return authorityEntityList;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAuthority(AuthorityEntity authority) {
        try (PreparedStatement ps = holder(CFG.authJdbcUrl()).connection().prepareStatement(
                "DELETE FROM authority WHERE id = ?"
        )) {
            ps.setObject(1, authority.getId());
            ps.execute();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<AuthorityEntity> findAllAuthorities() {
        try (PreparedStatement ps = holder(url).connection().prepareStatement(
                "SELECT * FROM authority"
        )) {
            ps.execute();
            List<AuthorityEntity> resultList = new ArrayList<>();
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

    private AuthorityEntity convertToEntity(ResultSet resultSet) throws SQLException {
        return new AuthorityEntity(
                resultSet.getObject("id", UUID.class),
                resultSet.getObject("user_id", AuthUserEntity.class),
                resultSet.getObject("authority", Authority.class)
        );
    }
}
