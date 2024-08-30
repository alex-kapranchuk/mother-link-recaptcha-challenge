package utils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.microsoft.playwright.options.Cookie;

import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class ReaderResources {

    public static List<Cookie> readCookiesFromResource(String resourcePath) throws IOException {
        try (InputStreamReader reader = new InputStreamReader(
                ReaderResources.class.getResourceAsStream(resourcePath), StandardCharsets.UTF_8)) {
            Gson gson = new Gson();
            return gson.fromJson(reader, new TypeToken<List<Cookie>>(){}.getType());
        }
    }

    /**
     * Retrieves the path to the unpacked extension directory from the environment variable EXTENSION_PATH.
     * @return the extension path if set, otherwise an empty string.
     */
    public static String getExtensionPath() {
        String extensionPath = System.getenv("EXTENSION_PATH");
        if (extensionPath == null || extensionPath.isEmpty()) {
            System.out.println("Environment variable EXTENSION_PATH is not set or empty.");
            return "";
        } else {
            System.out.println("Extension path is: " + extensionPath);
            return extensionPath;
        }
    }
}
