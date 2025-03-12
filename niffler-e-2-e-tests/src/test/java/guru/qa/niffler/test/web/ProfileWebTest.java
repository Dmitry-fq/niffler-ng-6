package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.extension.BrowserExtension;
import guru.qa.niffler.model.CategoryJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.awt.image.BufferedImage;
import java.io.IOException;

@ExtendWith(BrowserExtension.class)
public class ProfileWebTest {

    private final Header header = new Header();

    @User(
            username = "duck",
            categories = {
                    @Category(
                            archived = true
                    )
            }
    )
    @ApiLogin(username = "duck", password = "12345")
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        Selenide.open(MainPage.URL, MainPage.class);

        header.toProfilePage()
              .clickShowArchivedCheckbox()
              .checkCategoryArchived(category[0].name());
    }

    @User(
            username = "duck",
            categories = @Category()
    )
    @ApiLogin(username = "duck", password = "12345")
    @Test
    void activeCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        Selenide.open(MainPage.URL, MainPage.class);

        header.toProfilePage()
              .clickShowArchivedCheckbox()
              .checkCategoryActive(category[0].name());
    }

    @User
    @ApiLogin
    @Test
    void nameShouldBeChanged() {
        Selenide.open(MainPage.URL, MainPage.class);

        final String newName = RandomDataUtils.randomName();
        header.toProfilePage()
              .setNewName(newName)
              .clickSaveChangesButton()
              .checkAlert("Profile successfully updated")
              .checkName(newName);
    }

    @User
    @ApiLogin
    @ScreenShotTest("img/expected-avatar.png")
    void avatarShouldBeCorrect(BufferedImage expectedImage) throws IOException {
        Selenide.open(MainPage.URL, MainPage.class);

        header.toProfilePage()
              .setNewAvatar()
              .clickSaveChangesButton()
              .avatarShouldBeCorrect(expectedImage);
    }
}
