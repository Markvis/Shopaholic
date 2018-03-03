package com.favis.shopaholic.core;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyReader {

    public static String getProperty(String propertyName) {
        Properties appProperties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream urlStream = loader.getResourceAsStream("application.properties");

        try {
            appProperties.load(urlStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        return appProperties.getProperty(propertyName);
    }

}
