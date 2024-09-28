package guru.qa.niffler.jupiter.extension;

import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.extension.AfterTestExecutionCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.junit.jupiter.api.extension.ParameterContext;
import org.junit.jupiter.api.extension.ParameterResolutionException;
import org.junit.jupiter.api.extension.ParameterResolver;
import org.junit.platform.commons.support.AnnotationSupport;

import java.util.Objects;

import static guru.qa.niffler.utils.RandomDataUtils.getRandomCategoryNameIfEmpty;

public class CategoryExtension implements BeforeEachCallback, AfterTestExecutionCallback, ParameterResolver {

    public static final ExtensionContext.Namespace NAMESPACE = ExtensionContext.Namespace.create(CategoryExtension.class);

    private final CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();

    @Override
    public void beforeEach(ExtensionContext context) {
        AnnotationSupport.findAnnotation(context.getRequiredTestMethod(), User.class)
                .ifPresent(annotation -> {
                    if (annotation.categories().length > 0) {
                        Category category = annotation.categories()[0];

                        CategoryJson categoryJson = new CategoryJson(
                                null,
                                getRandomCategoryNameIfEmpty(category.name()),
                                annotation.username(),
                                category.archived()
                        );
                        CategoryEntity categoryEntity = categoryDaoJdbc.createWithoutTransaction(CategoryEntity.fromJson(categoryJson));
                        context.getStore(NAMESPACE).put(
                                context.getUniqueId(),
                                CategoryJson.fromEntity(categoryEntity)
                        );
                    }
                });
    }

    @Override
    public void afterTestExecution(ExtensionContext context) {
        CategoryJson categoryJson = context.getStore(CategoryExtension.NAMESPACE).get(context.getUniqueId(), CategoryJson.class);
        if (!Objects.isNull(categoryJson)) {
            categoryDaoJdbc.deleteCategory(CategoryEntity.fromJson(categoryJson));
        }
    }

    @Override
    public boolean supportsParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return parameterContext.getParameter().getType().isAssignableFrom(CategoryJson.class);
    }

    @Override
    public CategoryJson resolveParameter(ParameterContext parameterContext, ExtensionContext extensionContext) throws ParameterResolutionException {
        return extensionContext.getStore(CategoryExtension.NAMESPACE).get(extensionContext.getUniqueId(), CategoryJson.class);
    }
}
