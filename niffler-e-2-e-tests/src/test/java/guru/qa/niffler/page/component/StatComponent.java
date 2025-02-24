package guru.qa.niffler.page.component;

import com.codeborne.selenide.ElementsCollection;
import com.codeborne.selenide.SelenideElement;
import guru.qa.niffler.condition.Bubble;
import guru.qa.niffler.condition.Color;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

import static com.codeborne.selenide.Selenide.$;
import static guru.qa.niffler.condition.StatConditions.color;
import static guru.qa.niffler.condition.StatConditions.statBubbles;
import static guru.qa.niffler.condition.StatConditions.statBubblesContains;
import static guru.qa.niffler.condition.StatConditions.statBubblesInAnyOrder;
import static java.util.Objects.requireNonNull;

public class StatComponent extends BaseComponent<StatComponent> {
    private final ElementsCollection bubbles = self.$("#legend-container").$$("li");

    private final SelenideElement chart = $("canvas[role='img']");

    public StatComponent() {
        super($("#stat"));
    }

    @Step("Get screenshot of stat chart")
    @Nonnull
    public BufferedImage chartScreenshot() throws IOException {
        return ImageIO.read(requireNonNull(chart.screenshot()));
    }

    @Step("Check that stat bubbles contains colors {expectedColors}")
    @Nonnull
    public StatComponent checkBubblesColor(Color... expectedColors) {
        bubbles.should(color(expectedColors));
        return this;
    }

    @Step("Check first Bubble color and text")
    @Nonnull
    public StatComponent checkFirstBubble(Bubble expectedBubble) {
        bubbles.first().should(statBubbles(expectedBubble));
        return this;
    }

    @Step("Check Bubbles any order")
    @Nonnull
    public StatComponent checkBubbles(Bubble... expectedBubbles) {
        bubbles.should(statBubblesInAnyOrder(expectedBubbles));
        return this;
    }

    @Step("Проверка бабблов, если они они встречаются среди прочих")
    @Nonnull
    public StatComponent checkBubblesContains(Bubble... expectedBubbles) {
        bubbles.should(statBubblesContains(expectedBubbles));
        return this;
    }
}
