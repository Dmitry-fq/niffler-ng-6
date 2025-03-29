package guru.qa.niffler.condition;

import com.codeborne.selenide.CheckResult;
import com.codeborne.selenide.Driver;
import com.codeborne.selenide.WebElementsCondition;
import guru.qa.niffler.model.rest.SpendJson;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import javax.annotation.Nonnull;
import javax.annotation.ParametersAreNonnullByDefault;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.codeborne.selenide.CheckResult.accepted;
import static com.codeborne.selenide.CheckResult.rejected;

@ParametersAreNonnullByDefault
public class SpendingConditions {

    private static final int CATEGORY_COLUMN_NUMBER = 1;

    private static final int AMOUNT_COLUMN_NUMBER = 2;

    private static final int DESCRIPTION_COLUMN_NUMBER = 3;

    private static final int DATE_COLUMN_NUMBER = 4;

    @Nonnull
    public static WebElementsCondition spends(@Nonnull List<SpendJson> expectedSpendings) {
        return new WebElementsCondition() {

            @NotNull
            @Override
            public CheckResult check(Driver driver, List<WebElement> actualElements) {
                if (expectedSpendings.isEmpty()) {
                    throw new IllegalArgumentException("No expected spendings given");
                }
                if (expectedSpendings.size() != actualElements.size()) {
                    final String message = String.format(
                            "List size mismatch (expected: %s, actual: %s)", expectedSpendings.size(), actualElements.size()
                    );
                    return rejected(message, actualElements.toString());
                }

                for (int i = 0; i < actualElements.size(); i++) {
                    WebElement spendingRow = actualElements.get(i);
                    SpendJson expectedSpend = expectedSpendings.get(i);
                    List<WebElement> actualSpendingCells = spendingRow.findElements(By.tagName("td"));

                    WebElement actualCategory = actualSpendingCells.get(CATEGORY_COLUMN_NUMBER);
                    String actualCategoryText = actualCategory.getText();
                    String expectedCategoryText = expectedSpend.category().name();
                    if (!actualCategoryText.equals(expectedCategoryText)) {
                        String message = String.format(
                                "Element text expected: %s, actual: %s", expectedCategoryText, actualCategoryText
                        );
                        return rejected(message, actualCategory);
                    }

                    WebElement actualAmount = actualSpendingCells.get(AMOUNT_COLUMN_NUMBER);
                    String actualAmountText = actualAmount.getText();
                    String expectedAmountText = convertAmount(expectedSpend.amount());
                    if (!actualAmountText.equals(expectedAmountText)) {
                        String message = String.format(
                                "Element text expected: %s, actual: %s", expectedAmountText, actualAmountText
                        );
                        return rejected(message, actualAmount);
                    }

                    WebElement actualDescription = actualSpendingCells.get(DESCRIPTION_COLUMN_NUMBER);
                    String actualDescriptionText = actualDescription.getText();
                    String expectedDescriptionText = expectedSpend.description();
                    if (!actualDescriptionText.equals(expectedDescriptionText)) {
                        String message = String.format(
                                "Element text expected: %s, actual: %s", expectedDescriptionText, actualDescriptionText
                        );
                        return rejected(message, actualDescription);
                    }

                    WebElement actualDate = actualSpendingCells.get(DATE_COLUMN_NUMBER);
                    String actualDateText = actualDate.getText();
                    String expectedDateText = convertDate(expectedSpend.spendDate());
                    if (!actualDateText.equals(expectedDateText)) {
                        String message = String.format(
                                "Element text expected: %s, actual: %s", expectedDateText, actualDateText
                        );
                        return rejected(message, actualDate);
                    }
                }

                return accepted();
            }

            @Override
            public String toString() {
                return expectedSpendings.toString();
            }

        };
    }

    private static String convertAmount(Double amount) {
        return String.format("%.0f ₽", amount);
    }

    private static String convertDate(Date date) {
        SimpleDateFormat formatter = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);
        return formatter.format(date);
    }
}
