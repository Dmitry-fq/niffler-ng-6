package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.User;
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

import java.util.List;

public class UserExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UserExtension.class);

    private static final String defaultPassword = "12345";

    private final UsersClient usersClient = new UsersApiClient();

    public static void setUser(UserJson testUser) {
        final ExtensionContext context = TestMethodContextExtension.context();
        context.getStore(NAMESPACE).put(
                context.getUniqueId(),
                testUser
        );
    }

    public static UserJson getUserJson() {
        final ExtensionContext context = TestMethodContextExtension.context();
        return context.getStore(NAMESPACE).get(context.getUniqueId(), UserJson.class);
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                         .ifPresent(userAnno -> {
                             if ("".equals(userAnno.username())) {
                                 final String username = RandomDataUtils.randomUsername();
                                 UserJson user = usersClient.createUser(username, defaultPassword);
                                 addTestData(user, userAnno);

                                 setUser(user);
                             }
                         });
    }

    private void addTestData(UserJson user, User userAnno) {
        List<UserJson> incomeInvitationUsers = usersClient.addIncomeInvitation(user, userAnno.incomeInvitation());
        user.testData().incomeInvitation().addAll(incomeInvitationUsers);

        List<UserJson> outcomeInvitationUsers = usersClient.addOutcomeInvitation(user, userAnno.outcomeInvitation());
        user.testData().outcomeInvitation().addAll(outcomeInvitationUsers);

        List<UserJson> friends = usersClient.addFriends(user, userAnno.friends());
        user.testData().friends().addAll(friends);
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(UserJson.class);
    }

    @Override
    public UserJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getUserJson();
    }
}
