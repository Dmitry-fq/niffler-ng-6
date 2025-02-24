package guru.qa.niffler.condition;

import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum Color {
    yellow("rgba(255, 183, 3, 1)"),
    green("rgba(53, 173, 123, 1)"),
    orange("rgba(251, 133, 0, 1)");

    public final String rgba;

    public static Color fromRgba(String rgba) {
        for (Color color : values()) {
            if (color.rgba.equals(rgba)) {
                return color;
            }
        }
        throw new IllegalArgumentException("Unknown color: " + rgba);
    }
}
