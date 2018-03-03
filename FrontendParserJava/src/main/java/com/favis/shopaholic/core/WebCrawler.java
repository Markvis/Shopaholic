package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;
import com.favis.shopaholic.containers.ItemURL;
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
import java.util.*;

public class WebCrawler {

    private static WebDriver webDriver;
    private Properties locatorsProp;


    WebCrawler() {
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

    public List<ItemHistory> getItemPrices(List<Item> items) {

        List<ItemHistory> itemHistories = new ArrayList<ItemHistory>();

        for (Item item : items) {
            for (ItemURL itemURL : item.getItem_urls()) {
                String price = getPriceFromItemURL(itemURL);
                Timestamp date = new java.sql.Timestamp(new java.util.Date().getTime());
                BigDecimal bd = BigDecimal.valueOf(Double.parseDouble(price));
                itemHistories.add(new ItemHistory(itemURL.getItem_name(), itemURL.getStore_name(), bd, date));
            }
        }

        return itemHistories;
    }

    private String getPriceFromItemURL(ItemURL itemURL) {
        System.out.println("Navigating to: " + itemURL.getUrl());
        webDriver.get(itemURL.getUrl());

        if (itemURL.getStore_name().equals("bestbuy")) {
            return getBestbuyItemPrice();
        } else if (itemURL.getStore_name().equals("amazon")) {
            return getAmazonItemPrice();
        } else
            return "SITE: " + itemURL.getUrl() + " NOT SUPPORTED";
    }

    private String getBestbuyItemPrice() {
        WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("bestbuy.price_tag_locator")));
        return locator.getAttribute(locatorsProp.getProperty("bestbuy.price_attribute"));
    }

    private String getAmazonItemPrice() {
        try {
            WebElement locator = webDriver.findElement(By.xpath(locatorsProp.getProperty("amazon.price_tag_locator")));
            return locator.getText().replace("$", "");
        } catch (Exception e) {
            return "-1";
        }
    }

    private void cleanUp() {
        webDriver.quit();
    }
}
