package guru.qa.niffler.utils;

import com.github.javafaker.Faker;
import wiremock.org.apache.commons.lang3.RandomStringUtils;

public class RandomDataUtils {

    private static final Faker faker = new Faker();

    public static String getRandomName(int min, int max) {
        return RandomStringUtils.randomAlphabetic(min, max);
    }

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
        return faker.commerce().material();
    }

    public static String randomSentence(int wordsCount) {
        return faker.lorem().sentence(wordsCount);
    }
}
