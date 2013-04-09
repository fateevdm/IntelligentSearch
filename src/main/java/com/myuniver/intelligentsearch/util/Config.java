package com.myuniver.intelligentsearch.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

/**
 * User: Dmitry Fateev
 * Date: 08.04.13
 * Time: 22:54
 */
public class Config {

    private Properties properties = new Properties();
    private String filePath = "src/main/resources/config.properties";

    private Config() {
        try {
            properties.load(new FileInputStream(filePath));
        } catch (IOException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }

    private static class Prop {
        private static final Config props = new Config();


    }


    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String defaultValue) throws IllegalAccessException {
        if (key == null) throw new IllegalAccessException("key should not be null");
        return properties.getProperty(key, defaultValue);
    }

    public static Config getConfig() {
        return Prop.props;
    }
}
