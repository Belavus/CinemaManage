package main.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigUtil {
    private static final String CONFIG_FILE = "config.properties"; // Правильный путь
    private static Properties properties = new Properties();

    static {
        try (InputStream inputStream = ConfigUtil.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (inputStream != null) {
                properties.load(inputStream);
            } else {
                throw new RuntimeException("Configuration file " + CONFIG_FILE + " not found in the classpath.");
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to load configuration file " + CONFIG_FILE, e);
        }
    }

    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}
