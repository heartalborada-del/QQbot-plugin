package com;

import java.util.Calendar;
import me.albert.amazingbot.bot.Bot;
import java.sql.Date;
import java.text.SimpleDateFormat;
import org.bukkit.plugin.java.JavaPlugin;

public class Time{
    public void timer() {
        while (true) {
            try{
                Thread.sleep(1000);
                Calendar c = Calendar.getInstance();
                int hour = c.get(Calendar.HOUR_OF_DAY);
                int minute = c.get(Calendar.MINUTE);
                int second = c.get(Calendar.SECOND);
                if (minute == 0 && second == 0) {
                    Bot.getApi().sendGroupMsg("1031419163", "[整点报时] 现在是北京时间 " + hour + " 点整");
                } else if (second == 0 && minute%30==0 && minute/30==1) {
                    Bot.getApi().sendGroupMsg("1031419163", "[报时] 现在是北京时间 " + hour + " 点 " + minute + " 分");
                }
            }
            catch (InterruptedException e){
                e.printStackTrace();
            }
        }
    }
    public static String timestampToDate(long time) {
        String dateTime;
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long timeLong = Long.valueOf(time);
        dateTime = simpleDateFormat.format(new Date(timeLong * 1000L));
        return dateTime;
    }
}
