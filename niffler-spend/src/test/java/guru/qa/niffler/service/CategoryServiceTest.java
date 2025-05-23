package guru.qa.niffler.service;

import guru.qa.niffler.data.CategoryEntity;
import guru.qa.niffler.data.repository.CategoryRepository;
import guru.qa.niffler.ex.CategoryNotFoundException;
import guru.qa.niffler.ex.InvalidCategoryNameException;
import guru.qa.niffler.ex.TooManyCategoriesException;
import guru.qa.niffler.model.CategoryJson;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CategoryServiceTest {

    @Test
    void categoryNotFoundExceptionShouldBeThrown(@Mock CategoryRepository categoryRepository) {
        final String username = "not_found";
        final UUID id = UUID.randomUUID();

        Mockito.when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
               .thenReturn(Optional.empty());

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
                id,
                "",
                username,
                true
        );

        CategoryNotFoundException ex = Assertions.assertThrows(
                CategoryNotFoundException.class,
                () -> categoryService.update(categoryJson)
        );
        Assertions.assertEquals(
                "Can`t find category by id: '" + id + "'",
                ex.getMessage()
        );
    }

    @ValueSource(strings = {"Archived", "ARCHIVED", "ArchIved"})
    @ParameterizedTest
    void categoryNameArchivedShouldBeDenied(String catName, @Mock CategoryRepository categoryRepository) {
        final String username = "duck";
        final UUID id = UUID.randomUUID();
        final CategoryEntity cat = new CategoryEntity();

        Mockito.when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
               .thenReturn(Optional.of(
                       cat
               ));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
                id,
                catName,
                username,
                true
        );

        InvalidCategoryNameException ex = Assertions.assertThrows(
                InvalidCategoryNameException.class,
                () -> categoryService.update(categoryJson)
        );
        Assertions.assertEquals(
                "Can`t add category with name: '" + catName + "'",
                ex.getMessage()
        );
    }

    @Test
    void onlyTwoFieldsShouldBeUpdated(@Mock CategoryRepository categoryRepository) {
        final String username = "duck";
        final UUID id = UUID.randomUUID();
        final CategoryEntity cat = new CategoryEntity();
        cat.setId(id);
        cat.setUsername(username);
        cat.setName("Магазины");
        cat.setArchived(false);

        Mockito.when(categoryRepository.findByUsernameAndId(eq(username), eq(id)))
               .thenReturn(Optional.of(
                       cat
               ));
        Mockito.when(categoryRepository.save(any(CategoryEntity.class)))
               .thenAnswer(invocation -> invocation.getArgument(0));

        CategoryService categoryService = new CategoryService(categoryRepository);

        CategoryJson categoryJson = new CategoryJson(
                id,
                "Бары",
                username,
                true
        );

        categoryService.update(categoryJson);
        ArgumentCaptor<CategoryEntity> argumentCaptor = ArgumentCaptor.forClass(CategoryEntity.class);
        verify(categoryRepository).save(argumentCaptor.capture());
        assertEquals("Бары", argumentCaptor.getValue().getName());
        assertEquals("duck", argumentCaptor.getValue().getUsername());
        assertTrue(argumentCaptor.getValue().isArchived());
        assertEquals(id, argumentCaptor.getValue().getId());
    }

    @Test
    void getAllCategoriesShouldFilterByExcludeArchived(@Mock CategoryRepository categoryRepository) {
        final String username = "username";
        final CategoryEntity unarchivedCategoryEntity = new CategoryEntity();
        unarchivedCategoryEntity.setId(UUID.randomUUID());
        unarchivedCategoryEntity.setName("Магазины");
        unarchivedCategoryEntity.setUsername(username);
        unarchivedCategoryEntity.setArchived(false);
        final CategoryEntity archivedCategoryEntity = new CategoryEntity();
        archivedCategoryEntity.setId(UUID.randomUUID());
        archivedCategoryEntity.setName("Базары");
        archivedCategoryEntity.setUsername(username);
        archivedCategoryEntity.setArchived(true);

        when(categoryRepository.findAllByUsernameOrderByName(eq(username)))
                .thenReturn(List.of(unarchivedCategoryEntity, archivedCategoryEntity));

        CategoryService categoryService = new CategoryService(categoryRepository);
        List<CategoryJson> result = categoryService.getAllCategories(username, true);
        CategoryJson actualCategoryJson = result.getFirst();

        assertEquals(result.size(), 1);
        assertEquals(unarchivedCategoryEntity.getId(), actualCategoryJson.id());
        assertEquals(unarchivedCategoryEntity.getName(), actualCategoryJson.name());
        assertEquals(unarchivedCategoryEntity.getUsername(), actualCategoryJson.username());
        assertEquals(unarchivedCategoryEntity.isArchived(), actualCategoryJson.archived());
    }

    @Test
    void archivedCategoryShouldNotUpdateUnarchivedIfUserHaveMoreMaxSizeCategories(@Mock CategoryRepository categoryRepository) {
        final String username = "username";
        final UUID id = UUID.randomUUID();
        final String name = "name";
        final CategoryJson categoryJsonForUpdate = new CategoryJson(
                id,
                name,
                username,
                false
        );
        final CategoryEntity archivedCategoryEntity = new CategoryEntity();
        archivedCategoryEntity.setId(id);
        archivedCategoryEntity.setName(name);
        archivedCategoryEntity.setUsername(username);
        archivedCategoryEntity.setArchived(true);
        final long maxCategoriesSize = 7L;

        when(categoryRepository.findByUsernameAndId(eq(categoryJsonForUpdate.username()), eq(categoryJsonForUpdate.id())))
                .thenReturn(Optional.of(archivedCategoryEntity));
        when(categoryRepository.countByUsernameAndArchived(eq(categoryJsonForUpdate.username()), eq(false)))
                .thenReturn(maxCategoriesSize + 1);

        CategoryService categoryService = new CategoryService(categoryRepository);
        TooManyCategoriesException ex = Assertions.assertThrows(
                TooManyCategoriesException.class,
                () -> categoryService.update(categoryJsonForUpdate)
        );
        Assertions.assertEquals(
                "Can`t unarchive category for user: '" + categoryJsonForUpdate.username() + "'",
                ex.getMessage()
        );
    }

    @Test
    void archivedCategoryShouldNotSaveUnarchivedIfUserHaveMoreMaxSizeCategories(@Mock CategoryRepository categoryRepository) {
        final String username = "username";
        final UUID id = UUID.randomUUID();
        final String name = "name";
        final CategoryJson categoryJsonForSave = new CategoryJson(
                id,
                name,
                username,
                false
        );
        final long maxCategoriesSize = 7L;

        when(categoryRepository.countByUsernameAndArchived(eq(categoryJsonForSave.username()), eq(false)))
                .thenReturn(maxCategoriesSize + 1);

        CategoryService categoryService = new CategoryService(categoryRepository);
        TooManyCategoriesException ex = Assertions.assertThrows(
                TooManyCategoriesException.class,
                () -> categoryService.save(categoryJsonForSave)
        );
        Assertions.assertEquals(
                "Can`t add over than 8 categories for user: '" + username + "'",
                ex.getMessage()
        );
    }
}