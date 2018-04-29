package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;
import com.favis.shopaholic.util.DatabaseUtil;
import com.favis.shopaholic.util.Debugger;
import java.util.ArrayList;
import java.util.List;

public class Shopaholic {

    public static void main(String... args) {
//        PropertyReader.localRun();

        setDebugger();

        DatabaseUtil databaseUtil = new DatabaseUtil();

        System.out.println("Getting items...");
        List<Item> items = databaseUtil.getItems();

        List<ItemHistory> itemHistories;

        if (System.getProperty("threaded").equals("true")) {
            // multi-threading implementation
            System.out.println("Getting prices(multi-thread)...");
            itemHistories = getItemHistories(items);
        } else {
            // Single thread
            System.out.println("Getting prices(single-thread)...");
            WebCrawler webCrawler = new WebCrawler();
            itemHistories = webCrawler.getItemPrices(items);
        }

        System.out.println("Inserting items...");
        long startTime = System.nanoTime();
        databaseUtil.insertItemHistories(itemHistories);
        long endTime = System.nanoTime();
        long totalTime = endTime - startTime;
        System.out.println("DB insert took " + totalTime/1000 + " seconds to complete");

        System.out.println("Comparing current prices...");
        ShopaholicController shopaholicController = new ShopaholicController();
        shopaholicController.checkCurrentValue(items, itemHistories);
    }

    private static List<ItemHistory> getItemHistories(List<Item> items){
        ArrayList<ItemHistory> itemHistories = new ArrayList<ItemHistory>();
        ArrayList<MultiShopaholic> threads = new ArrayList<MultiShopaholic>();

        for(Integer i = 0; i < items.size(); i++){
            MultiShopaholic multiShopaholic = new MultiShopaholic(items.get(i).getItem_name()+"_thread", items.get(i));
            multiShopaholic.start();
            threads.add(multiShopaholic);
        }

        try {
            for(MultiShopaholic multiShopaholic : threads){
                multiShopaholic.getThread().join();
            }
            for(MultiShopaholic multiShopaholic : threads) {
                itemHistories.addAll(multiShopaholic.getItemHistories());
            }
        } catch ( Exception e) {
            Debugger.log("Interrupted");
        }

        return itemHistories;
    }

    private static void setDebugger(){
        if(System.getProperty("debug.enabled").equals("true")) {
            Debugger.enable();
        }
    }

}
