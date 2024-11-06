package guru.qa.niffler.api;

import guru.qa.niffler.api.core.RestClient;
import retrofit2.Response;

import java.io.IOException;

import static org.assertj.core.api.Assertions.assertThat;

public class AuthApiClient extends RestClient {

    private final AuthApi authApi;

    public AuthApiClient() {
        super(CFG.authUrl());
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

    public void login(String username, String password, String csrf) {
        final Response<Void> response;
        try {
            response = authApi.login(username, password, csrf).execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(302);
    }
}
