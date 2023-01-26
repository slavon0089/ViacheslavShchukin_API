package org.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

public class Config {
    static String pathName = "src/test/resources/config.properties";

    public static Properties getPropertyObject(String pathName) {
        Properties prop = new Properties();
        try {
            String path = new File(pathName).getAbsolutePath();
            FileInputStream fp = new FileInputStream(path);

            prop.load(fp);
            fp.close();
        } catch (Exception e) {
            System.out.println("errors in the properties");
        }

        return prop;
    }

    public static String getApiKeyFromProperties() {
        return getPropertyObject(pathName).getProperty("apiKey");
    }

    public static String getApiTokenFromProperties() {
        return getPropertyObject(pathName).getProperty("apiToken");
    }
}
