package guru.qa.niffler.test.web;

import guru.qa.niffler.data.dao.impl.AuthUserDaoJdbc;
import guru.qa.niffler.data.entity.auth.AuthUserEntity;
import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.UdUserRepositoryJdbc;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.SpendDbClient;
import guru.qa.niffler.service.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

public class JdbcTest {

    @Test
    void txTest() {
        SpendDbClient spendDbClient = new SpendDbClient();

        SpendJson spend = spendDbClient.createSpend(
                new SpendJson(
                        null,
                        new Date(),
                        new CategoryJson(
                                null,
                                "cat-name-tx-3",
                                "duck",
                                false
                        ),
                        CurrencyValues.RUB,
                        1000.0,
                        "spend-name-tx-3",
                        "duck"
                )
        );

        System.out.println(spend);
    }

    @Test
    void springJdbcTest() {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson user = usersDbClient.createUser(
                new UserJson(
                        null,
                        "valentin-6",
                        null,
                        null,
                        null,
                        CurrencyValues.RUB,
                        null,
                        null,
                        null
                )
        );
        System.out.println(user);
    }

    @Test
    void test() {
        UserEntity requester = new UserEntity(
                null,
                "banana",
                CurrencyValues.RUB,
                null,
                null,
                null,
                null,
                null,
                null,
                null

        );
        UserEntity addressee = new UserEntity(
                null,
                "apelsina",
                CurrencyValues.RUB,
                null,
                null,
                null,
                null,
                null,
                null,
                null

        );

        UdUserRepositoryJdbc udUserRepositoryJdbc = new UdUserRepositoryJdbc();
        udUserRepositoryJdbc.addFriend(requester, addressee);
    }

    @Test
    void test2() throws SQLException {
        AuthUserDaoJdbc authUserDaoJdbc = new AuthUserDaoJdbc();

        Optional<AuthUserEntity> authUserEntity = authUserDaoJdbc.findUserWithAuthorityByUserId(UUID.fromString("ce7365c2-f6b7-49cd-b67e-480190a390ed"));

        System.out.println(authUserEntity);
    }
}
