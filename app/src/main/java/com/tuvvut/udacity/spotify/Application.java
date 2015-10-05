package com.tuvvut.udacity.spotify;

/**
 * Created by wu on 2015/06/14
 */
public class Application extends android.app.Application {
    public static boolean isOnePane = false;
    public static boolean isLargeLayout = false;
    public static String countryCode = "US";

    @Override
    public void onCreate() {
        super.onCreate();
    }
}
