package guru.qa.niffler.service;

import guru.qa.niffler.config.Config;
import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.data.tpl.XaTransactionTemplate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;
import lombok.NonNull;
import org.jetbrains.annotations.NotNull;

import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private static final Config CFG = Config.getInstance();

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    private final XaTransactionTemplate xaTransactionTemplate = new XaTransactionTemplate(
            CFG.spendJdbcUrl()
    );

    @NonNull
    public SpendJson createSpend(SpendJson spend) {
        Optional<CategoryEntity> category = spendRepository.findCategoryByUsernameAndSpendName(
                spend.username(), spend.category().name()
        );
        if (category.isEmpty()) {
            return xaTransactionTemplate.execute(() -> SpendJson.fromEntity(
                            spendRepository.create(SpendEntity.fromJson(spend))
                    )
            );
        } else {
            SpendEntity spendEntity = spendRepository.findByUsernameAndDescription(
                    spend.username(), spend.description()
            ).orElseThrow();

            return SpendJson.fromEntity(spendEntity);
        }
    }

    @NotNull
    public SpendJson updateSpend(SpendJson spend) {
        SpendEntity spendEntity = spendRepository.update(SpendEntity.fromJson(spend));
        return SpendJson.fromEntity(spendEntity);
    }

    @NotNull
    public CategoryJson createCategory(CategoryJson category) {
        return xaTransactionTemplate.execute(() -> CategoryJson.fromEntity(
                        spendRepository.createCategory(CategoryEntity.fromJson(category))
                )
        );
    }

    @NotNull
    public Optional<CategoryJson> findCategoryById(UUID id) {
        return spendRepository.findCategoryById(id)
                .map(CategoryJson::fromEntity);
    }

    @NotNull
    public Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name) {
        return spendRepository.findCategoryByUsernameAndSpendName(username, name)
                .map(CategoryJson::fromEntity);
    }

    @NotNull
    public Optional<SpendJson> findSpendById(UUID id) {
        return spendRepository.findById(id)
                .map(SpendJson::fromEntity);
    }

    @NotNull
    public Optional<SpendJson> findByUsernameAndDescription(String username, String description) {
        return spendRepository.findByUsernameAndDescription(username, description)
                .map(SpendJson::fromEntity);
    }

    public void removeCategory(CategoryJson category) {
        xaTransactionTemplate.execute(() -> {
            spendRepository.removeCategory(CategoryEntity.fromJson(category));
            return null;
        });
    }
}
