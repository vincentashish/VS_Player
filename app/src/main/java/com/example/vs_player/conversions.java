package com.example.vs_player;

import android.annotation.SuppressLint;

public class conversions {
    @SuppressLint("DefaultLocale")
    public static String timeConversion(long value) {
        String videoTime;
        int duration = (int) value;
        int hrs = (duration / 3600000);
        int mins = (duration / 60000) % 600000;
        int scs = duration % 60000 / 1000;
        if (hrs > 0) {
            videoTime = String.format("%02d:%02d:%02d", hrs, mins, scs);
        } else {
            videoTime = String.format("%02d:%02d", mins, scs);
        }
        return videoTime;
    }

    @SuppressLint("DefaultLocale")
    public static String sizeConversion(long value) {
        String fileSize;
        double bytes = (double) value;
        double kb = bytes/1024;
        double mb = kb/1024;
        double gb = mb/1024;

        if(gb > 1)
            fileSize = String.format("%.2f",gb) + " GB";
        else if(mb > 1)
            fileSize = String.format("%.2f",mb) + " MB";
        else
            fileSize = String.format("%.2f",kb) + " KB";

        return fileSize;
    }
}
