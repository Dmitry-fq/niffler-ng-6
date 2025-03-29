package guru.qa.niffler.test.fake;

import guru.qa.niffler.model.rest.UserJson;
import guru.qa.niffler.service.impl.UserdataApiClient;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;

import java.util.List;

import static java.lang.Integer.MIN_VALUE;
import static org.assertj.core.api.Assertions.assertThat;

@Order(MIN_VALUE)
public class TestFirst {

    private static final UserdataApiClient USERDATA_API_CLIENT = new UserdataApiClient();

    @Test
    void testFirst() {
        List<UserJson> userJsonList = USERDATA_API_CLIENT.getAllUsersByUsernameAndSearchQuery("test", null);

        assertThat(userJsonList)
                .as("Список пользователей не пустой")
                .isNotEmpty();
    }
}
