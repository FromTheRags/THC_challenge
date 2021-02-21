package com.example.dynaapp;


import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class idInter {
    public static String tk;
    public idInter(String a){
        tk=a;
    }
    public static String id() {
        MessageDigest digest = null;
        byte[] byteData = new byte[0];
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byteData = digest.digest(tk.getBytes("UTF-8"));
            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < byteData.length; i++){
                hash.append(Integer.toString((byteData[i] & 0xff) + 0x100, 16).substring(1));
            }
            //Log.d("webClient","hash: "+hash.toString());
            return hash.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        //Log.e("webClient","Erreur hash ! ");
      //  showToast("Version d'Android incompatible !");
        return "Hash erreur !";
    }
}
