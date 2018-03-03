package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;

import java.util.List;

public class Shopaholic {

    public static void main(String... args) {
        DatabaseControl databaseControl = new DatabaseControl();

        // get db items
        System.out.println("Getting items...");
        List<Item> items = databaseControl.getItems();

        // crawl sites
        System.out.println("Getting prices...");
        WebCrawler webCrawler = new WebCrawler();
        List<ItemHistory> itemHistories =  webCrawler.getItemPrices(items);

        System.out.println("Inserting items...");
        databaseControl.insertItemHistories(itemHistories);

        // print item_name of items list
//        for (Item item : items) {
//            System.out.println(item.getItem_name());
//        }

    }

}
