package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;
import io.qameta.allure.Step;

import java.time.LocalDate;
import java.time.Month;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

public class Calendar extends BaseComponent {

    private final SelenideElement calendarBody = $x("//div[contains(@class, 'MuiDateCalendar-viewTransitionContainer')]");

    private final SelenideElement monthAndYearButton = $x("//div[contains(@id, 'grid-label')]");

    private final SelenideElement arrowLeftButton = $x("//*[@data-testid = 'ArrowLeftIcon']");

    private final SelenideElement arrowRightButton = $x("//*[@data-testid = 'ArrowRightIcon']");

    public Calendar() {
        super($x("//div[div[contains(@class, 'MuiDateCalendar-viewTransitionContainer')]]"));
    }

    @Step("Установка даты")
    public Calendar selectDateInCalendar(LocalDate date) {
        setYear(date.getYear());
        setMonth(date.getMonth());
        setDay(date.getDayOfMonth());

        return this;
    }

    @Step("Установка года")
    private void setYear(int year) {
        monthAndYearButton.click();
        String yearXpath = ".//button";
        calendarBody.$$x(yearXpath)
                .find(text(Integer.toString(year)))
                .click();
    }

    @Step("Установка месяца")
    private void setMonth(Month month) {
        while (true) {
            String currentMonthText = monthAndYearButton.getText().toLowerCase();
            String desiredMonthText = month.name().toLowerCase();

            if (currentMonthText.contains(desiredMonthText)) {
                break;
            }
            arrowRightButton.click();
        }
    }

    @Step("Установка дня")
    private void setDay(int day) {
        String dayXpath = ".//button";
        calendarBody.$$x(dayXpath)
                .find(text(Integer.toString(day)))
                .click();
    }
}
