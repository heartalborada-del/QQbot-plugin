package me.heartalborada.QQ_bot;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    Thread t;
    public void onDisable(){
        super.onDisable();
        t.stop();
        System.out.println("[消息转发]插件已停用");
    }
    public void onEnable() {
        super.onEnable();
        System.out.println("[消息转发]插件已启用");
        this.getServer().getPluginManager().registerEvents(new Listen(),this);
        t = new Thread(){
            public void run(){
                new me.heartalborada.QQ_bot.Time().timer();
            }
        };
    }
}