package com;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class releaseMessage {
    public static class HttpURLConnectionDemo {
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
        public String[] GET_NEW_release() {
            HttpURLConnectionDemo http = new HttpURLConnectionDemo();
            String data = http.doGet("http://launchermeta.mojang.com/mc/game/version_manifest.json");
            String[] strArr = data.split("\\[");
            return strArr[1].split(",");
        }
        public String Get_player_UUID(String name){
            HttpURLConnectionDemo http = new HttpURLConnectionDemo();
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
            HttpURLConnectionDemo http = new HttpURLConnectionDemo();
            String Data =http.doGet("https://api.mojang.com/user/profiles/"+Player_UUID+"/names");
            return Data;
        }
    }
}
