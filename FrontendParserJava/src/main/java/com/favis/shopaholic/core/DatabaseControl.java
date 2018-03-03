package com.favis.shopaholic.core;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;
import com.favis.shopaholic.containers.ItemURL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseControl {
    private Connection conn;

    private List<String> convertResultSetToStringList(ResultSet rs) {
        ArrayList<String> list = new ArrayList<String>();

        try {
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return list;
    }

    private Connection connect() {
        conn = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conn = DriverManager.getConnection(
                    PropertyReader.getProperty("mysql.url"),
                    PropertyReader.getProperty("mysql.username"),
                    PropertyReader.getProperty("mysql.password"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return conn;
    }

    private ResultSet executeQuery(String query) {
        ResultSet rs = null;
//        System.out.println(query);

        try {
            Statement stmt = connect().createStatement();
            rs = stmt.executeQuery(query);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return rs;
    }

    private List<String> getItemNamesList() {
        String query = "SELECT `items`.`item_name` FROM `Shopaholic`.`items`;";
        ResultSet rs = executeQuery(query);

        return convertResultSetToStringList(rs);
    }

    public List<Item> getItems() {
        ArrayList<Item> items = new ArrayList<Item>();
        List<String> item_names = getItemNamesList();

        for (String item_name : item_names) {
            items.add(getItem(item_name));
        }

        return items;
    }

    private List<ItemURL> getURLsForItem(String item_name) {
        String query = "SELECT * FROM `Shopaholic`.`item_urls` WHERE `item_urls`.`item_name` LIKE '" + item_name + "';";
        ResultSet rs = executeQuery(query);
        ArrayList<ItemURL> itemURLs = new ArrayList<ItemURL>();

        try {
            while (rs.next()) {
                itemURLs.add(new ItemURL(item_name, rs.getString("store_name"), rs.getString("item_url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemURLs;
    }

    public Item getItem(String item_name) {
        String query = "SELECT * FROM `Shopaholic`.`items` WHERE `items`.`item_name` LIKE '" + item_name + "';";
        ResultSet rs = executeQuery(query);
        Item item = null;

        try {
            rs.next();
            item = new Item(item_name);
            item.setItem_desc(rs.getString("item_desc"));
            item.setItem_latest_price(rs.getBigDecimal("item_latest_price"));
            item.setItem_max_price(rs.getBigDecimal("item_max_price"));
            item.setItem_min_price(rs.getBigDecimal("item_min_price"));
            item.setItem_min_store_name(rs.getString("item_min_store_name"));
            item.setItem_msrp(rs.getBigDecimal("item_msrp"));
            item.setItem_urls(getURLsForItem(item_name));
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return item;
    }

    public List<ItemHistory> getItemHistories(String item_name) {
        String query = "SELECT * FROM `Shopaholic`.`item_history` WHERE `item_history`.`item_name` LIKE '" + item_name + "';";
        ResultSet rs = executeQuery(query);
        ArrayList<ItemHistory> itemHistories = new ArrayList<ItemHistory>();

        try {
            while (rs.next()) {
                itemHistories.add(new ItemHistory(item_name, rs.getString("store_name"), rs.getBigDecimal("item_price"), rs.getDate("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return itemHistories;
    }

    public void insertItemHistories(List<ItemHistory> itemHistories) {
        for (ItemHistory itemHistory : itemHistories) {
//            System.out.println("Inserting " + itemHistory.getItem_name() + " to the DB...");
            String query = "INSERT INTO `Shopaholic`.`item_history` " +
                    "(`item_name`, " +
                    "`store_name`, " +
                    "`item_price`, " +
                    "`date`) " +
                    "VALUES " +
                    "('" + itemHistory.getItem_name() + "', " +
                    "'" + itemHistory.getStore_name() + "', " +
                    "'" + itemHistory.getItem_price() + "', " +
                    "'" + itemHistory.getDate() + "'); ";
            try {
//                System.out.println(query);
                Statement stmt = connect().createStatement();
                stmt.executeUpdate(query);
            }catch (SQLException e){
                e.printStackTrace();
            }
        }
    }

    private void updateItemsListInDB(List<Item> items){

    }

    protected void finalize() throws Throwable {
        try {
            conn.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        super.finalize();
    }
}
