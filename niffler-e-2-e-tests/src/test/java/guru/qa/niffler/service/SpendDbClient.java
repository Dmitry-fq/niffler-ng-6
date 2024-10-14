package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.CategoryDao;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.tpl.JdbcTransactionTemplate;
import guru.qa.niffler.model.SpendJson;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDbClient {

    private static final Config CFG = Config.getInstance();

    private final SpendDao spendDao = new SpendDaoJdbc();

    private final CategoryDao categoryDao = new CategoryDaoJdbc();

    private final JdbcTransactionTemplate jdbcTxTemplate = new JdbcTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    public SpendJson createSpend(SpendJson spend) {
        return jdbcTxTemplate.execute(() -> {
                    SpendEntity spendEntity = SpendEntity.fromJson(spend);
                    if (spendEntity.getCategory().getId() == null) {
                        CategoryEntity categoryEntity = categoryDao.createCategory(spendEntity.getCategory());
                        spendEntity.setCategory(categoryEntity);
                    }
                    return SpendJson.fromEntity(
                            spendDao.create(spendEntity)
                    );
                }
        );
    }

    public SpendEntity createSpend(SpendEntity spend) throws SQLException {
        return new SpendDaoJdbc().create(spend);
    }

    public Optional<SpendEntity> findSpendById(UUID id) {
        return spendDao.findSpendById(id);
    }

    public List<SpendEntity> findAllSpendsByUsername(String username) {
        return spendDao.findAllSpendsByUsername(username);
    }

    public void deleteSpend(SpendEntity spend) {
        spendDao.deleteSpend(spend);
    }

    public List<SpendEntity> findAllSpends() {
        return spendDao.findAllSpends();
    }

    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDao.createCategory(category);
    }

    public CategoryEntity createCategoryWithoutTransaction(CategoryEntity category) {
        return new CategoryDaoJdbc().createWithoutTransaction(category);
    }

    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDao.findCategoryById(id);
    }

    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String categoryName) {
        return categoryDao.findCategoryByUsernameAndCategoryName(username, categoryName);
    }

    public List<CategoryEntity> findAllCategoriesByUsername(String username) {
        return categoryDao.findAllCategoriesByUsername(username);
    }

    public void deleteCategory(CategoryEntity category) {
        categoryDao.deleteCategory(category);
    }

    public List<CategoryEntity> findAllCategories() {
        return categoryDao.findAllCategories();
    }
}
