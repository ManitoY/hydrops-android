package com.edu.zwu.hydrops.util;

import java.io.Closeable;
import java.io.IOException;

/**
 * Created by shengwei.yi on 17/3/31.
 */

public class IOUtil {
    public static void close(Closeable closeable){
        try {
            closeable.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
