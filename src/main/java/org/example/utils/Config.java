package org.example.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {

    public static Properties getPropertyObject() throws IOException {
        String path = new File("C:/config.properties").getAbsolutePath();
        FileInputStream fp = new FileInputStream(path);
        Properties prop = new Properties();
        prop.load(fp);
        return prop;
    }

    public static String getUserNameFromProperties() throws IOException {
        return getPropertyObject().getProperty("apiKey");
    }

    public static String getUserPasswordFromProperties() throws IOException {
        return getPropertyObject().getProperty("apiToken");
    }

}
