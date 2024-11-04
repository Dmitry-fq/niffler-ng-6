package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type;
import io.qameta.allure.Allure;
import org.apache.commons.lang3.time.StopWatch;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeTestExecutionCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.TimeUnit;

import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.EMPTY;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_FRIEND;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_INCOME_REQUEST;
import static guru.qa.niffler.jupiter.extension.UsersQueueExtension.UserType.Type.WITH_OUTCOME_REQUEST;

/**
 * В каждом тесте теперь создаются новые юзеры
 */
@Deprecated
public class UsersQueueExtension implements
        BeforeTestExecutionCallback,
        AfterTestExecutionCallback,
        ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(UsersQueueExtension.class);

    private static final Queue<StaticUser> emptyQueue = new ConcurrentLinkedQueue<>();

    private static final Queue<StaticUser> withFriendQueue = new ConcurrentLinkedQueue<>();

    private static final Queue<StaticUser> withIncomeRequestQueue = new ConcurrentLinkedQueue<>();

    private static final Queue<StaticUser> withOutcomeRequestQueue = new ConcurrentLinkedQueue<>();

    static {
        emptyQueue.add(new StaticUser(
                "test1", "test", null, null, null, EMPTY)
        );
        withFriendQueue.add(new StaticUser(
                "test2", "test", "test", null, null, WITH_FRIEND)
        );
        withIncomeRequestQueue.add(new StaticUser(
                "test3", "test", null, "test", null, WITH_INCOME_REQUEST)
        );
        withOutcomeRequestQueue.add(new StaticUser(
                "test4", "test", null, null, "test5", WITH_OUTCOME_REQUEST)
        );
    }

    @Override
    public void beforeTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> userMap = getOrCreateUserMap(context);

        Arrays.stream(context.getRequiredTestMethod().getParameters())
                .filter(p -> AnnotationSupport.isAnnotated(p, UserType.class))
                .map(p -> p.getAnnotation(UserType.class))
                .forEach(ut -> {
                    resetStartTimeAllureTestCase();
                    Optional<StaticUser> user = getStaticUserFromQueueByEmpty(ut.value());
                    user.ifPresentOrElse(
                            u -> userMap.put(ut, u),
                            () -> {
                                throw new IllegalStateException("Can`t obtain user after 30s.");
                            }
                    );
                });
    }

    private void resetStartTimeAllureTestCase() {
        Allure.getLifecycle().updateTestCase(testCase ->
                testCase.setStart(new Date().getTime())
        );
    }

    private Optional<StaticUser> getStaticUserFromQueueByEmpty(Type typeValue) {
        Optional<StaticUser> user = Optional.empty();
        StopWatch sw = StopWatch.createStarted();

        while (user.isEmpty() && sw.getTime(TimeUnit.SECONDS) < 30) {
            user = switch (typeValue) {
                case EMPTY -> Optional.ofNullable(emptyQueue.poll());
                case WITH_FRIEND -> Optional.ofNullable(withFriendQueue.poll());
                case WITH_INCOME_REQUEST -> Optional.ofNullable(withIncomeRequestQueue.poll());
                case WITH_OUTCOME_REQUEST -> Optional.ofNullable(withOutcomeRequestQueue.poll());
            };
        }

        return user;
    }

    private Map<UserType, StaticUser> getOrCreateUserMap(ExtensionContext context) {
        return (Map<UserType, StaticUser>) context.getStore(NAMESPACE)
                .getOrComputeIfAbsent(
                        context.getUniqueId(),
                        key -> new HashMap<>()
                );
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        Map<UserType, StaticUser> map = context.getStore(NAMESPACE).get(
                context.getUniqueId(),
                Map.class);

        for (Map.Entry<UserType, StaticUser> entry : map.entrySet()) {
            StaticUser user = entry.getValue();

            switch (user.type) {
                case EMPTY -> emptyQueue.add(user);
                case WITH_FRIEND -> withFriendQueue.add(user);
                case WITH_INCOME_REQUEST -> withIncomeRequestQueue.add(user);
                case WITH_OUTCOME_REQUEST -> withOutcomeRequestQueue.add(user);
            }
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(StaticUser.class)
                && AnnotationSupport.isAnnotated(parameterContext.getParameter(), UserType.class);
    }

    @Override
    public StaticUser resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext)
            throws ParameterResolutionException {
        UserType ut = parameterContext.getParameter().getAnnotation(UserType.class);
        Map<UserType, StaticUser> userMap = extensionContext.getStore(NAMESPACE)
                .get(extensionContext.getUniqueId(), Map.class);

        return userMap.get(ut);
    }

    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    public @interface UserType {
        Type value() default EMPTY;

        enum Type {
            EMPTY, WITH_FRIEND, WITH_INCOME_REQUEST, WITH_OUTCOME_REQUEST
        }
    }

    public record StaticUser(String username, String password, String friend, String income, String outcome,
                             Type type) {
    }
}
