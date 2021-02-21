package inc.pir.buyexpress;

import android.content.Context;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

//https://www.baeldung.com/java-classloaders
/*
 CustomClassLoader loader = new CustomClassLoader(this);
            // This class should be in your application class path
            Class<?> c = null;
            try {
                c = loader.findClass("inc.pir.buyexpress.WebAppInterface");
                Object inter = c.getClass().getDeclaredConstructor(Context.class).newInstance(this);//c.newInstance();
            //<=>

 */
public class CustomClassLoader extends ClassLoader {

    Context mContext;
    /** Instantiate the interface and set the context */
    CustomClassLoader(Context c) {
        mContext = c;
    }
    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        byte[] b = new byte[0];
        try {
            b = loadClassFromFile(name);
        } catch (IOException e) {
            Log.d("webClient", Arrays.toString(e.getStackTrace()));
        }
        return defineClass("WebAppInterface", b, 0, b.length);
    }

    private byte[] loadClassFromFile(String fileName) throws IOException {
        InputStream inputStream = /*getClass().getClassLoader().getResourceAsStream(
                fileName.replace('.', File.separatorChar) + ".class");*/
        mContext.getAssets().open("not_found.html");
         //       new FileInputStream("file:///android_asset/c.class");

        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            Log.d("webClient", Arrays.toString(e.getStackTrace()));
        }
        buffer = byteStream.toByteArray();
        Log.d("webClient", Arrays.toString(buffer));
        return buffer;
    }
}
