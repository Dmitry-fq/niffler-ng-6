package guru.qa.niffler.data.dao;

import guru.qa.niffler.data.entity.spend.SpendEntity;
import lombok.NonNull;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SpendDao {
    @NonNull
    SpendEntity create(SpendEntity spend);

    @NonNull
    Optional<SpendEntity> findSpendById(UUID id);

    @NonNull
    List<SpendEntity> findAllSpendsByUsername(String username);

    void deleteSpend(SpendEntity spend);

    @NonNull
    List<SpendEntity> findAllSpends();
}
