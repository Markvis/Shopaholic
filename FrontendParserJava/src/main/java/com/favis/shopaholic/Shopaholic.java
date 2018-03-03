package com.favis.shopaholic;

import java.util.List;

public class Shopaholic {

    public static void main(String... args) {
        DatabaseControl databaseControl = new DatabaseControl();

        List<Item> items = databaseControl.getItems();



        // print item_name of items list
        for (Item item : items) {
            System.out.println(item.getItem_name());
        }

    }

}
