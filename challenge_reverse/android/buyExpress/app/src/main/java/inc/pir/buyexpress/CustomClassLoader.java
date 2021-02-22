package inc.pir.buyexpress;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

//=> simplest Class Loader possible (not used here #pur java)
//https://www.baeldung.com/java-classloaders
//https://frugalisminds.com/custom-classloaders-java/
//https://www.javacodegeeks.com/2013/03/java-handmade-classloader-isolation.html
/* use:
        CustomClassLoader load= new CustomClassLoader();
        Class<?> clazz =load.findClass("test.frugalis.Frugalis");
        +exception handling
 */
public class CustomClassLoader extends ClassLoader {

    @Override
    public Class findClass(String name) throws ClassNotFoundException {
        byte[] b = loadClassFromFile(name);
        //resolveClass éventuellement
        return defineClass(name, b, 0, b.length);
    }
    //dans le dossier bin avec les .class
    private byte[] loadClassFromFile(String fileName)  {
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(
                fileName.replace('.', File.separatorChar) + ".class");
        byte[] buffer;
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        int nextValue = 0;
        try {
            while ( (nextValue = inputStream.read()) != -1 ) {
                byteStream.write(nextValue);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        buffer = byteStream.toByteArray();
        return buffer;
    }
}
