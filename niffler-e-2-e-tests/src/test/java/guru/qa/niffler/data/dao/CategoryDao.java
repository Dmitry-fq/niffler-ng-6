package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.CategoryEntity;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CategoryDao {
    CategoryEntity createCategory(CategoryEntity category);

    Optional<CategoryEntity> findCategoryById(UUID id);

    Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String CategoryName);

    List<CategoryEntity> findAllCategoriesByUsername(String username);

    void deleteCategory(CategoryEntity category);

    List<CategoryEntity> findAllCategories();
}
