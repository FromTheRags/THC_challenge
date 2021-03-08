package com.example.dynaapp;


import android.util.Log;

import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

public class idInter {
    public static String tk;
    private String url;
    protected String c;
    public idInter(String a,String b){
        tk=a;
        url=b;
    }
    private String serv(){
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(
                    url+"?request=get_authentication_code")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setRequestProperty("Cookie", c+";");
            con.setConnectTimeout(2000);
            /*String k="";
            for(String s:con.getRequestProperties().keySet())
                k+=s+"->"+con.getRequestProperties().get(s)+"\n";
            Log.i("webClient",con.toString()+"////"+k+"////"+con.getRequestProperties().values());
            con.connect();
            Log.i("webClient",Integer.toString(con.getResponseCode()));*/
           return Integer.toString(con.getResponseCode());
        } catch (Exception e) {
            Log.d("webClient",e.getMessage()+e.getCause()+ Arrays.toString(e.getStackTrace()) +e.toString());
        }
        return "fail";
    }
    public String id() {
        String bonus=serv();
        MessageDigest digest;
        byte[] byteData;
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byteData = digest.digest(tk.getBytes(StandardCharsets.UTF_8));
            StringBuilder hash = new StringBuilder();
            for (int i = 0; i < byteData.length; i++){
                hash.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            hash.append(bonus);
            //Log.i("webClient","hash: "+hash.toString());
            return hash.toString();
        } catch (NoSuchAlgorithmException ignored) {
        }
        //Log.e("webClient","Erreur hash ! ");
      //  showToast("Version d'Android incompatible !");
        return "Hash erreur !";
    }
}
