package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.page.EditSpendingPage;
import guru.qa.niffler.page.FriendsPage;
import guru.qa.niffler.page.LoginPage;
import guru.qa.niffler.page.MainPage;
import guru.qa.niffler.page.PeoplePage;
import guru.qa.niffler.page.ProfilePage;
import io.qameta.allure.Step;

import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

@ParametersAreNonnullByDefault
public class Header extends BaseComponent {

    private static final String ELEMENT_XPATH = "//header";

    private static final String PROFILE_BUTTON_TEXT = "Profile";

    private static final String FRIENDS_BUTTON_TEXT = "Friends";

    private static final String ALL_PEOPLE_BUTTON_TEXT = "All People";

    private static final String SIGN_OUT_BUTTON_TEXT = "Sign Out";

    private final SelenideElement mainPageButton = $x("//h1[contains(@class, 'MuiTypography-root ')]");

    private final SelenideElement newSpendingsButton = $x("//a[contains(@class, 'MuiButtonBase-root')]");

    private final SelenideElement settingsButton = $x("//div[contains(@class, 'MuiAvatar-root')]");

    private final ElementsCollection settingsRows = $x("//ul[@role ='menu']").$$x("li");

    public Header() {
        super($x(ELEMENT_XPATH));
    }

    @Step("Проверка отображения элемента")
    public Header checkVisible() {
        $x(ELEMENT_XPATH).shouldBe(visible);

        return this;
    }

    @Step("Переход на страницу друзей")
    public FriendsPage toFriendsPage() {
        settingsButton.click();
        settingsRows.find(text(FRIENDS_BUTTON_TEXT))
                .click();

        return new FriendsPage();
    }

    @Step("Переход на вкладку All Peoples")
    public PeoplePage toAllPeoplesPage() {
        settingsButton.click();
        settingsRows.find(text(ALL_PEOPLE_BUTTON_TEXT))
                .click();

        return new PeoplePage();
    }

    @Step("Переход на страницу профиля")
    public ProfilePage toProfilePage() {
        settingsButton.click();
        settingsRows.find(text(PROFILE_BUTTON_TEXT))
                .click();

        return new ProfilePage();
    }

    @Step("Разлогин")
    public LoginPage signOut() {
        settingsButton.click();
        settingsRows.find(text(SIGN_OUT_BUTTON_TEXT))
                .click();

        return new LoginPage();
    }

    @Step("Переход на страницу добавления траты")
    public EditSpendingPage toAddSpendingPage() {
        newSpendingsButton.click();

        return new EditSpendingPage();
    }

    @Step("Переход на главную страницу")
    public MainPage toMainPage() {
        mainPageButton.click();

        return new MainPage();
    }
}
