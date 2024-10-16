package guru.qa.niffler.service;

import guru.qa.niffler.data.entity.spend.CategoryEntity;
import guru.qa.niffler.data.entity.spend.SpendEntity;
import guru.qa.niffler.data.repository.SpendRepository;
import guru.qa.niffler.data.repository.impl.SpendRepositoryHibernate;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.SpendJson;

import java.util.Optional;
import java.util.UUID;

public class SpendDbClient implements SpendClient {

    private final SpendRepository spendRepository = new SpendRepositoryHibernate();

    public SpendJson createSpend(SpendJson spend) {
        SpendEntity spendEntity = spendRepository.create(SpendEntity.fromJson(spend));
        return SpendJson.fromEntity(spendEntity);
    }

    public SpendJson updateSpend(SpendJson spend) {
        SpendEntity spendEntity = spendRepository.update(SpendEntity.fromJson(spend));
        return SpendJson.fromEntity(spendEntity);
    }

    public CategoryJson createCategory(CategoryJson category) {
        CategoryEntity categoryEntity = spendRepository.createCategory(CategoryEntity.fromJson(category));
        return CategoryJson.fromEntity(categoryEntity);
    }

    public Optional<CategoryJson> findCategoryById(UUID id) {
        return spendRepository.findCategoryById(id)
                .map(CategoryJson::fromEntity);
    }

    public Optional<CategoryJson> findCategoryByUsernameAndSpendName(String username, String name) {
        return spendRepository.findCategoryByUsernameAndSpendName(username, name)
                .map(CategoryJson::fromEntity);
    }

    public Optional<SpendJson> findSpendById(UUID id) {
        return spendRepository.findById(id)
                .map(SpendJson::fromEntity);
    }

    public Optional<SpendJson> findByUsernameAndDescription(String username, String description) {
        return spendRepository.findByUsernameAndDescription(username, description)
                .map(SpendJson::fromEntity);
    }

    public void removeCategory(CategoryJson category) {
        spendRepository.removeCategory(CategoryEntity.fromJson(category));
    }
}
