package guru.qa.niffler.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import guru.qa.niffler.api.AuthApi;
import guru.qa.niffler.api.core.RestClient;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import org.apache.commons.lang3.StringUtils;
import retrofit2.Response;

import java.io.IOException;

import static guru.qa.niffler.utils.OAuthUtils.generateCodeChallenge;
import static guru.qa.niffler.utils.OAuthUtils.generateCodeVerifier;
import static org.assertj.core.api.Assertions.assertThat;

public class AuthApiClient extends RestClient {

    private static final String RESPONSE_TYPE = "code";

    private static final String SCOPE = "openid";

    private static final String REDIRECT_URI = CFG.frontUrl() + "authorized";

    private static final String CODE_CHALLENGE_METHOD = "S256";

    private static final String GRANT_TYPE = "authorization_code";

    private static final String CLIENT_ID = "client";

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl(), true);
        this.authApi = retrofit.create(AuthApi.class);
    }

    public void getRegisterPage() {
        final Response<Void> response;
        try {
            response = authApi.getRegisterPage().execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);
    }

    public void registerUser(String username, String password, String passwordSubmit, String csrf) {
        final Response<Void> response;
        try {
            response = authApi.registerUser(username, password, passwordSubmit, csrf).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(201);
    }

    public void preRequest(String codeChallenge) {
        final Response<Void> response;
        try {
            response = authApi.authorize(
                                      RESPONSE_TYPE,
                                      CLIENT_ID,
                                      SCOPE,
                                      REDIRECT_URI,
                                      codeChallenge,
                                      CODE_CHALLENGE_METHOD)
                              .execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);
    }

    public String oAuthLogin(String username, String password) {
        final Response<Void> response;
        try {
            response = authApi.login(
                    username,
                    password,
                    ThreadSafeCookieStore.INSTANCE.cookieValue("XSRF-TOKEN")).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        String url = response.raw().request().url().toString();
        return StringUtils.substringAfter(url, "code=");
    }

    public String token(String code, String codeVerifier) {
        final Response<JsonNode> response;
        try {
            response = authApi.token(code, REDIRECT_URI, CLIENT_ID, codeVerifier, GRANT_TYPE).execute();

        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);
        assertThat(response.body()).isNotNull();

        return response.body().path("id_token").asText();
    }

    public String login(String username, String password) {
        String codeVerifier = generateCodeVerifier();
        String codeChallenge = generateCodeChallenge(codeVerifier);

        preRequest(codeChallenge);
        String code = oAuthLogin(username, password);

        return token(code, codeVerifier);
    }
}
