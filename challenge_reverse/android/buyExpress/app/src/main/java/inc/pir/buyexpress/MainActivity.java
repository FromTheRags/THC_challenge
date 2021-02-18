package inc.pir.buyexpress;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.ConnectivityManager;
import android.os.Bundle;
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

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
/*
//https://developer.android.com/guide/webapps/webview 
// https://www.androidhive.info/2012/07/android-detect-internet-connection-status/
//reload url if changement de connexion et connected is true
 */

public class MainActivity extends AppCompatActivity {
    public WebView webView;
    @SuppressLint("SetJavaScriptEnabled")
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
       // webSettings.setPluginState(WebSettings.PluginState.ON);
        webSettings.setUserAgentString( webSettings.getUserAgentString()+ " ? id:"+
                getResources().getText(R.string.app_name).toString()+"/"+
                getResources().getText(R.string.appVersion).toString()+"/"+
                getResources().getText(R.string.token).toString());
        //Log.d("webClient",webSettings.getUserAgentString());
        webSettings.setDomStorageEnabled(true);
        webSettings.setAllowUniversalAccessFromFileURLs(true);
        if(this.isInternetAvailable()) {
            WebAppInterface inter = new WebAppInterface(this);
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
            String url="https://tryagain2.dynamic-dns.net";
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

}
