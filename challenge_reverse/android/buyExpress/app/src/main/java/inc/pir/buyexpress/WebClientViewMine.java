package inc.pir.buyexpress;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.KeyEvent;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

public class WebClientViewMine extends WebViewClient {
    //avoid
    @Override
    public void onLoadResource( WebView webview, String url) {
        // exit the redirect loop if landed on homepage
        if(url.endsWith("login.html")) return;// redirect to home page if the page to load is error page
        if(url.endsWith("error.html")) {
            webview.stopLoading();
            webview.loadUrl("file:///android_asset/not_found.html");
        }
    }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Context context = view.getContext();
            //on va charger en webview
            //// This is my website, so do not override; let my WebView load the page
            if( Uri.parse(context.getResources().getText(R.string.URL).toString()).getHost().equals(Uri.parse(url).getHost()))
                return false;
            else {
                Log.d("webClient", "Open external browser !");
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                ((Activity) context).startActivity(intent);
                return true;
            }
        }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
    }
        ///TODO: avoid getting errors
    //@RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void onReceivedError(final WebView webview, WebResourceRequest request,
                                WebResourceError error) {

        webview.loadUrl("file:///android_asset/not_found.html");
        Log.d("webClient","Erreur loading page "+ error.getDescription()+ " /"+error.getErrorCode());
        /*Context ctx=webview.getContext();
        String txt=ctx.getResources().getString(R.string.error_message_no_internet);
        Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();//long=time to show*/
        // dialog_Show(webview, "Error Occur, Do you want to Reload?", true);

        //si on veut la page d'erreur par défaut :
        //super.onReceivedError(webview, request, error);
    }


}
