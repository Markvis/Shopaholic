package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;
import com.favis.shopaholic.util.DatabaseUtil;
import com.favis.shopaholic.util.GoogleMail;
import com.favis.shopaholic.util.PropertyReader;

import javax.mail.MessagingException;
import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;

public class ShopaholicController {
    final private static BigDecimal errorValue = new BigDecimal(-31337);

    public void checkCurrentValue(List<Item> items, List<ItemHistory> itemHistories) {
        Collections.sort(items);

        DatabaseUtil databaseUtil = new DatabaseUtil();

        for (ItemHistory itemHistory : itemHistories) {
            Integer index = Collections.binarySearch(items, new Item(itemHistory.getItem_name()));
            Item item = items.get(index);

            BigDecimal currentPrice = itemHistory.getItem_price();

            if(currentPrice.compareTo(errorValue) != 0) {
                // set latest_price
//              item.setItem_latest_price(itemHistory.getItem_price());

                // set min and storename
                if (currentPrice.compareTo(item.getItem_min_price()) < 0) {
                    item.setItem_min_price(itemHistory.getItem_price());
                    item.setItem_min_store_name(itemHistory.getStore_name());
                }

                // set max price
                if (currentPrice.compareTo(item.getItem_max_price()) > 0) {
                    item.setItem_max_price(itemHistory.getItem_price());
                }

                // Last if statement
                if (currentPrice.compareTo(item.getItem_msrp()) < 0) {
                    sendMail(itemHistory);
                }
            }

            // set new item values
            items.set(index,item);

            // update db of changes
            databaseUtil.updateItemInDB(item);

        }

        // UPDATE ITEMS in DB
//        DatabaseUtil databaseUtil = new DatabaseUtil();
//        databaseUtil.updateItemsListInDB(items);
    }

    private void sendMail(ItemHistory itemHistory) {
        try {
            GoogleMail.Send(System.getProperty("email.username"),
                    System.getProperty("email.app.password"),
                    System.getProperty("email.recipient"),
                    "Shopaholic: " + itemHistory.getItem_name() + " $" + itemHistory.getItem_price() + " @ " + itemHistory.getStore_name(),
                    itemHistory.getUrl());
        } catch (MessagingException e) {
            e.printStackTrace();
        }
    }
}
