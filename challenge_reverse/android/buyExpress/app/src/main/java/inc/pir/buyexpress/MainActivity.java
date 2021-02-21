package inc.pir.buyexpress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.support.annotation.Keep;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.ConsoleMessage;
import android.webkit.JavascriptInterface;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

import dalvik.system.DexClassLoader;
/*
//https://developer.android.com/guide/webapps/webview 
// https://www.androidhive.info/2012/07/android-detect-internet-connection-status/
//reload url if changement de connexion et connected is true
 */

public class MainActivity extends AppCompatActivity {
    public WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        setContentView(R.layout.activity_main);
        webView= findViewById(R.id.webview);
        webView.setWebViewClient(new WebClientViewMine());
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getPath());
        webSettings.setJavaScriptEnabled(true);
        //getAssets()
        /*String apkPath = "file:///android_asset/app-debug.apk";//getFilesDir().getAbsolutePath() + "/app-debug.apk";
        Log.d("webClient",apkPath);
        //getCacheDir().getAbsolutePath()
        final DexClassLoader classLoader = new DexClassLoader(apkPath, "file:///android_asset/", "file:///android_asset/", this.getClass().getClassLoader());
        try {
            Class<?> c =classLoader.loadClass("com.example.dynaapp.dynaClasse");
            //c.getClass().getDeclaredConstructor(Context.class).newInstance(this);
            Object o= c.getClass().newInstance();
            Method m=o.getClass().getMethod("add",  double.class, double.class);
            Log.d("webClient","Alleluia !"+(String)m.invoke(1,1));
        } catch (Exception e) {
            Log.d("webClient",e.getMessage()+ Arrays.toString(e.getStackTrace()));
        }*/
        Object o=decryptText();
        // webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUserAgentString( webSettings.getUserAgentString()+ " ? id:"+
                getResources().getText(R.string.app_name).toString()+"/"+
                getResources().getText(R.string.appVersion).toString()+"/"+
                getResources().getText(R.string.token).toString());
        //Log.d("webClient",webSettings.getUserAgentString());
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        if(this.isInternetAvailable()) {
             WebAppInterface inter = new WebAppInterface(this,o);
            //inter.identification();
            //Log.d("webClient", "hash:"+inter.identification().toString());
                    /* funny (deep interaction inter js and android => security : only our page is handled)
        <input type="button" value="Say hello" onClick="showAndroidToast('Hello Android!')" />
            <script type="text/javascript">
                function showAndroidToast(toast) {
                    Android.showToast(toast);
                }
            </script>
         */
            // Set the Activity title by getting a string from the Resources object, because
//  this method requires a CharSequence rather than a resource ID
            webView.addJavascriptInterface(inter, "Android");
            String url="https://tryagain.dynamic-dns.net/old/0/7";
            webView.loadUrl(getResources().getText(R.string.URL).toString());
            //redirige vers le debugging android les console.log du web
            webView.setWebChromeClient(new WebChromeClient() {
                public boolean onConsoleMessage(ConsoleMessage cm) {
                    Log.d("webPage", cm.message() + " -- From line "
                            + cm.lineNumber() + " of "
                            + cm.sourceId());
                    return true;
                }
            });
        }else{
            webView.loadUrl("file:///android_asset/not_found.html");
        }
    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Check if the key event was the Back button and if there's history
        if ((keyCode == KeyEvent.KEYCODE_BACK) && webView.canGoBack()) {
            webView.goBack();
            return true;
        }
        // If it wasn't the Back key or there's no web page history, bubble up to the default
        // system behavior (probably exit the activity)
        return super.onKeyDown(keyCode, event);
    }
    public boolean isNetworkAvailable(Context context) {
        ConnectivityManager connectivityManager = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE));
        assert connectivityManager != null;
        return connectivityManager.getActiveNetworkInfo() != null && connectivityManager.getActiveNetworkInfo().isConnected();
    }
    public boolean isInternetAvailable() {
        Context ctx =webView.getContext();
        try {
            ///ok
            String ip = ctx.getResources().getString(R.string.ip);
           // int port = ctx.getResources().getInteger(R.integer.port);
            InetAddress ipAddr = InetAddress.getByName("www.google.com");
            boolean c=ipAddr.isReachable(2000);
            if(c) {
               /* DatagramSocket so=new DatagramSocket();
                so.connect(InetAddress.getByAddress(ip.getBytes()), port);
                boolean tc=so.isConnected();*/
                if(estEnLigne(ip,443,2000)) {
                    String txt = ctx.getResources().getString(R.string.message_successful_connexion);
                    Toast.makeText(ctx, txt, Toast.LENGTH_SHORT).show();
                    return true;
                }else {
                    String txt = ctx.getResources().getString(R.string.message_unsuccessful_connexion);
                    Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();
                }
            }else {
                if(isNetworkAvailable(ctx)) {
                    Log.d("webClient","sooo");
                    String txt = ctx.getResources().getString(R.string.error_message_no_internet_but_network);
                    Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();
                }else {
                    String txt = ctx.getResources().getString(R.string.error_message_no_internet);
                    Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();
                }
            }
        } catch (Exception e) {
            Log.d("webClient","baddd "+e.getMessage()+e.getCause()+e.getStackTrace()[1]);
            if(isNetworkAvailable(ctx)) {
                String txt = ctx.getResources().getString(R.string.error_message_no_internet_but_network);
                Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();
            }else {
                String txt = ctx.getResources().getString(R.string.error_message_no_internet);
                Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();
            }
        }
        return false;
    }
    private static boolean estEnLigne(String addr, int openPort, int timeOutMillis) {
        // Any Open port on other machine
        // openPort =  22 - ssh, 80 or 443 - webserver, 25 - mailserver etc.
        try {
            try (Socket soc = new Socket()) {
                soc.connect(new InetSocketAddress(addr, openPort), timeOutMillis);
            }
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    public Object decryptText() {
        String name = "test-sec.apk";
        File file = new File(getFilesDir(), name);
        if (copyAssetFile(name, file)) {
            String dexPath = file.getPath();
            String optimizedDirectory = file.getParent();
            ClassLoader parent = getClass().getClassLoader();
            DexClassLoader classLoader = new DexClassLoader(dexPath, optimizedDirectory, null, parent);
            try {
                Class<?> clazz = classLoader.loadClass("com.example.dynaapp.idInter");
               // Method method = clazz.getDeclaredMethod("decrypt", String.class);
               // String text = (String) method.invoke(clazz, textView.getText().toString());
                //c.getClass().getDeclaredConstructor(Context.class).newInstance(this);
                //Object o= clazz.getClass().newInstance();
                //balec just use the method not the object
                //Object o=clazz.newInstance();//clazz.getConstructor(Context.class,String.class).newInstance(this);
                Constructor co = clazz.getConstructor(String.class);
                Log.d("webClient", Arrays.toString(clazz.getConstructors()));
                //obfuscation
                Object o= co.newInstance("monSuperToken");
                Method m=o.getClass().getMethod("id");
                //passage du vrai paramètre
                Field f=clazz.getDeclaredField("tk");
                f.set(clazz,webView.getContext().getResources().getString(R.string.token));
                Log.d("webClient", (String)m.invoke(o));
                return o;
                /*fonctionnel 2
                Method m=clazz.getDeclaredMethod("id");//,String.class);
                Field f=clazz.getDeclaredField("tk");
                f.set(clazz,"lol");
                Log.d("webClient", (String) m.invoke(clazz));//,"lol"));
                */
                //fonctionnel
                /*
                Method m=clazz.getDeclaredMethod("add",double.class, double.class);
                Log.d("webClient","Alleluia ! numéro: "+m.invoke(clazz,1,1));
                */
            } catch (Exception e) {
                Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
            }
        }
        //https://www.programcreek.com/java-api-examples/?code=fooree%2FfooXposed%2FfooXposed-master%2FFoox_4th_02%2Fsrc%2Fmain%2Fjava%2Ffoo%2Free%2Fdemos%2Fx4th02%2FMainActivity.java
        return null;
    }

    private boolean copyAssetFile(String name, File file) {
        InputStream input = null;
        OutputStream output = null;
        File tmp=new File(getFilesDir(), "temp");
        try {
            input = getAssets().open(name);
            output = new BufferedOutputStream(new FileOutputStream(tmp));
           /* byte[] buf = new byte[40960000];
            //while si le fichier est plus grand
            while (true) {
                int len = input.read(buf);
                if (len == -1) {
                    break;
                }
                output.write(buf, 0, len);
            }*/
           output.write(EncryptorAesGcmPassword.readAllBytes(input));
           //dechiffrement
            FileInputStream fis = new FileInputStream(tmp);
            FileOutputStream fos = new FileOutputStream(file);
            byte[] decryptedText = EncryptorAesGcmPassword.decryptFile(tmp, "aBeautifulLaydOfEncryption");
            fos.write(decryptedText);
            /*SecretKeySpec sks = new SecretKeySpec(
                    ("e629ed98829a893899ddda67f582ede72e2a187dd1ddd5ada54f49cfe2c7486f").getBytes(),
                    "AES/CBC/NoPadding");
            Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
            AlgorithmParameters p=AlgorithmParameters.getInstance("AES/CBC/NoPadding");
            p.init("4f49cae2c748".getBytes());
            cipher.init(Cipher.DECRYPT_MODE, sks,p);
            CipherInputStream cis = new CipherInputStream(fis, cipher);
            int b;
            byte[] d = new byte[8];
            while ((b = cis.read(d)) != -1) {
                fos.write(d, 0, b);
            }
            cis.close();
            */

            fos.flush();
            fos.close();
            fis.close();
            return true;
        } catch (IOException e) {
            Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        } catch (NoSuchPaddingException e) {
            Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        } catch (InvalidKeyException e) {
            Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        } catch (InvalidAlgorithmParameterException e) {
            Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        } catch (Exception e) {
            Log.d("webClient","Decryption:"+e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        } finally {
            close(output);
            close(input);
        }
        return false;
    }

    private void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}
