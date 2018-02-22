package com.favis.shopaholic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Properties;

public class GetPrices {

    private static WebDriver webDriver;
    private static Properties urlsProp;
    private static Properties locatorsProp;


    GetPrices(){
        DesiredCapabilities capability = DesiredCapabilities.chrome();

        try {
            webDriver = new RemoteWebDriver(new URL("http://192.168.3.240:4444/wd/hub"), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        getUrlsAndLocators();
    }

    private void getUrlsAndLocators(){
        urlsProp = new Properties();
        locatorsProp = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream urlStream = loader.getResourceAsStream("urls.properties");;
        InputStream locatorsStream = loader.getResourceAsStream("locators.properties");

        try {
            urlsProp.load(urlStream);
            locatorsProp.load(locatorsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void getAllPrices(){

        System.out.println(getPrice(urlsProp.getProperty("alienware_aw3418dw")));
    }

    public String getPrice(String url){
        webDriver.get(url);

        if(url.contains("https://www.bestbuy.com")){
            return getBestbuyItemPrice();
        }
        else
            return "SITE: " + url + " NOT SUPPORTED";
    }

    private String getBestbuyItemPrice(){
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("bestbuy.price_tag_locator")));
        return locator.getAttribute(locatorsProp.getProperty("bestbuy.price_attribute"));
    }
}
