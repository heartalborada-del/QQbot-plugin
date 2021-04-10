package me.heartalborada.QQ_bot;

import net.mamoe.mirai.message.data.MessageChain;
import net.mamoe.mirai.message.data.MusicKind;
import net.mamoe.mirai.message.data.MusicShare;
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
    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent j){
        Bot.getApi().sendGroupMsg("1031419163","[系统] 玩家 "+String.valueOf(j.getPlayer()).replace("CraftPlayer{name=","").replace("}","")+" 加入了游戏");
    }
    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent q){
        Bot.getApi().sendGroupMsg("1031419163", "[系统] 玩家 "+String.valueOf(q.getPlayer()).replace("CraftPlayer{name=","").replace("}","")+" 退出了游戏");
    }
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
    @EventHandler
    public void onQQMessages(GroupMessageEvent g) {
        Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        if (g.getUserID().equals(Long.valueOf("2854196310"))){}
        else {
            if (g.getMsg().contains(".online")) {
                g.response("在线玩家为: " + String.valueOf(Bukkit.getOnlinePlayers()).replace("CraftPlayer{name=", "").replace("}", ""));
            } else if(g.getMsg().contains(".music ")){
                String msg=g.getMsg().replace(".music ","");
                String[] info=new Internet.get().Get_MusicID(msg);
                String URL = new Internet.get().Get_Jump_Url(info[1]);
                MessageChain chain=net.mamoe.mirai.message.data.MessageUtils.newChain(new MusicShare(
                        MusicKind.NeteaseCloudMusic,
                        info[0] +" - "+info[4],
                        info[2].replace("[","").replace("]",""),
                        "http://music.163.com/song/"+info[1],
                        info[3],
                        URL
                ));
                g.response(chain);
            } else if (g.getMsg().contains(".time")) {
                g.response("[当前时间] 当前时间为 " + hour + "时" + minute + "分" + second + "秒");
            } else if (g.getMsg().contains(".version") || g.getMsg().contains("最新MC")) {
                String[] release = new Internet.get().GET_NEW_version();
                String msg = release[0] + release[1] + release[3];
                msg = msg.replace(": ", ":").replace(" ", "; ").replace("\"", "").replace("{", "").replace(":", ": ");
                msg = msg.replace("id", "版本号").replace("type", "类型").replace("time", "更新时间");
                g.response("[最新mc版本] " + msg);
            } else if (g.getMsg().contains(".skin ")){
                String ID=g.getMsg().replace("skin ","");
                String UUID = new Internet.get().Get_player_UUID(ID);
                if (UUID=="Error"){
                    g.response("请检查是否输入正确的Minecraft ID");
                }
                else {
                    String History_Data=new Internet.get().Get_player_history_name(UUID);
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
            } else if (g.getMsg().contains(".help")){
                g.response(".online 获取服务器在线信息\n.music +歌曲名 网易云音乐歌曲链接\n.time 获取当前时间\n.version 获取我的世界最新版本\n.skin +ID 获取正版玩家皮肤以及曾用名");
            } else if (g.getMsg().contains(".test")){
                MessageChain chain=net.mamoe.mirai.message.data.MessageUtils.newChain(new MusicShare(
                        MusicKind.NeteaseCloudMusic,
                        "ファッション",
                        "rinahamu/Yunomi",
                        "http://music.163.com/song/1338728297/?userid=324076307",
                        "http://p2.music.126.net/y19E5SadGUmSR8SZxkrNtw==/109951163785855539.jpg",
                        "http://music.163.com/song/media/outer/url?id=1338728297&userid=324076307"
                ));
                g.response(chain);
            } else {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.sendMessage("[群号:" + g.getGroupID() + "][发送人:" + g.getUserID() + "][信息:" + g.getMsg() + "]");
                }
            }
        }
    }
}
