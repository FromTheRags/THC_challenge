package inc.pir.buyexpress;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WebAppInterface {
    Context mContext;
    /** Instantiate the interface and set the context */
    WebAppInterface(Context c) {
        mContext = c;
    }
    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public String identification() {
        MessageDigest digest = null;
        byte[] byteData = new byte[0];
        try {
            digest = MessageDigest.getInstance("SHA-256");
            byteData = digest.digest(mContext.getResources().getString(R.string.token).getBytes("UTF-8"));
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
        showToast("Version d'Android incompatible !");
        return "Hash erreur !";
    }
}
