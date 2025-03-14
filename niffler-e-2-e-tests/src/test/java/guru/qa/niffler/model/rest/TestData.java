package guru.qa.niffler.model.rest;

import javax.annotation.Nonnull;
import java.util.ArrayList;
import java.util.List;

public record TestData(
        String password,
        List<CategoryJson> categories,
        List<SpendJson> spendings,
        List<UserJson> friends,
        List<UserJson> incomeInvitations,
        List<UserJson> outcomeInvitations
) {

    public TestData(@Nonnull String password) {
        this(password, new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>(), new ArrayList<>());
    }
}
