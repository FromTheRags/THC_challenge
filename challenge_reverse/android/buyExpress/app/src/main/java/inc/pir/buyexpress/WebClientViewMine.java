package inc.pir.buyexpress;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.net.http.SslError;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebResourceError;
import android.webkit.WebResourceRequest;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.util.Objects;

public class WebClientViewMine extends WebViewClient {
    MainActivity ctx;
    boolean first;
    WebClientViewMine(MainActivity app){
        ctx=app;
        first=true;
    }
    @Override
    public void onLoadResource( WebView webview, String url) {
        // exit the redirect loop if landed on homepage
        // redirect to home page if the page to load is error page
        if (!url.endsWith("login.html")) {
            if (url.endsWith("error.html")) {
                webview.stopLoading();
                Log.d("webClient", " loadRessourceNotFound");
                webview.loadUrl("file:///android_asset/not_found.html");
            }
        }
    }
    /*@Override
    public void onPageFinished(WebView view, String url){
        String cookies = CookieManager.getInstance().getCookie(url);
        Log.d("webClient", "All the cookies of"+url+" in a string:" + cookies);
    }*/
    /*@Override
    public void onPageStarted(WebView view, String url, Bitmap favicon) {
        ctx.findViewById(R.id.textView1).setVisibility(View.VISIBLE);
        ctx.findViewById(R.id.progressBar1).setVisibility(View.VISIBLE);
        ctx.findViewById(R.id.imageView1).setVisibility(View.VISIBLE);
        //show webview
        ctx.findViewById(R.id.webview).setVisibility(View.GONE);
    }*/

    @Override
    public void onPageFinished(WebView view, String url) {
        /*WebView webview = (WebView) view.findViewById(R.id.webview);
        ImageView logo = (ImageView) view.findViewById(R.id.imageView1);
        ProgressBar bar = (ProgressBar)view.findViewById(R.id.progressBar1);
        TextView version = (TextView) view.findViewById(R.id.textView1);
        view.setVisibility(0);
        logo.setVisibility(10);
        bar.setVisibility(10);
        version.setVisibility(10);*/
        //hide loading image
        if(first) {
            ctx.findViewById(R.id.textView1).setVisibility(View.GONE);
            ctx.findViewById(R.id.progressBar1).setVisibility(View.GONE);
            ctx.findViewById(R.id.imageView1).setVisibility(View.GONE);
            //show webview
            ctx.findViewById(R.id.webview).setVisibility(View.VISIBLE);
        }
        super.onPageFinished(view,url);
    }
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            Context context = view.getContext();
            //on va charger en webview
            //// This is my website, so do not override; let my WebView load the page
           //  better than getHost().equals(..) as getHost can be null
            if(Objects.equals(Uri.parse(context.getResources().getString(R.string.URL)).getHost(), Uri.parse(url).getHost()))
                return false;
            else {
                Log.d("webClient", "Open external browser !");
                // Otherwise, the link is not for a page on my site, so launch another Activity that handles URLs
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                context.startActivity(intent);
                return true;
            }
        }

    @Override
    public void onReceivedSslError(WebView view, SslErrorHandler handler,
                                   SslError error) {
    }

    @Override
    public void onReceivedError(final WebView webview, WebResourceRequest request,
                                WebResourceError error) {
        Log.d("webClient", " onreceivedError");
        webview.loadUrl("file:///android_asset/not_found.html");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Log.d("webClient","Erreur loading page "+ error.getDescription()+ " /"+error.getErrorCode());
        }else{
            Log.d("webClient","Erreur loading page "+ error);
        }
        /*Context ctx=webview.getContext();
        String txt=ctx.getResources().getString(R.string.error_message_no_internet);
        Toast.makeText(ctx, txt, Toast.LENGTH_LONG).show();//long=time to show*/
        // dialog_Show(webview, "Error Occur, Do you want to Reload?", true);

        //si on veut la page d'erreur par défaut :
        //super.onReceivedError(webview, request, error);
    }


}
