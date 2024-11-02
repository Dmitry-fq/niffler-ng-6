package guru.qa.niffler.api;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

public class SpendApiClient implements SpendClient {

    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(Config.getInstance().spendUrl())
            .addConverterFactory(JacksonConverterFactory.create())
            .build();

    private final SpendApi spendApi = retrofit.create(SpendApi.class);

    @NonNull
    public SpendJson createSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.addSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(201);

        return response.body();
    }

    @NotNull
    @NonNull
    public SpendJson updateSpend(SpendJson spend) {
        final Response<SpendJson> response;
        try {
            response = spendApi.editSpend(spend)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    @NonNull
    public SpendJson getSpend(String id, String username) {
        final Response<SpendJson> response;
        try {
            response = spendApi.getSpend(id, username)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    @NonNull
    public List<SpendJson> getSpends(String username, CurrencyValues currency, Date from, Date to) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, currency, from, to)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    @NonNull
    public SpendJson deleteSpends(String username, List<String> ids) {
        final Response<SpendJson> response;
        try {
            response = spendApi.deleteSpends(username, ids)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    @NotNull
    @NonNull
    public CategoryJson createCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.addCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    @NotNull
    @Override
    public Optional<CategoryJson> findCategoryById(UUID id) {
        throw new UnsupportedOperationException("Действие не поддерживается в API");
    }

    @NotNull
    @Override
    public Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(username, false)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        List<CategoryJson> categoryJsonList = response.body();
        if (Objects.nonNull(categoryJsonList)) {

            return categoryJsonList.stream()
                    .filter(categoryJson -> name.equals(categoryJson.name()))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }

    @NotNull
    @Override
    public Optional<SpendJson> findSpendById(UUID id) {
        throw new UnsupportedOperationException("Действие не поддерживается в API");
    }

    @NotNull
    @Override
    public Optional<SpendJson> findByUsernameAndDescription(String username, String description) {
        final Response<List<SpendJson>> response;
        try {
            response = spendApi.getSpends(username, null, null, null)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        List<SpendJson> spendJsonList = response.body();
        if (Objects.nonNull(spendJsonList)) {

            return spendJsonList.stream()
                    .filter(categoryJson -> description.equals(categoryJson.description()))
                    .findFirst();
        } else {
            return Optional.empty();
        }
    }

    @Override
    public void removeCategory(CategoryJson category) {
        throw new UnsupportedOperationException("Действие не поддерживается в API");
    }

    @NonNull
    public CategoryJson updateCategory(CategoryJson category) {
        final Response<CategoryJson> response;
        try {
            response = spendApi.updateCategory(category)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }

    @NonNull
    public List<CategoryJson> getCategories(String username, boolean excludeArchived) {
        final Response<List<CategoryJson>> response;
        try {
            response = spendApi.getCategories(username, excludeArchived)
                    .execute();
        } catch (IOException e) {
            throw new AssertionError(e);
        }
        assertThat(response.code()).isEqualTo(200);

        return response.body();
    }
}
