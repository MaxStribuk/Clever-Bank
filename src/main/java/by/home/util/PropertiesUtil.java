package by.home.util;

import by.home.data.exception.PropertiesLoadException;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import static by.home.util.Constant.Utils.PROPERTIES_FILE_NAME;

/**
 * класс для парсинга файла properties
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class PropertiesUtil {

    private static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public static String getProperty(String key) {
        return PROPERTIES.getProperty(key);
    }

    private static void loadProperties() {
        try (InputStream resourceAsStream = PropertiesUtil.class
                .getClassLoader()
                .getResourceAsStream(PROPERTIES_FILE_NAME)) {
            PROPERTIES.load(resourceAsStream);
        } catch (IOException e) {
            throw new PropertiesLoadException("failed load " + PROPERTIES_FILE_NAME, e);
        }
    }
}
