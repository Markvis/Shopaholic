package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;

import java.util.List;

public class Shopaholic {

    public static void main(String... args) {
        DatabaseControl databaseControl = new DatabaseControl();

        // get db items
        List<Item> items = databaseControl.getItems();

        // crawl sites
        WebCrawler webCrawler = new WebCrawler();
        webCrawler.getItemPrices(items);

        // print item_name of items list
        for (Item item : items) {
            System.out.println(item.getItem_name());
        }

    }

}
