package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import lombok.NonNull;

import java.util.Random;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    private static final Random random = new Random();

    @NonNull
    public static String randomUsername() {
        return faker.name().username();
    }

    @NonNull
    public static String randomName() {
        return faker.name().firstName();
    }

    @NonNull
    public static String randomSurname() {
        return faker.name().lastName();
    }

    @NonNull
    public static String randomCategoryName() {
        String categoryName = faker.commerce().material();
        return categoryName + random.nextInt();
    }

    @NonNull
    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    @NonNull
    public static double randomAmount() {
        return faker.number().randomDouble(2, 1, 10000);
    }
}
