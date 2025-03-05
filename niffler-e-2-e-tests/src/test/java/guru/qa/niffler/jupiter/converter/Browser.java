package guru.qa.niffler.jupiter.converter;

import com.codeborne.selenide.SelenideConfig;

public enum Browser {
    CHROME("chrome"),
    FIREFOX("firefox");

    private final String name;

    Browser(String name) {
        this.name = name;
    }

    public SelenideConfig getConfig() {
        return new SelenideConfig()
                .browser(name)
                .pageLoadStrategy("eager")
                .timeout(5000L);
    }
}
