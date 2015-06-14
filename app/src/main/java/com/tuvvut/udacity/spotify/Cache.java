package com.tuvvut.udacity.spotify;

import java.util.HashMap;

/**
 * Created by wu on 2015/06/14
 */
public class Cache {
    private static HashMap<TYPE, Object> data = new HashMap<>();

    public static void put(TYPE key, Object value) {
        data.put(key, value);
    }

    public static Object get(TYPE key) {
        return data.get(key);
    }

    public static void clear(){
        data.clear();
    }

    public enum TYPE {ARTISTS, TRACKS}
}
