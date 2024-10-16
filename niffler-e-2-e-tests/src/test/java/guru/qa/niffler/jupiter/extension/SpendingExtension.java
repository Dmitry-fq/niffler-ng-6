package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.jupiter.annotation.Spending;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.CurrencyValues;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendDbClient;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Date;

public class SpendingExtension implements BeforeEachCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(SpendingExtension.class);

    private final SpendDbClient spendDbClient = new SpendDbClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(annotation -> {
                    if (annotation.spendings().length > 0) {
                        Spending spending = annotation.spendings()[0];
                        CategoryJson categoryJson = new CategoryJson(
                                null,
                                spending.category(),
                                annotation.username(),
                                false
                        );
                        CategoryJson findedOrCreatedCategoryJson = spendDbClient.findCategoryByUsernameAndSpendName(
                                categoryJson.username(), categoryJson.name()
                        ).orElseGet(
                                () -> spendDbClient.createCategory(categoryJson)
                        );
                        SpendJson spend = new SpendJson(
                                null,
                                new Date(),
                                findedOrCreatedCategoryJson,
                                CurrencyValues.RUB,
                                spending.amount(),
                                spending.description(),
                                annotation.username()
                        );
                        SpendJson createdSpendJson;
                        createdSpendJson = spendDbClient.createSpend(spend);
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                createdSpendJson
                        );
                    }
                });
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(SpendJson.class);
    }

    @Override
    public SpendJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(NAMESPACE).get(extensionContext.getUniqueId(), SpendJson.class);
    }
}
