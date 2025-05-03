package guru.qa.niffler.page;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideDriver;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.config.Config;
import guru.qa.niffler.page.component.Header;

import static com.codeborne.selenide.Condition.text;

public abstract class BasePage<T extends BasePage<?>> {

    protected static final Config CFG = Config.getInstance();

    protected final Header header = new Header();

    protected final SelenideElement alert;

    protected BasePage(SelenideDriver driver) {
        this.alert = driver.$(".MuiSnackbar-root");
    }

    public BasePage() {
        this.alert = Selenide.$(".MuiSnackbar-root");
    }

    @SuppressWarnings("unchecked")
    public T checkAlert(String message) {
        alert.shouldHave(text(message));
        return (T) this;
    }
}
