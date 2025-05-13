package guru.qa.niffler.test.web;

import com.codeborne.selenide.Selenide;
import guru.qa.niffler.jupiter.annotation.ApiLogin;
import guru.qa.niffler.jupiter.annotation.Category;
import guru.qa.niffler.jupiter.annotation.ScreenShotTest;
import guru.qa.niffler.jupiter.annotation.User;
import guru.qa.niffler.jupiter.annotation.meta.WebTest;
import guru.qa.niffler.model.rest.CategoryJson;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.component.Header;
import guru.qa.niffler.utils.RandomDataUtils;
import org.junit.jupiter.api.Test;

import java.awt.image.BufferedImage;
import java.io.IOException;

@WebTest
public class ProfileWebTest {

    private final Header header = new Header();

    @User(
            categories = {
                    @Category(
                            archived = true
                    )
            }
    )
    @ApiLogin
    @Test
    void archivedCategoryShouldPresentInCategoriesList(CategoryJson[] category) {
        Selenide.open(MainPage.URL, MainPage.class);

        header.toProfilePage()
              .clickShowArchivedCheckbox()
              .checkCategoryArchived(category[0].name());
    }

    @User(
            categories = @Category
    )
    @ApiLogin
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
    @ScreenShotTest(expected = "expected-avatar.png")
    void avatarShouldBeCorrect(BufferedImage expectedImage) throws IOException {
        Selenide.open(MainPage.URL, MainPage.class);

        header.toProfilePage()
              .setNewAvatar()
              .clickSaveChangesButton()
              .avatarShouldBeCorrect(expectedImage);
    }
}
