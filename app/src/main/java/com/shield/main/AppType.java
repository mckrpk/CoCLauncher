package com.shield.main;

import android.content.Context;

public class AppType {

    public static boolean isFree(Context context) {
        return context.getPackageName().contains("free");
    }

}
