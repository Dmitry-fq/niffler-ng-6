package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$x;

public class SearchField {

    private final SelenideElement searchInput = $x("//input[@aria-label= 'search']");

    public SearchField search(String query) {
        searchInput.setValue(query)
                .pressEnter();

        return this;
    }
}
