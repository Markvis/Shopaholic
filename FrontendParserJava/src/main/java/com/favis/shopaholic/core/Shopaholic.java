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
        double totalTime = (double)(endTime - startTime) / 1000000000.0;
        System.out.println("DB insert took " + totalTime + " seconds to complete");

        System.out.println("Comparing current prices...");
        ShopaholicController shopaholicController = new ShopaholicController();
        shopaholicController.checkCurrentValue(items, itemHistories);
    }

    private static List<ItemHistory> getItemHistories(List<Item> items){
        int maxThreadCount = 2;
        ArrayList<ItemHistory> itemHistories = new ArrayList<ItemHistory>();
        ArrayList<MultiShopaholic> newThreads = new ArrayList<MultiShopaholic>();
        ArrayList<MultiShopaholic> startedThreads = new ArrayList<MultiShopaholic>();
        ArrayList<MultiShopaholic> completedThreads = new ArrayList<MultiShopaholic>();

        // create new threads
        for(Integer i = 0; i < items.size(); i++){
            MultiShopaholic multiShopaholic = new MultiShopaholic(items.get(i).getItem_name()+"_thread", items.get(i));
            newThreads.add(multiShopaholic);
        }

        try {
            while(newThreads.size() > 0){
                // start threads and move to started
                while(startedThreads.size() < maxThreadCount) {
                    MultiShopaholic multiShopaholic = newThreads.get(0);
                    multiShopaholic.start();
                    startedThreads.add(multiShopaholic);
                    newThreads.remove(multiShopaholic);
                }

                // move completed threads to completedThread
                for(MultiShopaholic multiShopaholic : startedThreads){
                    if(!multiShopaholic.getThread().isAlive()) {
                        completedThreads.add(multiShopaholic);
                        startedThreads.remove(multiShopaholic);
                    }
                }

                Thread.sleep(500);
            }

            // save items
            for(MultiShopaholic multiShopaholic : completedThreads) {
                itemHistories.addAll(multiShopaholic.getItemHistories());
            }
        } catch ( Exception e) {
            Debugger.log("Interrupted");
        }

        return itemHistories;
    }

    private void startThread(){

    }

    private static void setDebugger(){
        if(System.getProperty("debug.enabled").equals("true")) {
            Debugger.enable();
        }
    }

}
