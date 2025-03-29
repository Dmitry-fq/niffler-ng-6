package guru.qa.niffler.data.repository.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.impl.CategoryDaoJdbc;
import guru.qa.niffler.data.dao.impl.SpendDaoJdbc;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.model.rest.CurrencyValues;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.UUID;

import static guru.qa.niffler.data.jpa.Connections.holder;

@ParametersAreNonnullByDefault
public class SpendRepositoryJdbc implements SpendRepository {

    private static final Config CFG = Config.getInstance();

    private final SpendDaoJdbc spendDaoJdbc = new SpendDaoJdbc();

    private final CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();

    @Nonnull
    @Override
    public SpendEntity create(SpendEntity spend) {
        return spendDaoJdbc.create(spend);
    }

    @Nonnull
    @Override
    public SpendEntity update(SpendEntity spend) {
        return null;
    }

    @Nonnull
    @Override
    public CategoryEntity createCategory(CategoryEntity category) {
        return categoryDaoJdbc.createCategory(category);
    }

    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryById(UUID id) {
        return categoryDaoJdbc.findCategoryById(id);
    }

    @SuppressWarnings("resource")
    @Nonnull
    @Override
    public Optional<CategoryEntity> findCategoryByUsernameAndCategoryName(String username, String name) {
        return categoryDaoJdbc.findCategoryByUsernameAndCategoryName(username, name);
    }

    @Nonnull
    @Override
    public Optional<SpendEntity> findById(UUID id) {
        return spendDaoJdbc.findSpendById(id);
    }

    @SuppressWarnings("resource")
    @Nonnull
    @Override
    public Optional<SpendEntity> findByUsernameAndSpendDescription(String username, String description) {
        try (PreparedStatement ps = holder(CFG.spendJdbcUrl()).connection().prepareStatement(
                "SELECT * FROM spend WHERE username = ? AND  description = ?"
        )) {
            ps.setString(1, username);
            ps.setString(2, description);
            ps.execute();

            try (ResultSet rs = ps.getResultSet()) {
                if (rs.next()) {
                    SpendEntity spendEntity = getSpendEntity(rs);

                    return Optional.of(spendEntity);
                } else {
                    return Optional.empty();
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void remove(SpendEntity spend) {
        spendDaoJdbc.deleteSpend(spend);
    }

    @Override
    public void removeCategory(CategoryEntity category) {
        categoryDaoJdbc.deleteCategory(category);
    }

    private SpendEntity getSpendEntity(ResultSet rs) throws SQLException {
        String currency = rs.getString("currency");
        CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
        CategoryEntity categoryEntity = categoryDaoJdbc.findCategoryById(
                                                               rs.getObject("category_id", UUID.class))
                                                       .orElseThrow(
                                                               () -> new SQLException("category not found")
                                                       );

        return new SpendEntity(rs.getObject("id", UUID.class),
                rs.getString("username"),
                CurrencyValues.valueOf(currency),
                rs.getDate("spend_date"),
                rs.getDouble("amount"),
                rs.getString("description"),
                categoryEntity
        );
    }
}
