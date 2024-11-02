package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {

    @NonNull
    CategoryEntity createCategory(CategoryEntity category);

    @NonNull
    Optional<CategoryEntity> findCategoryById(UUID id);

    @NonNull
    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String CategoryName);

    @NonNull
    List<CategoryEntity> findAllCategoriesByUsername(String username);

    void deleteCategory(CategoryEntity category);

    @NonNull
    List<CategoryEntity> findAllCategories();
}
