package inc.pir.buyexpress;

import android.content.Context;
import android.util.Log;
import android.webkit.JavascriptInterface;
import android.widget.Toast;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class WebAppInterface {
    Context mContext;
    Object mo;
    /** Instantiate the interface and set the context */
    WebAppInterface(Context c,Object o) {
        mContext = c;
        mo=o;
    }
    /** Show a toast from the web page */
    @JavascriptInterface
    public void showToast(String toast) {
        Toast.makeText(mContext, toast, Toast.LENGTH_SHORT).show();
    }
    @JavascriptInterface
    public String identification()  {
        try {
           return (String)mo.getClass().getMethod("id").invoke(mo);
        } catch (IllegalAccessException e) {
            Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        } catch (InvocationTargetException e) {
            Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        } catch (NoSuchMethodException e) {
            Log.d("webClient",e.getMessage()+e.getCause()+e.getStackTrace()+e.toString());
        }
        return "fail!";
    }
}
