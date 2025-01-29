package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.service.UsersClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.ArrayList;
import java.util.List;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);

    private static final String defaultPassword = "12345";

    private final UsersClient usersClient = new UsersApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(userAnno -> {
                    if ("".equals(userAnno.username())) {
                        final String username = RandomDataUtils.randomUsername();
                        UserJson testUser = usersClient.createUser(username, defaultPassword);
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                testUser.addTestData(
                                        createTestData(testUser, userAnno)
                                )
                        );
                    }
                });
    }

    private TestData createTestData(UserJson testUser, User userAnno) {
        List<UserJson> friends = usersClient.addFriends(testUser, userAnno.friends());
        List<UserJson> incomeInvitation = usersClient.addIncomeInvitation(testUser, userAnno.incomeInvitation());
        List<UserJson> outcomeInvitation = usersClient.addOutcomeInvitation(testUser, userAnno.outcomeInvitation());

        return new TestData(
                defaultPassword,
                new ArrayList<>(),
                new ArrayList<>(),
                getUsernameListFromUserJsonList(friends),
                getUsernameListFromUserJsonList(incomeInvitation),
                getUsernameListFromUserJsonList(outcomeInvitation)
        );
    }

    private List<String> getUsernameListFromUserJsonList(List<UserJson> userJsonList) {
        return userJsonList.stream().map(UserJson::username).toList();
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), UserJson.class);
    }
}
