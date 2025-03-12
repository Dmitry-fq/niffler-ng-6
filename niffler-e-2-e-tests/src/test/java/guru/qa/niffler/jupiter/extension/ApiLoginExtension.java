package guru.qa.niffler.jupiter.extension;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.WebDriverRunner;
import guru.qa.niffler.api.core.ThreadSafeCookieStore;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Token;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.model.TestData;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.service.impl.AuthApiClient;
import guru.qa.niffler.service.impl.SpendApiClient;
import guru.qa.niffler.service.impl.UsersApiClient;
import jaxb.userdata.FriendState;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;
import org.openqa.selenium.Cookie;

import java.util.List;

public class ApiLoginExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(ApiLoginExtension.class);

    private static final Config CFG = Config.getInstance();

    private final AuthApiClient authApiClient = new AuthApiClient();

    private final SpendApiClient spendApiClient = new SpendApiClient();

    private final UsersApiClient usersApiClient = new UsersApiClient();

    private final boolean setupBrowser;

    private ApiLoginExtension(boolean setupBrowser) {
        this.setupBrowser = setupBrowser;
    }

    public ApiLoginExtension() {
        this.setupBrowser = true;
    }

    public static ApiLoginExtension restApiLoginExtension() {
        return new ApiLoginExtension(false);
    }

    public static String getToken() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("token", String.class);
    }

    public static void setToken(String token) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("token", token);
    }

    public static String getCode() {
        return TestMethodContextExtension.context().getStore(NAMESPACE).get("code", String.class);
    }

    public static void setCode(String code) {
        TestMethodContextExtension.context().getStore(NAMESPACE).put("code", code);
    }

    public static Cookie getJsessionIdCookie() {
        return new Cookie(
                "JSESSIONID",
                ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
        );
    }

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), ApiLogin.class)
                         .ifPresent(apiLogin -> {

                             final UserJson userToLogin;
                             final UserJson userFromUserExtension = UserExtension.getUserJson();
                             if ("".equals(apiLogin.username()) || "".equals(apiLogin.password())) {
                                 if (userFromUserExtension == null) {
                                     throw new IllegalStateException("@User must be present in case that @ApiLogin is empty!");
                                 }
                                 userToLogin = userFromUserExtension;
                             } else {
                                 UserJson fakeUser = new UserJson(
                                         apiLogin.username(),
                                         new TestData(
                                                 apiLogin.password()
                                         )
                                 );
                                 if (userFromUserExtension != null) {
                                     throw new IllegalStateException("@User must not be present in case that @ApiLogin contains username or password!");
                                 }
                                 UserExtension.setUser(fakeUser);
                                 userToLogin = fakeUser;
                                 addTestData(userToLogin);
                             }

                             final String token = authApiClient.login(
                                     userToLogin.username(),
                                     userToLogin.testData().password()
                             );
                             setToken(token);
                             if (setupBrowser) {
                                 Selenide.open(CFG.frontUrl());
                                 Selenide.localStorage().setItem("id_token", getToken());
                                 WebDriverRunner.getWebDriver().manage().addCookie(
                                         new Cookie(
                                                 "JSESSIONID",
                                                 ThreadSafeCookieStore.INSTANCE.cookieValue("JSESSIONID")
                                         )
                                 );
                                 Selenide.open(MainPage.URL, MainPage.class).checkThatPageLoaded();
                             }
                         });
    }

    private void addTestData(UserJson user) {
        addCategories(user);
        addSpendings(user);

        List<UserJson> allFriends = usersApiClient.getAllFriendsByUsernameAndSearchQuery(
                user.username(), null
        );
        addFriends(user, allFriends);
        addIncomeInvitations(user, allFriends);
        addOutcomeInvitations(user);
    }

    private void addCategories(UserJson user) {
        List<CategoryJson> categories = spendApiClient.getCategories(user.username(), true);
        user.testData()
            .categories()
            .addAll(categories);
    }

    private void addSpendings(UserJson user) {
        List<SpendJson> spendings = spendApiClient.getSpends(user.username(), null, null, null);
        user.testData()
            .spendings()
            .addAll(spendings);
    }

    private void addFriends(UserJson user, List<UserJson> allFriends) {
        user.testData()
            .friends()
            .addAll(
                    allFriends.stream()
                              .filter(f -> FriendState.FRIEND.equals(f.friendState()))
                              .toList()
            );
    }

    private void addIncomeInvitations(UserJson user, List<UserJson> allFriends) {
        user.testData()
            .incomeInvitation()
            .addAll(
                    allFriends.stream()
                              .filter(f -> FriendState.INVITE_RECEIVED.equals(f.friendState()))
                              .toList()
            );
    }

    private void addOutcomeInvitations(UserJson user) {
        List<UserJson> allUsers = usersApiClient.getAllUsersByUsernameAndSearchQuery(user.username(), null);
        user.testData()
            .outcomeInvitation()
            .addAll(
                    allUsers.stream()
                            .filter(f -> FriendState.INVITE_SENT.equals(f.friendState()))
                            .toList()
            );
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(String.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), Token.class);
    }

    @Override
    public String resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return getToken();
    }
}
