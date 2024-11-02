package guru.qa.niffler.utils;

import com.github.javafaker.Faker;

import java.util.Random;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    private static final Random random = new Random();

    public static String randomUsername() {
        return faker.name().username();
    }

    public static String randomName() {
        return faker.name().firstName();
    }

    public static String randomSurname() {
        return faker.name().lastName();
    }

    public static String randomCategoryName() {
        String categoryName = faker.commerce().material();
        return categoryName + random.nextInt();
    }

    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }

    public static double randomAmount() {
        return faker.number().randomDouble(2, 1, 10000);
    }
}
