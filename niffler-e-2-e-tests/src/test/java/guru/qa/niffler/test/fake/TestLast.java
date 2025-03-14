package guru.qa.niffler.test.fake;

import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UserdataApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Integer.MAX_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@Order(MAX_VALUE)
public class TestLast {

    private static final UserdataApiClient USERDATA_API_CLIENT = new UserdataApiClient();

    @Test
    void testLast() {
        List<UserJson> userJsonList = USERDATA_API_CLIENT.getAllUsersByUsernameAndSearchQuery("☺", "☺");

        assertThat(userJsonList)
                .as("Список пользователей пустой")
                .isEmpty();
    }
}
