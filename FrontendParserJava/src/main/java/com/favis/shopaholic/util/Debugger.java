package com.favis.shopaholic.util;

public class Debugger {
    private static boolean debuggerStatus = false;

    public static void disable(){
        debuggerStatus = false;
    }

    public static void enable(){
        debuggerStatus = true;
    }

    public static boolean isEnabled(){
        return debuggerStatus;
    }

    public static void log(String message){
        if(debuggerStatus == true){
            System.out.println(message);
        }
    }
}
