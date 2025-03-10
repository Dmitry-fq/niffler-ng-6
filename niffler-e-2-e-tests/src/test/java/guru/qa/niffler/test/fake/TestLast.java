package guru.qa.niffler.test.fake;

import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.UsersApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@Order(MAX_VALUE)
public class TestLast {

    private static final UsersApiClient usersApiClient = new UsersApiClient();

    @Test
    void testLast() {
        List<UserJson> userJsonList = usersApiClient.getAllUsersByUsernameAndSearchQuery("☺", "☺");

        assertThat(userJsonList)
                .as("Список пользователей пустой")
                .isEmpty();
    }
}
