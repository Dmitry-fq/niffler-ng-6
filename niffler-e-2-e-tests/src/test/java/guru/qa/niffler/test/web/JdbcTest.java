package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UsersDbClient;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;
import java.util.UUID;

public class JdbcTest {

    @Test
    void test1() {
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

//        UserdataUserRepositorySpringJdbc userdataUserRepositorySpringJdbc = new UserdataUserRepositorySpringJdbc();
//        userdataUserRepositorySpringJdbc.addFriend(requester, addressee);
    }

    @Test
    void test2() throws SQLException {
        UsersDbClient usersDbClient = new UsersDbClient();
        UserJson userJson = new UserJson(
                UUID.fromString("7c586721-5d39-49a8-bbb7-6baab67a3fde"),
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null,
                null
        );

        usersDbClient.addIncomeInvitation(userJson, 1);
    }
}
