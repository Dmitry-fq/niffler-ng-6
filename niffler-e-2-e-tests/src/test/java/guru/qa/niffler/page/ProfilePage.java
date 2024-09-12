package guru.qa.niffler.page;

import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static org.assertj.core.api.Assertions.assertThat;

public class ProfilePage implements Header {

    private final SelenideElement username = $("input[name='username']");

    public void checkUsername(String currentUsername) {
        assertThat(username.getValue())
                .as("username некорректный")
                .isEqualTo(currentUsername);
    }
}