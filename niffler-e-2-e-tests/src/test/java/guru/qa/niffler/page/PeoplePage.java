package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;

@ParametersAreNonnullByDefault
public class PeoplePage extends BasePage<PeoplePage> {

    private final SelenideElement peopleTab = $("a[href='/people/friends']");

    private final SelenideElement allTab = $("a[href='/people/all']");

    private final SelenideElement peopleTable = $("#all");

    @Nonnull
    @Step("Проверка, что запрос дружбы отправлен по username")
    public PeoplePage checkInvitationSentToUser(String username) {
        SelenideElement friendRow = peopleTable.$$("tr").find(text(username));
        friendRow.shouldHave(text("Waiting..."));
        return this;
    }
}
