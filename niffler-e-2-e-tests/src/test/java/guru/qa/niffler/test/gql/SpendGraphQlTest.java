package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.CurrentUserQuery;
import guru.qa.SpendsQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.rest.CurrencyValues;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.openqa.selenium.NotFoundException;

import static org.assertj.core.api.Assertions.assertThat;

public class SpendGraphQlTest extends BaseGraphQlTest {

    @User(
            categories = {
                    @Category(name = "category_active"),
                    @Category(name = "category_archived", archived = true),
            },
            spendings = {
                    @Spending(
                            category = "category_active",
                            description = "spend_with_category_active",
                            amount = 100
                    ),
                    @Spending(
                            category = "category_archived",
                            description = "spend_with_category_archived",
                            amount = 200
                    )
            }
    )
    @ApiLogin
    @Test
    void checkArchivedCategoryNotReceived(@Token String bearerToken) {
        final ApolloCall<CurrentUserQuery.Data> currentUserCall = apolloClient.query(CurrentUserQuery.builder().build())
                                                                              .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<CurrentUserQuery.Data> response = Rx2Apollo.single(currentUserCall).blockingGet();
        final CurrentUserQuery.Data data = response.dataOrThrow();
        CurrentUserQuery.Category categoryArchived = data.user.categories.stream()
                                                                         .filter(s -> s.archived)
                                                                         .findFirst()
                                                                         .orElse(null);

        CurrentUserQuery.Category categoryActive = data.user.categories.stream()
                                                                       .filter(s -> !s.archived)
                                                                       .findFirst()
                                                                       .orElse(null);

        assertThat(categoryArchived).isNotNull();
        assertThat(categoryActive).isNotNull();
    }

    @User(
            spendings = {
                    @Spending(
                            description = "RUB",
                            amount = 1,
                            currency = CurrencyValues.RUB
                    ),
                    @Spending(
                            description = "KZT",
                            amount = 2,
                            currency = CurrencyValues.KZT
                    ),
                    @Spending(
                            description = "EUR",
                            amount = 3,
                            currency = CurrencyValues.EUR
                    ),
                    @Spending(
                            description = "USD",
                            amount = 4,
                            currency = CurrencyValues.USD
                    ),
            }
    )
    @ApiLogin
    @ParameterizedTest
    @ValueSource(strings = {"RUB", "KZT", "EUR", "USD"})
    void checkSpendsWithAllCurrencies(String currencyName, @Token String bearerToken) {
        final ApolloCall<SpendsQuery.Data> spendsCall = apolloClient.query(SpendsQuery.builder()
                                                                                      .page(0)
                                                                                      .size(10)
                                                                                      .build())
                                                                    .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<SpendsQuery.Data> response = Rx2Apollo.single(spendsCall).blockingGet();
        final SpendsQuery.Data data = response.dataOrThrow();
        SpendsQuery.Edge spend = data.spends.edges.stream()
                                                  .filter(s -> s.node.currency.rawValue.equals(currencyName))
                                                  .findFirst()
                                                  .orElseThrow(() -> new NotFoundException(
                                                          "Не найдена валюта: " + currencyName)
                                                  );

        assertThat(spend.node.currency.rawValue).isEqualTo(currencyName);
    }
}
