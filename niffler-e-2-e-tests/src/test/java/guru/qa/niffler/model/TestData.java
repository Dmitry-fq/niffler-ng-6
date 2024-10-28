package guru.qa.niffler.model;

import java.util.List;

public record TestData(
        String password,
        List<CategoryJson> categories,
        List<SpendJson> spendings,
        List<String> friends,
        List<String> incomeInvitation,
        List<String> outcomeInvitation
) {
}
