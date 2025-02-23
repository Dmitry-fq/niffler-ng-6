package guru.qa.niffler.condition;

import java.util.Date;

public record Spending(String category, Double amount, String description, Date date) {
}
