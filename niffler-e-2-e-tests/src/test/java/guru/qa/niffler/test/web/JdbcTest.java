package guru.qa.niffler.test.web;

import guru.qa.niffler.data.entity.userdata.UserEntity;
import guru.qa.niffler.data.repository.impl.UserdataUserRepositorySpringJdbc;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.service.UsersDbClient;
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

        UserdataUserRepositorySpringJdbc userdataUserRepositorySpringJdbc = new UserdataUserRepositorySpringJdbc();
        userdataUserRepositorySpringJdbc.addFriend(requester, addressee);
    }

    @Test
    void test2() throws SQLException {
        UsersDbClient usersDbClient = new UsersDbClient();
//        AuthUserEntity authUserEntity = new AuthUserEntity(
//                null,
//                "mandarina",
//                "12345",
//                true,
//                true,
//                true,
//                true,
//                new ArrayList<>()
//        );

        System.out.println(
                usersDbClient.findUserById(UUID.fromString("95397424-f5b8-4457-9ab4-a45995cd0bda")).toString()
        );
    }
}
