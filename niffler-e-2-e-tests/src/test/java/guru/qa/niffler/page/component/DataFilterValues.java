package guru.qa.niffler.page.component;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DataFilterValues {
    ALL_TIME("All time"),

    LAST_MONTH("Last month"),

    LAST_WEEK("Last week"),

    TODAY("Today");

    public final String value;
}
