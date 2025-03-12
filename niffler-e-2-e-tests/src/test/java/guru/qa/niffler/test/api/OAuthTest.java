package guru.qa.niffler.test.api;

import guru.qa.niffler.api.AuthApiClient;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.UserJson;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static guru.qa.niffler.utils.OAuthUtils.generateCodeChallenge;
import static guru.qa.niffler.utils.OAuthUtils.generateCodeVerifier;
import static org.assertj.core.api.Assertions.assertThat;

@ExtendWith(BrowserExtension.class)
public class OAuthTest {

    private final AuthApiClient authApiClient = new AuthApiClient();

    @User
    @Test
    void oAuthTest(UserJson user) {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        authApiClient.preRequest(codeChallenge);
        String code = authApiClient.login(user.username(), user.testData().password());
        String token = authApiClient.token(code, codeVerifier);

        assertThat(token).isNotNull();
    }
}
