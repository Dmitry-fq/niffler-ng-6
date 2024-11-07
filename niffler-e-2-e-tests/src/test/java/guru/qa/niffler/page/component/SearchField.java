package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import static com.codeborne.selenide.Selenide.$x;

public class SearchField extends BaseComponent {

    private final SelenideElement searchInput = $x("//input[@aria-label= 'search']");

    public SearchField() {
        super($x("//form[div[input[@aria-label= 'search']]]"));
    }

    @Step("Поиск")
    public SearchField search(String query) {
        searchInput.setValue(query)
                .pressEnter();

        return this;
    }
}
