package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.AuthAuthorityDaoJdbc;
import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthorityEntity;
import guru.qa.niffler.data.entity.auth.UserEntity;
import guru.qa.niffler.model.AuthorityJson;

import static guru.qa.niffler.data.Databases.transaction;
import static java.sql.Connection.TRANSACTION_REPEATABLE_READ;

public class AuthDbClient {

    private static final Config CFG = Config.getInstance();

    public AuthorityJson createAuthority(AuthorityJson authority) {
        return transaction(connection -> {
                    AuthorityEntity authorityEntity = AuthorityEntity.fromJson(authority);
                    if (authorityEntity.getUser().getId() == null) {
                        UserEntity userEntity = new AuthUserDaoJdbc(connection)
                                .createUser(authorityEntity.getUser());
                        authorityEntity.setUser(userEntity);
                    }
                    return AuthorityJson.fromEntity(
                            new AuthAuthorityDaoJdbc(connection).createAuthority(authorityEntity)
                    );
                },
                CFG.authJdbcUrl(),
                TRANSACTION_REPEATABLE_READ
        );
    }
}
