package com.favis.shopaholic.util;

import com.favis.shopaholic.containers.Item;
import com.favis.shopaholic.containers.ItemHistory;
import com.favis.shopaholic.containers.ItemURL;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DatabaseUtil {

    private Connection connect() {
        Connection conn;
        try {
            Class.forName(PropertyReader.getProperty("mysql.driver.class"));
            conn = DriverManager.getConnection(
                    System.getProperty("mysql.url"),
                    System.getProperty("mysql.username"),
                    System.getProperty("mysql.password"));
            return conn;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    private List<String> getItemNamesList() {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        String query = "SELECT `items`.`item_name` FROM `Shopaholic`.`items`;";

        ArrayList<String> list = new ArrayList<String>();

        try {
            conn = connect();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                list.add(rs.getString(1));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(rs != null){rs.close();}
                if(ps != null){ps.close();}
                if(conn != null){conn.close();}
            }catch (SQLException s){
                s.printStackTrace();
            }
        }

        return list;
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
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ArrayList<ItemURL> itemURLs = new ArrayList<ItemURL>();
        String query = "SELECT * FROM `Shopaholic`.`item_urls` WHERE `item_urls`.`item_name` LIKE '" + item_name + "';";

        try {
            conn = connect();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                itemURLs.add(new ItemURL(item_name, rs.getString("store_name"), rs.getString("item_url")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }finally {
            try{
                if(rs != null){rs.close();}
                if(ps != null){ps.close();}
                if(conn != null){conn.close();}
            }catch (SQLException s){
                s.printStackTrace();
            }
        }

        return itemURLs;
    }

    public Item getItem(String item_name) {
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        Item item = null;
        String query = "SELECT * FROM `Shopaholic`.`items` WHERE `items`.`item_name` LIKE '" + item_name + "';";

        try {
            conn = connect();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
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
        } finally {
            try{
                if(rs != null){rs.close();}
                if(ps != null){ps.close();}
                if(conn != null){conn.close();}
            }catch (SQLException s){
                s.printStackTrace();
            }
        }

        return item;
    }

    public List<ItemHistory> getItemHistories(String item_name) {
        ArrayList<ItemHistory> itemHistories = new ArrayList<ItemHistory>();
        ResultSet rs = null;
        Connection conn = null;
        PreparedStatement ps = null;
        String query = "SELECT * FROM `Shopaholic`.`item_history` WHERE `item_history`.`item_name` LIKE '" + item_name + "';";

        try {
            conn = connect();
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            while (rs.next()) {
                itemHistories.add(new ItemHistory(item_name,
                        rs.getString("store_name"),
                        rs.getBigDecimal("item_price"),
                        rs.getDate("date")));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(rs != null){rs.close();}
                if(ps != null){ps.close();}
                if(conn != null){conn.close();}
            }catch (SQLException s){
                s.printStackTrace();
            }
        }

        return itemHistories;
    }

    public void insertItemHistories(List<ItemHistory> itemHistories) {
        for (ItemHistory itemHistory : itemHistories) {
            Debugger.log("Inserting " + itemHistory.getItem_name() + " to the DB...");
            Connection conn = null;
            PreparedStatement ps = null;

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
                conn = connect();
                ps = conn.prepareStatement(query);
                ps.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            } finally {
                try{
                    if(ps != null){ps.close();}
                    if(conn != null){conn.close();}
                }catch (SQLException s){
                    s.printStackTrace();
                }
            }
        }
    }

    public void updateItemInDB(Item item) {
        Connection conn = null;
        PreparedStatement ps = null;
        String query = "UPDATE `Shopaholic`.`items` " +
                "SET `item_msrp`='" + item.getItem_msrp() + "'," +
                " `item_min_price`='" + item.getItem_min_price() + "'," +
                " `item_max_price`='" + item.getItem_max_price() + "'," +
                " `item_latest_price`='" + item.getItem_msrp() + "'," +
                " `item_min_store_name`='" + item.getItem_min_store_name() + "' " +
                "WHERE `item_name` ='" + item.getItem_name() + "';";


        try {
            Debugger.log("UPDATING " + item.getItem_name());
            Debugger.log(query);
            conn = connect();
            ps = conn.prepareStatement(query);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            try{
                if(ps != null){ps.close();}
                if(conn != null){conn.close();}
            }catch (SQLException s){
                s.printStackTrace();
            }
        }
    }
}
