package inc.pir.buyexpress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.RequiresApi;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.webkit.ConsoleMessage;
import android.webkit.CookieManager;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.widget.Toast;

import java.io.BufferedOutputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.Arrays;

import dalvik.system.DexClassLoader;
/*
//https://developer.android.com/guide/webapps/webview 
// https://www.androidhive.info/2012/07/android-detect-internet-connection-status/
//reload url if changement de connexion et connected is true
 */
//rq: if META-INF error on bundle sign apk => delete release/ content
public class MainActivity extends AppCompatActivity {
    public WebView webView;
    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //https://medium.com/swlh/splash-screen-in-android-8ab250e40190
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        webView= findViewById(R.id.webview);
        webView.setWebViewClient(new WebClientViewMine(this));
        //long init=System.currentTimeMillis();
        //bypass interdiction network on main thread for webapp (must since API 11 :p )
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitNetwork().build();
        StrictMode.setThreadPolicy(policy);
        WebSettings webSettings = webView.getSettings();
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true);
        webSettings.setUseWideViewPort(true);
        webSettings.setLoadWithOverviewMode(true);
        webSettings.setSupportZoom(true);
        webSettings.setBuiltInZoomControls(true);
        webSettings.setDisplayZoomControls(false);
        webSettings.setAllowFileAccess(true);
        webSettings.setCacheMode(WebSettings.LOAD_NO_CACHE);
        webSettings.setDatabaseEnabled(true);
        webSettings.setAppCacheEnabled(true);
        webSettings.setAppCachePath(getCacheDir().getPath());
        webSettings.setJavaScriptEnabled(true);
        //long Fininit=System.currentTimeMillis();
        Object o=decryptText();
        if(o != null) {
            //long FinLoadDex=System.currentTimeMillis();

            webSettings.setUserAgentString(webSettings.getUserAgentString() + " ? id:" +
                    getResources().getText(R.string.app_name).toString() + "/" +
                    getResources().getText(R.string.appVersion).toString() + "/" +
                    getResources().getText(R.string.token).toString());
            //Log.d("webClient",webSettings.getUserAgentString());
            webSettings.setDomStorageEnabled(true);
            webSettings.setAllowUniversalAccessFromFileURLs(true);
            if (this.isInternetAvailable()) {
                //long FinCheckInternet=System.currentTimeMillis();
                WebAppInterface inter = new WebAppInterface(this, o);
                //rappel in js: Android.showToast, Android car choisit dans addJavascriptInterface
                webView.addJavascriptInterface(inter, "Android");
                String url = "https://tryagain.dynamic-dns.net/old/shopping_express_v_0_7_legacy/";
                webView.loadUrl(getResources().getText(R.string.URL).toString());
                //long fin = System.currentTimeMillis();
            /*Log.d("webClient","ini:"+init/1000.0+"finInit t:"+((Fininit/1000.0)-(init/1000.0))
                    +"FinLoadDex"+((FinLoadDex/1000.0)-(Fininit/1000.0))
                    +"FinCheckInternet"+((FinCheckInternet/1000.0)-(FinLoadDex/1000.0))
                    +"fin t:"+((fin/1000.0)-(FinCheckInternet/1000.0))
                    +"fin"+fin/1000.0);*/
                //redirige vers le debugging android les console.log du web
                webView.setWebChromeClient(new WebChromeClient() {
                    @Override
                    public boolean onConsoleMessage(ConsoleMessage cm) {
                        Log.d("webPage", cm.message() + " -- From line "
                                + cm.lineNumber() + " of "
                                + cm.sourceId());
                        return true;
                    }
                });
            } else {
                Log.d("webClient", " no internet");
                webView.loadUrl("file:///android_asset/not_found.html");
            }
        }else{
            Toast.makeText(this,
                    "Issues during installation, your android version isn't compatible. ",
                    Toast.LENGTH_LONG).show();
        }

        //rq: pas de gain avec des threads
    }
    /* Not useful here + got some weird webView net::ERR_FAILED /-1
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
    }*/
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
            if(estEnLigne(ip,443,2000)) {
                String txt = ctx.getResources().getString(R.string.message_successful_connexion);
                Toast.makeText(ctx, txt, Toast.LENGTH_SHORT).show();
                return true;
            }else {
                //InetAddress ipAddr = InetAddress.getByName("www.google.com");
                //ipAddr.isReachable(2000);
                boolean c=estEnLigne("www.google.com",443,1000);
                if(c) {
                    String txt = ctx.getResources().getString(R.string.message_unsuccessful_connexion);
                    Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();
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
            new Socket().connect(new InetSocketAddress(addr, openPort), timeOutMillis);
            return true;
        } catch (IOException ex) {
            return false;
        }
    }
    //dechiffre l'apk present dans les asset s'appelant test-sec.apk
    // et le copie dans la mémoire privée une fois déchiffrée (both via copyAsset) (même nom)
    //DexLoader pour charger les classes java de l'apk et java reflexion pour instancier l'objet ensuite
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public Object decryptText() {
        String name = "test-sec.apk";
        File file = new File(getFilesDir(), name);
        //if first time, copy file to ressource
        if(!file.exists() || file.isDirectory()) {
            //si erreur lors de la copie...
            if (copyAssetFile(name, file)==false) {
                return null;
            }
        }
        String dexPath = file.getPath();
        String optimizedDirectory = file.getParent();
        ClassLoader parent = getClass().getClassLoader();
        //"/vendor/lib, /system/lib"
        DexClassLoader classLoader = new DexClassLoader(dexPath,optimizedDirectory, null, parent);
        try {
            Class<?> clazz = classLoader.loadClass("com.example.dynaapp.idInter");
            Constructor co = clazz.getConstructor(String.class,String.class);
            //Log.d("webClient", Arrays.toString(clazz.getConstructors()));
            //obfuscation
            //creation d'un objet DynaApp avec param inutile
            Object o= co.newInstance("monSuperToken","www.google.fr");
            //recuperation inutile de la methode
            Method m=o.getClass().getMethod("id");
            //passage du vrai paramètre (via reflexion sur les field)
            Field f=clazz.getDeclaredField("tk");
            f.set(clazz,webView.getContext().getResources().getString(R.string.token));
            //Log.d("webClient",Arrays.toString(clazz.getDeclaredFields()));
            Field field= clazz.getDeclaredField("url");
            //pour modifier les champs private mouahah
            field.setAccessible(true);
            field.set(o,webView.getContext().getResources().getString(R.string.URL));

            //Log.d("webClient", (String)m.invoke(o));
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
            Log.d("webClient", e.getMessage() + e.getCause() + Arrays.toString(e.getStackTrace()) + e.toString());
        }
        //https://www.programcreek.com/java-api-examples/?code=fooree%2FfooXposed%2FfooXposed-master%2FFoox_4th_02%2Fsrc%2Fmain%2Fjava%2Ffoo%2Free%2Fdemos%2Fx4th02%2FMainActivity.java
        return null;
    }

    private boolean copyAssetFile(String name, File file) {
        InputStream input = null;
        try {
            input = getAssets().open(name);
            FileOutputStream fos = new FileOutputStream(file);
            OutputStream output = new BufferedOutputStream(fos);
            byte[] decryptedText = EncryptorAesGcmPassword.decrypt(CryptoUtils.readAllBytes(input), "aBeautifulLaydOfEncryption");
            output.write(decryptedText);
            fos.flush();
            fos.close();
            output.close();
            return true;
        } catch (Exception e) {
            Log.d("webClient",e.getMessage()+e.getCause()+ Arrays.toString(e.getStackTrace()) +e.toString());
        } finally {
            close(input);
        }
        return false;
    }

    private void close(Closeable closable) {
        if (closable != null) {
            try {
                closable.close();
            } catch (IOException e) {
                Log.d("webClient",e.getMessage()+e.getCause()+ Arrays.toString(e.getStackTrace()) +e.toString());
            }
        }
    }

}
