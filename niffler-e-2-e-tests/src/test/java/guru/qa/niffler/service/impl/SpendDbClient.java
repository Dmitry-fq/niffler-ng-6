package guru.qa.niffler.service.impl;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import guru.qa.niffler.service.SpendClient;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Optional;
import java.util.UUID;

@ParametersAreNonnullByDefault
public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @Nonnull
    public SpendJson createSpend(SpendJson spend) {
        Optional<CategoryEntity> category = spendRepository.findCategoryByUsernameAndCategoryName(
                spend.username(), spend.category().name()
        );
        if (category.isEmpty()) {
            return xaTransactionTemplate.execute(() -> SpendJson.fromEntity(
                            spendRepository.create(SpendEntity.fromJson(spend))
                    )
            );
        } else {
            SpendEntity spendEntity = spendRepository.findByUsernameAndSpendDescription(
                    spend.username(), spend.description()
            ).orElseThrow();

            return SpendJson.fromEntity(spendEntity);
        }
    }

    @Nonnull
    public SpendJson updateSpend(SpendJson spend) {
        SpendEntity spendEntity = spendRepository.update(SpendEntity.fromJson(spend));
        return SpendJson.fromEntity(spendEntity);
    }

    @Nonnull
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
        );
    }

    @Nonnull
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return spendRepository.findCategoryById(id)
                .map(CategoryJson::fromEntity);
    }

    @Nonnull
    public Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name) {
        return spendRepository.findCategoryByUsernameAndCategoryName(username, name)
                .map(CategoryJson::fromEntity);
    }

    @Nonnull
    public Optional<SpendJson> findSpendById(UUID id) {
        return spendRepository.findById(id)
                .map(SpendJson::fromEntity);
    }

    @Nonnull
    public Optional<SpendJson> findByUsernameAndDescription(String username, String description) {
        return spendRepository.findByUsernameAndSpendDescription(username, description)
                .map(SpendJson::fromEntity);
    }

    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.removeCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}