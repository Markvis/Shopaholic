package com.favis.shopaholic;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;

import java.net.MalformedURLException;
import java.net.URL;

public class Runner {

    public static void main(String... args){
        System.out.println("hi im mark");

        DesiredCapabilities capability = DesiredCapabilities.chrome();


        WebDriver webDriver = null;

        try {
            webDriver = new RemoteWebDriver(new URL("http://192.168.3.240:4444/wd/hub"), capability);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }

        webDriver.get("https://www.google.com");
        System.out.println(webDriver.findElement(By.xpath("//a[text()='Store']")).getText());
    }
}