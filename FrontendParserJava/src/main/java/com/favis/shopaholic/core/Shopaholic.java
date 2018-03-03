package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;

import javax.mail.MessagingException;
import java.util.List;

public class Shopaholic {

    public static void main(String... args) {
        DatabaseControl databaseControl = new DatabaseControl();

        System.out.println("Getting items...");
        List<Item> items = databaseControl.getItems();

        System.out.println("Getting prices...");
        WebCrawler webCrawler = new WebCrawler();
        List<ItemHistory> itemHistories =  webCrawler.getItemPrices(items);

        System.out.println("Inserting items...");
        databaseControl.insertItemHistories(itemHistories);

        System.out.println("Comparing current prices...");
        ShopaholicController shopaholicController = new ShopaholicController();
        shopaholicController.checkCurrentValue(items, itemHistories);

    }



}
