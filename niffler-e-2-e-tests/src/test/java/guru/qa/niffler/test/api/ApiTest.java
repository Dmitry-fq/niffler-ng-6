package guru.qa.niffler.test.api;

import guru.qa.niffler.api.SpendApiClient;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    void test1() {
        SpendApiClient spendApiClient = new SpendApiClient();
        System.out.println(
                spendApiClient.findByUsernameAndDescription("duck", "Обучение Advanced 2.0")
        );
    }
}
