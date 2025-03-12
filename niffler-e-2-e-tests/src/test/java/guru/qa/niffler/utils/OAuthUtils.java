package guru.qa.niffler.utils;

import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.SecureRandom;
import java.util.Base64;

public class OAuthUtils {

    public static String generateCodeVerifier() {
        SecureRandom random = new SecureRandom();
        byte[] bytes = new byte[32];
        random.nextBytes(bytes);

        return base64UrlEncode(bytes);
    }

    public static String generateCodeChallenge(String codeVerifier) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(codeVerifier.getBytes(StandardCharsets.UTF_8));

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

    public static String getCodeFromRedirectUrl(String redirectUrl) {
        URI uri = URI.create(redirectUrl);
        String queryParam = uri.getQuery();

        String[] params = queryParam.split("&");
        for (String param : params) {
            String[] keyValue = param.split("=");
            if (keyValue[0].equals("code")) {
                return keyValue[1];
            }
        }
        throw new IllegalArgumentException("Invalid redirect URL: " + redirectUrl);
    }
}
