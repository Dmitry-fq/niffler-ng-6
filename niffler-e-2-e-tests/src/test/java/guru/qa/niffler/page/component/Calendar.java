package guru.qa.niffler.page.component;

import com.codeborne.selenide.SelenideElement;

import java.time.LocalDate;
import java.time.Month;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$x;

public class Calendar {

    private final SelenideElement calendarBody = $x("//div[contains(@class, 'MuiDateCalendar-viewTransitionContainer')]");

    private final SelenideElement monthAndYearButton = $x("//div[contains(@id, 'grid-label')]");

    private final SelenideElement arrowLeftButton = $x("//*[@data-testid = 'ArrowLeftIcon']");

    private final SelenideElement arrowRightButton = $x("//*[@data-testid = 'ArrowRightIcon']");

    public Calendar selectDateInCalendar(LocalDate date) {
        setYear(date.getYear());
        setMonth(date.getMonth());
        setDay(date.getDayOfMonth());

        return new Calendar();
    }

    private void setYear(int year) {
        monthAndYearButton.click();
        String yearXpath = ".//button";
        calendarBody.$$x(yearXpath)
                .find(text(Integer.toString(year)))
                .click();
    }

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

    private void setDay(int day) {
        String dayXpath = ".//button";
        calendarBody.$$x(dayXpath)
                .find(text(Integer.toString(day)))
                .click();
    }
}
