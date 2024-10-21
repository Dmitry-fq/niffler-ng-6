package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import lombok.NonNull;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import javax.annotation.Nonnull;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public interface CategoryDao {

    @Nonnull
    CategoryEntity createCategory(CategoryEntity category);

    @Nonnull
    Optional<CategoryEntity> findCategoryById(UUID id);

    @Nonnull
    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String CategoryName);

    @Nonnull
    List<CategoryEntity> findAllCategoriesByUsername(String username);

    void deleteCategory(CategoryEntity category);

    @Nonnull
    List<CategoryEntity> findAllCategories();
}
