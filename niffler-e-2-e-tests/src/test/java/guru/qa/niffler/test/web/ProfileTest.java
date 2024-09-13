package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;

public class ProfileTest {

    private static final Config CFG = Config.getInstance();

    @Category(
            username = "duck",
            archived = true
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson category) {
        final String username = category.username();
        final String password = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickProfile()
                .clickShowArchivedCheckbox()
                .checkCategoryArchived(category.archived(), category.name());
    }

    @Category(
            username = "duck",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        final String username = category.username();
        final String password = "12345";

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickProfile()
                .clickShowArchivedCheckbox()
                .checkCategoryArchived(category.archived(), category.name());
    }
}