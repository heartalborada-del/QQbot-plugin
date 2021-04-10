package me.heartalborada.QQ_bot;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;

public class Internet {
    public static class Url {
        public String doGet(String URL){
            HttpURLConnection conn = null;
            InputStream is = null;
            BufferedReader br = null;
            StringBuilder result = new StringBuilder();
            try{
                //创建远程url连接对象
                URL url = new URL(URL);
                //通过远程url连接对象打开一个连接，强转成HTTPURLConnection类
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                //设置连接超时时间和读取超时时间
                conn.setConnectTimeout(15000);
                conn.setReadTimeout(60000);
                conn.addRequestProperty("User-Agent", "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
                conn.setRequestProperty("Accept", "application/json");
                //发送请求
                conn.connect();
                //通过conn取得输入流，并使用Reader读取
                if (200 == conn.getResponseCode()){
                    is = conn.getInputStream();
                    br = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String line;
                    while ((line = br.readLine()) != null){
                        result.append(line);
                        return line;
                    }
                }else{
                }
            }catch (MalformedURLException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }catch (Exception e){
                e.printStackTrace();
            }finally {
                try{
                    if(br != null){
                        br.close();
                    }
                    if(is != null){
                        is.close();
                    }
                }catch (IOException ioe){
                    ioe.printStackTrace();
                }
                conn.disconnect();
            }
            return result.toString();
        }
    }
    public static class get {
        public String[] GET_NEW_version() {
            Url http = new Url();
            String data = http.doGet("http://launchermeta.mojang.com/mc/game/version_manifest.json");
            String[] strArr = data.split("\\[");
            return strArr[1].split(",");
        }
        public String Get_player_UUID(String name){
            Url http = new Url();
            String data =http.doGet("https://api.mojang.com/users/profiles/minecraft/"+name);
            if (data.contains("error")||data.equals("")){
                return "Error";
            }
            else {
                String UUID = null;
                String[] strArr = data.split(",");
                strArr = strArr[1].replace("\"","").replace("}","").split(":");
                UUID = strArr[1];
                return UUID;
            }
        }
        public String Get_player_history_name(String Player_UUID){
            Url http = new Url();
            String Data =http.doGet("https://api.mojang.com/user/profiles/"+Player_UUID+"/names");
            return Data;
        }
        public String[] Get_MusicID(String name) {
            try {
                name= URLEncoder.encode(name,"UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            Url http = new Url();
            String Data = http.doGet("http://music.163.com/api/search/pc?s=" + name + "&type=1");
            Data=Data.replace("\"","").replace(":","");
            String[] info;
            info = Data.split(",");
            return new String[]{info[0].replace("{result{songs[{name",""),info[1].replace("id",""),info[3].replace("alias",""),info[25].replace("blurPicUrl","").replace("http","http:"),info[9].replace("artists[{name","")};
        }
        public String Get_Jump_Url(String id) {
            String location =null;
            try {
                String url = "https://music.163.com/song/media/outer/url?id=" + id + ".mp3";
                URL serverUrl = new URL(url);
                HttpURLConnection conn = (HttpURLConnection) serverUrl.openConnection();
                conn.setRequestMethod("GET");
                // 必须设置false，否则会自动redirect到Location的地址
                conn.setInstanceFollowRedirects(false);
                conn.addRequestProperty("Accept-Charset", "UTF-8;");
                conn.addRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
                conn.addRequestProperty("Referer", "https://music.163.com/song/media/outer/url");
                conn.connect();
                location = conn.getHeaderField("Location");
                serverUrl = new URL(location);
                conn = (HttpURLConnection) serverUrl.openConnection();
                conn.setRequestMethod("GET");

                conn.addRequestProperty("Accept-Charset", "UTF-8;");
                conn.addRequestProperty("User-Agent",
                        "Mozilla/5.0 (Windows; U; Windows NT 5.1; zh-CN; rv:1.9.2.8) Firefox/3.6.8");
                conn.addRequestProperty("Referer", "https://music.163.com/song/media/outer/url");
                conn.connect();
            } catch (Exception e) {
                e.printStackTrace();
            }
            return location;
        }
    }
}
