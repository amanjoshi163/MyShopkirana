package com.myshopkirana.utils;

import android.os.Handler;
import android.widget.TextView;

import java.util.concurrent.TimeUnit;

public class CustomRunnable implements Runnable {
    public long millisUntilFinished = 0;
    public TextView holder;
    Handler handler;
    final String FORMAT = "%02d:%02d:%02d";
    public static String myFormat = "yyyy-MM-dd'T'hh:mm:ss";

    public CustomRunnable(Handler handler, TextView holder, long millisUntilFinished) {
        this.handler = handler;
        this.holder = holder;
        this.millisUntilFinished = millisUntilFinished;
    }

    @Override
    public void run() {
        if (millisUntilFinished > 0) {
            long mills = Math.abs(millisUntilFinished);
            holder.setText("" + String.format(FORMAT,
                    TimeUnit.MILLISECONDS.toHours(mills),
                    TimeUnit.MILLISECONDS.toMinutes(mills) - TimeUnit.HOURS.toMinutes(
                            TimeUnit.MILLISECONDS.toHours(mills)),
                    TimeUnit.MILLISECONDS.toSeconds(mills) - TimeUnit.MINUTES.toSeconds(
                            TimeUnit.MILLISECONDS.toMinutes(mills))));
            // holder.setText("  "+time);
            millisUntilFinished -= 1000;
            handler.postDelayed(this, 1000);
        } else {
            holder.setText("00:00:00");
            if (handler != null)
                handler.removeCallbacks(this);
            RxBus.getInstance().sendEvent(true);
        }
    }

}