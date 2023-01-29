package org.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
    private static final String PATH_NAME = "src/test/resources/config.properties";

    public static Properties getPropertyObject() {
        Properties prop = new Properties();
        String path = new File(PATH_NAME).getAbsolutePath();
        try (FileInputStream fp = new FileInputStream(path)){
            prop.load(fp);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return prop;
    }

    public static String getApiKeyFromProperties() {
        return getPropertyObject().getProperty("apiKey");
    }

    public static String getApiTokenFromProperties() {
        return getPropertyObject().getProperty("apiToken");
    }
}
