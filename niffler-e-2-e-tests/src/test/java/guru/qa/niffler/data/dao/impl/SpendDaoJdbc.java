package guru.qa.niffler.data.dao.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.dao.SpendDao;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.model.CurrencyValues;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class SpendDaoJdbc implements SpendDao {

    private static final Config CFG = Config.getInstance();

    private final Connection connection;

    public SpendDaoJdbc(Connection connection) {
        this.connection = connection;
    }

    @Override
    public SpendEntity create(SpendEntity spend) {
        try (PreparedStatement ps = connection.prepareStatement(
                    "INSERT INTO spend (username, spend_date, currency, amount, description, category_id) " +
                            "VALUES ( ?, ?, ?, ?, ?, ?)",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, spend.getUsername());
                ps.setDate(2, spend.getSpendDate());
                ps.setString(3, spend.getCurrency().name());
                ps.setDouble(4, spend.getAmount());
                ps.setString(5, spend.getDescription());
                ps.setObject(6, spend.getCategory().getId());

                ps.executeUpdate();

                final UUID generatedKey;
                try (ResultSet rs = ps.getGeneratedKeys()) {
                    if (rs.next()) {
                        generatedKey = rs.getObject("id", UUID.class);
                    } else {
                        throw new SQLException("Can`t find id in ResultSet");
                    }
                }
                spend.setId(generatedKey);

                return spend;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Optional<SpendEntity> findSpendById(UUID id) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setObject(1, id);
                ps.execute();

                try (ResultSet rs = ps.getResultSet()) {
                    if (rs.next()) {
                        SpendEntity spendEntity = getSpendEntity(rs);

                        return Optional.of(spendEntity);
                    } else {
                        return Optional.empty();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<SpendEntity> findAllByUsername(String username) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "SELECT * FROM spend WHERE username = ?",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setString(1, username);
                ps.execute();

                List<SpendEntity> spendEntityList = new ArrayList<>();
                try (ResultSet rs = ps.getResultSet()) {
                    while (rs.next()) {
                        SpendEntity spendEntity = getSpendEntity(rs);
                        spendEntityList.add(spendEntity);
                    }
                    return spendEntityList;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteSpend(SpendEntity spend) {
        try (Connection connection = Databases.connection(CFG.spendJdbcUrl())) {
            try (PreparedStatement ps = connection.prepareStatement(
                    "DELETE FROM spend WHERE id = ?",
                    Statement.RETURN_GENERATED_KEYS
            )) {
                ps.setObject(1, spend.getId());
                ps.execute();
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private SpendEntity getSpendEntity(ResultSet rs) throws SQLException {
        SpendEntity spendEntity = new SpendEntity();
        spendEntity.setId(rs.getObject("id", UUID.class));
        spendEntity.setUsername(rs.getString("username"));
        spendEntity.setSpendDate(rs.getDate("spend_date"));
        String currency = rs.getString("currency");
        spendEntity.setCurrency(CurrencyValues.valueOf(currency));
        spendEntity.setAmount(rs.getDouble("amount"));
        spendEntity.setDescription(rs.getString("description"));
        CategoryDaoJdbc categoryDaoJdbc = new CategoryDaoJdbc();
        CategoryEntity categoryEntity = categoryDaoJdbc.findCategoryById(
                        (UUID) rs.getObject("category_id"))
                .orElseThrow(
                        () -> new SQLException("category not found")
                );
        spendEntity.setCategory(categoryEntity);

        return spendEntity;
    }
}
