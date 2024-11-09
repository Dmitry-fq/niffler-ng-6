package guru.qa.niffler.test.api;

import guru.qa.niffler.api.UsersApiClient;
import guru.qa.niffler.model.UserJson;
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
                .as("Список пользователей не пустой")
                .isEmpty();
    }
}
