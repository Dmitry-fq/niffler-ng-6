package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.CurrenciesQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import org.junit.jupiter.api.Test;

public class CategoryGraphQlTest extends BaseGraphQlTest {

    @User
    @Test
    @ApiLogin
    void categoriesAnotherUserInaccessible(@Token String bearerToken) {
        final ApolloCall<CurrenciesQuery.Data> currenciesCall = apolloClient.query(new CurrenciesQuery())
                                                                            .addHttpHeader("authorization", bearerToken);

        final ApolloResponse<CurrenciesQuery.Data> response = Rx2Apollo.single(currenciesCall).blockingGet();
        final CurrenciesQuery.Data data = response.dataOrThrow();
    }
}
