package guru.qa.niffler.test.api;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.api.UsersApiClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import org.junit.jupiter.api.Test;

public class ApiTest {

    @Test
    void test1() {
        AuthApiClient authApiClient = new AuthApiClient();

        authApiClient.getRegisterPage();
        String csrf = ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN");

//        authApiClient.registerUser("testuuuusssserrr", "12345", "12345", csrf);
//        authApiClient.login("test", "test", csrf);

        UsersApiClient usersApiClient = new UsersApiClient();
        usersApiClient.createUser("dasdadad3", "12345");
    }
}
