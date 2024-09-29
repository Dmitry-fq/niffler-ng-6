package guru.qa.niffler.test.web;

import guru.qa.niffler.model.AuthUserJson;
import guru.qa.niffler.model.AuthorityJson;
import guru.qa.niffler.service.AuthDbClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

public class JdbcTest {

    @Test
    void test() {
        AuthDbClient authDbClient = new AuthDbClient();
        AuthorityJson authorityJson = new AuthorityJson(
                null,
                new AuthUserJson(
                        null,
                        RandomDataUtils.randomUsername(),
                        "123",
                        true,
                        true,
                        true,
                        true
                ),
                "read"
        );

        System.out.println(authDbClient.createAuthority(authorityJson));
    }
}
