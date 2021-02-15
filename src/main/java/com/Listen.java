package com;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.EventHandler;

import me.albert.amazingbot.bot.Bot;
import me.albert.amazingbot.events.GroupMessageEvent;

import java.util.Calendar;

public class Listen implements Listener {
    public Listen(){
        System.out.println("[消息转发]开始监听");
    }
    //mc上、下线开始
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent j){
        Bot.getApi().sendGroupMsg("1031419163","[系统] 玩家 "+String.valueOf(j.getPlayer()).replace("CraftPlayer{name=","").replace("}","")+" 加入了游戏");
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent q){
        Bot.getApi().sendGroupMsg("1031419163", "[系统] 玩家 "+String.valueOf(q.getPlayer()).replace("CraftPlayer{name=","").replace("}","")+" 退出了游戏");
    }
    //mc上、下线结束
    //mc玩家发送消息转发到QQ群开始
    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent c){
        String msg = null;
        String prefix = null;
        String player_msg = c.getMessage();
        if (c.getMessage().contains("%Location%")||c.getMessage().contains("%area%")){
            String Get_Location = String.valueOf(c.getPlayer().getLocation());
            String[] Step=Get_Location.split("\\{");
            String[] Location_info=Step[2].replace("{",",").replace("}","").split(",");
            String Step3=Location_info[0]+Location_info[1]+Location_info[2]+Location_info[3];
            String[] Step4=Step3.split("=");
            String Location=" \\\n在 "+Step4[1].replace("x","")+" 中 X: "+Math.round(Float.parseFloat(Step4[2].replace("y","")))+" Y: "+Math.round(Float.parseFloat(Step4[3].replace("z","")))+" Z: "+ Math.round(Float.parseFloat(Step4[4]));
            msg=player_msg.replace("%location%",Location).replace(" \\","");
            msg=msg.replace("%area%",Location).replace(" \\","");
            c.setMessage(msg.replace("\n",""));
            prefix="[坐标] ";
        }
        else {
            msg=player_msg;
            prefix="[信息] ";
        }
        Bot.getApi().sendGroupMsg("1031419163","[玩家] "+prefix+String.valueOf(c.getPlayer()).replace("CraftPlayer{name=","").replace("}","")+": "+msg);
    }
    //mc玩家发送消息转发到QQ群结束
    //QQ群发送消息转发到mc结束
    @EventHandler
    public void onQQMessages(GroupMessageEvent g) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        if (g.getUserID().equals(Long.valueOf("2854196310"))){}
        else {
            if (g.getMsg().contains("服务器ip") || g.getMsg().contains("服务器IP")) {
                g.response("服务器IP是: mc.sdcraft.fun");
                g.response("服务器的备用IP是: d3.lankodata.com:10153");
            } else if (g.getMsg().contains("服务器版本")) {
                g.response("服务端版本为: " + Bukkit.getVersion());
            } else if (g.getMsg().contains("在线玩家")) {
                g.response("在线玩家为: " + String.valueOf(Bukkit.getOnlinePlayers()).replace("CraftPlayer{name=", "").replace("}", ""));
            } else if (g.getMsg().contains("当前时间")) {
                g.response("[当前时间] 当前时间为 " + hour + "时" + minute + "分" + second + "秒");
            } else if (g.getMsg().contains("最新mc") || g.getMsg().contains("最新MC")) {
                String[] release = new releaseMessage.get().GET_NEW_release();
                String msg = release[0] + release[1] + release[3];
                msg = msg.replace(": ", ":").replace(" ", "; ").replace("\"", "").replace("{", "").replace(":", ": ");
                msg = msg.replace("id", "版本号").replace("type", "类型").replace("time", "更新时间");
                g.response("[最新mc版本] " + msg);
            } else if (g.getMsg().contains("skin ")){
                String ID=g.getMsg().replace("skin ","");
                String UUID = new releaseMessage.get().Get_player_UUID(ID);
                if (UUID=="Error"){
                    g.response("请检查是否输入正确的Minecraft ID");
                }
                else {
                    String History_Data=new releaseMessage.get().Get_player_history_name(UUID);
                    History_Data=History_Data.replace("[","").replace("]","").replace("{","").replace("}","").replace("\"","").replace(":","").replace("name","").replace("changedToAt","");
                    String[] name=History_Data.split(",");
                    int name_long=name.length-1;
                    int i=0;
                    String history=null;
                    while (i<=name_long){
                        if (i==0){
                            history="Player name: "+name[0]+"\n";
                        }
                        else {
                            String time=new Time().timestampToDate(Long.parseLong(name[i+1])/1000);
                            history=history+"Player name: "+name[i]+"  Time: "+time+"\n";
                            i++;
                        }
                        i++;
                    }
                    String msg="Player name: "+ID+" \nUUID: "+UUID+"\nSkin url: https://www.mc-heads.net/skin/"+UUID+"\nHistory name: \n"+history;
                    g.response(msg);
                }
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage("[群号:" + g.getGroupID() + "][发送人:" + g.getUserID() + "][信息:" + g.getMsg() + "]");
                }
            }
        }
    }
}
