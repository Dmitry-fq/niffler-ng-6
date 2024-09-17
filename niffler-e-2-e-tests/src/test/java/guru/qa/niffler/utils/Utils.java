package guru.qa.niffler.utils;

import wiremock.org.apache.commons.lang3.RandomStringUtils;

public class Utils {

    public static String getRandomName(int min, int max) {
        return RandomStringUtils.randomAlphabetic(min, max);
    }

    public static String getRandomNameIfEmpty(String str, int min, int max) {
        return str.isEmpty() ? getRandomName(min, max) : str;
    }
}
