package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;
import com.favis.shopaholic.containers.ItemURL;
import com.favis.shopaholic.util.Debugger;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

public class WebCrawler {

    private WebDriver webDriver;
    private Properties locatorsProp;
    private String delimiters = "[\\$\\,]";

    WebCrawler() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");

        try {
            webDriver = new RemoteWebDriver(new URL(System.getProperty("selenium.grid.url")), chromeOptions);
            getLocators();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
    }

    protected void finalize() throws Throwable {
        cleanUp();
        super.finalize();
    }

    private void getLocators() {
        locatorsProp = new Properties();
        ClassLoader loader = Thread.currentThread().getContextClassLoader();
        InputStream locatorsStream = loader.getResourceAsStream("locators.properties");

        try {
            locatorsProp.load(locatorsStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<ItemHistory> getItemPrices(Item item){
        List<ItemHistory> itemHistories = new ArrayList<ItemHistory>();

        long startTime = System.nanoTime();
        for (ItemURL itemURL : item.getItem_urls()) {
            String price = getPriceFromItemURL(itemURL);
            Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
            BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(price));
            itemHistories.add(new ItemHistory(itemURL.getItem_name(), itemURL.getStore_name(), bd, date, itemURL.getUrl()));
        }
        long endTime = System.nanoTime();
        double totalTime = (double)(endTime - startTime) / 1000000000.0;
        System.out.println(item.getItem_name() + " took " + totalTime + " seconds to execute");

        return itemHistories;
    }

    public List<ItemHistory> getItemPrices(List<Item> items) {

        List<ItemHistory> itemHistories = new ArrayList<ItemHistory>();

        for (Item item : items) {
            for (ItemURL itemURL : item.getItem_urls()) {
                String price = getPriceFromItemURL(itemURL);
                Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
                BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(price));
                itemHistories.add(new ItemHistory(itemURL.getItem_name(), itemURL.getStore_name(), bd, date, itemURL.getUrl()));
            }
        }

        return itemHistories;
    }

    private String getPriceFromItemURL(ItemURL itemURL) {
        Debugger.log("Fetching price for: " + itemURL.getItem_name());
        Debugger.log("Navigating to: " + itemURL.getUrl());
        webDriver.get(itemURL.getUrl());

        try {
            if (itemURL.getStore_name().equals("bestbuy")) {
                return getBestbuyItemPrice();
            } else if (itemURL.getStore_name().equals("amazon")) {
                return getAmazonItemPrice();
            } else if (itemURL.getStore_name().equals("newegg")) {
                return getNeweggItemPrice();
            } else if (itemURL.getStore_name().equals("bhphotovideo")) {
                return getBnHPhotoItemPrice();
            } else if (itemURL.getStore_name().equals("dell")) {
                return getDellItemPrice();
            } else
                return "SITE: " + itemURL.getUrl() + " NOT SUPPORTED";
        } catch (Exception e) {
            return "-31337";
        }
    }

    private String getBestbuyItemPrice() throws Exception {
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("bestbuy.price_tag_locator")));
        return locator.getAttribute(locatorsProp.getProperty("bestbuy.price_attribute")).replaceAll(delimiters, "");
    }

    private String getAmazonItemPrice() throws Exception {
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("amazon.price_tag_locator")));
        return locator.getText().replaceAll(delimiters, "");
    }

    private String getNeweggItemPrice() throws Exception {
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("newegg.price_tag_locator")));
        return locator.getText().replaceAll(delimiters, "");
    }

    private String getBnHPhotoItemPrice() throws Exception {
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("bhphotovideo.price_tag_locator")));
        return locator.getAttribute("content");
    }

    private String getDellItemPrice() throws Exception {
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("dell.price_tag_locator")));
        return locator.getText().replaceAll(delimiters, "");
    }

    public void cleanUp() {
        webDriver.quit();
    }
}
