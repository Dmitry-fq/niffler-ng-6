package guru.qa.niffler.data.mapper;

import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.auth.Authority;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import lombok.NonNull;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class AuthUserEntityExtractor implements ResultSetExtractor<AuthUserEntity> {

    public static final AuthUserEntityExtractor instance = new AuthUserEntityExtractor();

    private AuthUserEntityExtractor() {
    }

    @NonNull
    @Override
    public AuthUserEntity extractData(ResultSet rs) throws SQLException, DataAccessException {
        Map<UUID, AuthUserEntity> userMap = new ConcurrentHashMap<>();
        UUID userId = null;
        while (rs.next()) {
            userId = rs.getObject("id", UUID.class);
            AuthUserEntity user = userMap.computeIfAbsent(userId, id -> {
                try {
                    return new AuthUserEntity(
                            null,
                            rs.getString("username"),
                            rs.getString("password"),
                            rs.getBoolean("enabled"),
                            rs.getBoolean("account_non_expired"),
                            rs.getBoolean("account_non_locked"),
                            rs.getBoolean("credentials_non_expired"),
                            new ArrayList<>()
                    );
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            });
            AuthorityEntity authority = new AuthorityEntity();
            authority.setId(rs.getObject("authority_id", UUID.class));
            authority.setAuthority(Authority.valueOf(rs.getString("authority")));
            user.getAuthorities().add(authority);
        }
        return userMap.get(userId);
    }
}
