package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import javax.annotation.Nonnull;
import java.util.Random;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    private static final Random random = new Random();

    @Nonnull
    public static String randomUsername() {
        return faker.name().username();
    }

    @Nonnull
    public static String randomName() {
        return faker.name().firstName();
    }

    @Nonnull
    public static String randomSurname() {
        return faker.name().lastName();
    }

    @Nonnull
    public static String randomCategoryName() {
        String categoryName = faker.commerce().material();
        return categoryName + random.nextInt();
    }

    @Nonnull
    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    @Nonnull
    public static double randomAmount() {
        return faker.number().randomDouble(2, 1, 10000);
    }
}
