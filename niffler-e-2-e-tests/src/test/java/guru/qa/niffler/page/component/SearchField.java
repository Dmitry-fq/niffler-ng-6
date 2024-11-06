package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$x;

public class SearchField extends BaseComponent<SearchField> {
    private static final String ELEMENT_XPATH = "//form[div[input[@aria-label= 'search']]]";

    private final SelenideElement searchInput = $x("//input[@aria-label= 'search']");

    public SearchField(@Nonnull SelenideElement self) {
        super(self);
    }

    public SearchField() {
        super($x(ELEMENT_XPATH));
    }

    @Step("Проверка отображения элемента")
    public SearchField checkVisible() {
        $x(ELEMENT_XPATH).shouldBe(visible);

        return this;
    }

    @Step("Поиск")
    public SearchField search(String query) {
        searchInput.setValue(query)
                .pressEnter();

        return this;
    }
}
