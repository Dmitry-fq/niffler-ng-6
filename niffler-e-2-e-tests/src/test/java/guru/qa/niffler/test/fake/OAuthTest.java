package guru.qa.niffler.test.fake;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.impl.AuthApiClient;
import org.junit.jupiter.api.Test;

import static guru.qa.niffler.utils.OAuthUtils.generateCodeChallenge;
import static guru.qa.niffler.utils.OAuthUtils.generateCodeVerifier;
import static org.assertj.core.api.Assertions.assertThat;

public class OAuthTest {

    private static final Config CFG = Config.getInstance();

    private final AuthApiClient authApiClient = new AuthApiClient();

    @User
    @Test
    void oAuthTest(UserJson user) {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        authApiClient.preRequest(codeChallenge);
        String code = authApiClient.oAuthLogin(user.username(), user.testData().password());
        String token = authApiClient.token(code, codeVerifier);

        System.out.println(token);
        assertThat(token).isNotNull();
    }

    @User
    @ApiLogin()
    @Test
    void oauthTest(@Token String token, UserJson user) {
        System.out.println(user);
        assertThat(token).isNotNull();
    }
}
