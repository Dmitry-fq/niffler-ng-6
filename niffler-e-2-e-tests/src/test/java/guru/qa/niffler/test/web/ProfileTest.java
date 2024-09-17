package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.BrowserExtension;
import guru.qa.niffler.jupiter.Category;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.LoginPage;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
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
        final String categoryName = category.name();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickProfile()
                .clickShowArchivedCheckbox()
                .checkCategoryArchived(categoryName);
    }

    @Category(
            username = "duck",
            archived = false
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson category) {
        final String username = category.username();
        final String password = "12345";
        final String categoryName = category.name();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password)
                .clickSettingsButton()
                .clickProfile()
                .clickShowArchivedCheckbox()
                .checkCategoryActive(categoryName);
    }
}
