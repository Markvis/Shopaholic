package com.favis.shopaholic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.*;

public class WebCrawler {

    private static WebDriver webDriver;
    private Properties locatorsProp;


    WebCrawler(){
        ChromeOptions chromeOptions = new ChromeOptions();

        try {
            webDriver = new RemoteWebDriver(new URL(PropertyReader.getProperty("selenium.grid.url")), chromeOptions);
            getLocators();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        cleanUp();
        super.finalize();
    }

    private void getLocators(){
        locatorsProp = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream locatorsStream = loader.getResourceAsStream("locators.properties");

        try {
            locatorsProp.load(locatorsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

//    public List<Double> getPriceForItem(String item_name){
//
//        ArrayList<Double> prices = new ArrayList<Double>();
//
//        List <String> itemURLs = DatabaseControl.getURLsForItem(item_name);
//        for(String url : itemURLs) {
//            String price = getPrice(url);
//            prices.add(Double.valueOf(price));
//        }
//
//        return prices;
//    }

    public String getPrice(String url){
        webDriver.get(url);

        if(url.contains("bestbuy.com")){
            return getBestbuyItemPrice();
        }
        else if(url.contains("amazon.com")){
            return getAmazonItemPrice();
        }
        else
            return "SITE: " + url + " NOT SUPPORTED";
    }

    private String getBestbuyItemPrice(){
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("bestbuy.price_tag_locator")));
        return locator.getAttribute(locatorsProp.getProperty("bestbuy.price_attribute"));
    }

    private String getAmazonItemPrice(){
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("amazon.price_tag_locator")));
        return locator.getText().replace("$","");
    }

    private void cleanUp(){
        webDriver.quit();
    }
}
