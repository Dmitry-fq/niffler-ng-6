package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.model.UserJson;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {

    private static final Config CFG = Config.getInstance();

    private final Header header = new Header();

    @User(
            username = "duck",
            categories = {
                    @Category(
                            archived = true
                    )
            }
    )
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        final String username = category[0].username();
        final String password = "12345";
        final String categoryName = category[0].name();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toProfilePage()
                .clickShowArchivedCheckbox()
                .checkCategoryArchived(categoryName);
    }

    @User(
            username = "duck",
            categories = {
                    @Category(
                            archived = false
                    )
            }
    )
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        final String username = category[0].username();
        final String password = "12345";
        final String categoryName = category[0].name();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toProfilePage()
                .clickShowArchivedCheckbox()
                .checkCategoryActive(categoryName);
    }

    @User
    @Test
    void nameShouldBeChanged(UserJson user) {
        final String username = user.username();
        final String password = "12345";
        final String newName = RandomDataUtils.randomName();

        Selenide.open(CFG.frontUrl(), LoginPage.class)
                .login(username, password);

        header.toProfilePage()
                .setNewName(newName)
                .clickSaveChangesButton()
                .checkAlert("Profile successfully updated")
                .checkName(newName);
    }
}
