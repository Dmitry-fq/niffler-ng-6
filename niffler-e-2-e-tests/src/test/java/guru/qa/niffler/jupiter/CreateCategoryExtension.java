package guru.qa.niffler.jupiter;

import guru.qa.niffler.api.SpendApiClient;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.platform.commons.support.AnnotationSupport;

import static guru.qa.niffler.utils.Utils.getRandomName;

public class CreateCategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CreateCategoryExtension.class);

    private final SpendApiClient spendApiClient = new SpendApiClient();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), Category.class)
                .ifPresent(annotation -> {
                    CategoryJson category = new CategoryJson(
                            null,
                            getRandomName(1, 30),
                            annotation.username(),
                            annotation.archived()
                    );
                    CategoryJson createdCategory = spendApiClient.createCategory(category);
                    if (annotation.archived()) {
                        CategoryJson archivedCategory = new CategoryJson(
                                createdCategory.id(),
                                createdCategory.name(),
                                createdCategory.username(),
                                true
                        );
                        createdCategory = spendApiClient.updateCategory(archivedCategory);
                    }
                    context.getStore(NAMESPACE).put(
                            context.getUniqueId(),
                            createdCategory
                    );
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson category = context.getStore(CreateCategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!category.archived()) {
            CategoryJson updatedCategory = new CategoryJson(
                    category.id(),
                    category.name(),
                    category.username(),
                    true
            );
            updatedCategory = spendApiClient.updateCategory(updatedCategory);

            context.getStore(NAMESPACE).put(
                    context.getUniqueId(),
                    updatedCategory
            );
        }
    }
}
