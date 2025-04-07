package guru.qa.niffler.test.gql;

import com.apollographql.apollo.api.ApolloResponse;
import com.apollographql.java.client.ApolloCall;
import com.apollographql.java.rx2.Rx2Apollo;
import guru.qa.FriendsRecursiveQuery;
import guru.qa.FriendsWithCategoriesQuery;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.jupiter.annotation.User;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class UserGraphQlTest extends BaseGraphQlTest {

    @User(friends = 1)
    @Test
    @ApiLogin
    void categoriesAnotherUserInaccessible(@Token String bearerToken) {
        final ApolloCall<FriendsWithCategoriesQuery.Data> friendsWithCategoriesCall =
                apolloClient.query(
                        FriendsWithCategoriesQuery.builder()
                                                  .page(0)
                                                  .size(10)
                                                  .build()
                ).addHttpHeader("authorization", bearerToken);
        final ApolloResponse<FriendsWithCategoriesQuery.Data> response = Rx2Apollo.single(friendsWithCategoriesCall).blockingGet();

        assertThat(response.errors).isNotEmpty();
        assertThat(response.errors.getFirst().getMessage())
                .contains("Can`t query categories for another user");
    }

    @User
    @Test
    @ApiLogin
    void checkRestrictionsDepthRecursiveRequests(@Token String bearerToken) {
        final ApolloCall<FriendsRecursiveQuery.Data> friendsRecursiveCall =
                apolloClient.query(
                        FriendsRecursiveQuery.builder()
                                             .page(0)
                                             .size(10)
                                             .build()
                ).addHttpHeader("authorization", bearerToken);
        final ApolloResponse<FriendsRecursiveQuery.Data> response = Rx2Apollo.single(friendsRecursiveCall).blockingGet();

        assertThat(response.errors).isNotEmpty();
        assertThat(response.errors.getFirst().getMessage())
                .contains("Can`t fetch over 2 friends sub-queries");
    }
}
