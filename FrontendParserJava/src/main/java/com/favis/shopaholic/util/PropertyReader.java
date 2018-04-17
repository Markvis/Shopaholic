package com.favis.shopaholic.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
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

    /**
     * set application properties values as system properties
     */
    public static void localRun() {
        Properties appProperties = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream urlStream = loader.getResourceAsStream("application.properties");

        try {
            appProperties.load(urlStream);
        } catch (IOException e) {
            e.printStackTrace();
        }

        ArrayList<String> props = new ArrayList<String>();
        Enumeration<?> propertyNames = appProperties.propertyNames();
        while(propertyNames.hasMoreElements()){
            props.add(propertyNames.nextElement().toString());
        }

        for(String name:props){
            System.setProperty(name,appProperties.getProperty(name));
        }
    }

}
