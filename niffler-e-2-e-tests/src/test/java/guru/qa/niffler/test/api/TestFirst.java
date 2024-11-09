package guru.qa.niffler.test.api;

import guru.qa.niffler.api.UsersApiClient;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Integer.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@Order(MIN_VALUE)
public class TestFirst {

    private static final UsersApiClient usersApiClient = new UsersApiClient();

    @Test
    void testFirst() {
        List<UserJson> userJsonList = usersApiClient.getAllUsersByUsernameAndSearchQuery("test", null);

        assertThat(userJsonList)
                .as("Список пользователей пустой")
                .isNotEmpty();
    }
}
