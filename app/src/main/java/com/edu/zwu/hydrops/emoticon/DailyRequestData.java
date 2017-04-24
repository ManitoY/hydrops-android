package com.edu.zwu.hydrops.emoticon;

import android.util.Pair;

import com.edu.zwu.hydrops.MyApplication;

import java.util.ArrayList;

/**
 * Created by shengwei.yi on 17/4/1.
 */

public class DailyRequestData {

    public ArrayList<Pair<String, String>> getHydropsEmoji() {
        ArrayList<Pair<String, String>> list = new ArrayList<>();
        String key = MyApplication.getEmojiKey();
        String value = MyApplication.getEmojiValue();
        String keys[] = key.split(",");
        String values[] = value.split(",");
        for (int i = 0; i < keys.length; i++) {
            Pair<String, String> pair = new Pair<>(keys[i], values[i]);
            list.add(pair);
        }
        return list;
    }

    public static void addHydropsEmoji(ArrayList<Pair<String, String>> list) {
        StringBuilder keySb = new StringBuilder();
        StringBuilder valueSb = new StringBuilder();
        for (int i = 0; i < list.size(); i++) {
            Pair<String, String> pair = list.get(i);
            if (i > 0) {
                keySb.append(",");
                valueSb.append(",");
            }
            keySb.append(pair.first);
            valueSb.append(pair.second);
        }
        MyApplication.addEmojiKey(keySb.toString());
        MyApplication.addEmojiValue(valueSb.toString());
    }
}
