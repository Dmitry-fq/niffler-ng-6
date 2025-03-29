package guru.qa.niffler.utils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class OAuthUtils {

    private static SecureRandom secureRandom = new SecureRandom();

    public static String generateCodeVerifier() {
        byte[] bytes = new byte[32];
        secureRandom.nextBytes(bytes);

        return base64UrlEncode(bytes);
    }

    public static String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.US_ASCII));

            return base64UrlEncode(hash);

        } catch (Exception e) {
            throw new RuntimeException("Failed to generate code challenge", e);
        }
    }

    private static String base64UrlEncode(byte[] input) {
        return Base64.getUrlEncoder()
                     .withoutPadding()
                     .encodeToString(input);
    }
}
