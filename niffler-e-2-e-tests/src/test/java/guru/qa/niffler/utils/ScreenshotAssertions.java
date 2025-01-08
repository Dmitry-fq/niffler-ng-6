package guru.qa.niffler.utils;

import com.codeborne.selenide.Selenide;
import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import javax.annotation.Nonnull;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class ScreenshotAssertions {

    @Step("Проверка, что изображения отображаются одинаково")
    public static void imagesShouldBeEquals(@Nonnull BufferedImage expectedImage, @Nonnull SelenideElement actualElement) throws IOException {
        Selenide.sleep(3000);

        BufferedImage actualImage = ImageIO.read(Objects.requireNonNull(actualElement.screenshot()));
        assertFalse(new ScreenDiffResult(
                actualImage,
                expectedImage
        ));
    }
}
