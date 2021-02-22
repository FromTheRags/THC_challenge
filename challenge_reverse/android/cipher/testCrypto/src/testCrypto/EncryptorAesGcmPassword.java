package testCrypto;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Base64;

/**
 * AES-GCM inputs - 12 bytes IV, need the same IV and secret keys for encryption and decryption.
 * <p>
 * The output consist of iv, password's salt, encrypted content and auth tag in the following format:
 * output = byte[] {i i i s s s c c c c c c ...}
 * <p>
 * i = IV bytes
 * s = Salt bytes
 * c = content bytes (encrypted content)
 */
//https://mkyong.com/java/java-aes-encryption-and-decryption/
public class EncryptorAesGcmPassword {

    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

    private static final int TAG_LENGTH_BIT = 128; // must be one of {128, 120, 112, 104, 96}
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;
    private static final int MAX_SIZE = 40960000;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    // return a base64 encoded AES encrypted text
   // public static String encrypt(byte[] pText, String password) throws Exception {
    public static byte[] encrypt(byte[] pText, String password) throws Exception {
        // 16 bytes salt
        byte[] salt = CryptoUtils.getRandomNonce(SALT_LENGTH_BYTE);

        // GCM recommended 12 bytes iv?
        byte[] iv = CryptoUtils.getRandomNonce(IV_LENGTH_BYTE);

        // secret key from password
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        // ASE-GCM needs GCMParameterSpec
        cipher.init(Cipher.ENCRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] cipherText = cipher.doFinal(pText);

        // prefix IV and Salt to cipher text
        byte[] cipherTextWithIvSalt = ByteBuffer.allocate(iv.length + salt.length + cipherText.length)
                .put(iv)
                .put(salt)
                .put(cipherText)
                .array();

        // string representation, base64, send this string to other for decryption.
        //return Base64.getEncoder().encodeToString(cipherTextWithIvSalt);
     // we save the byte[] into a file.
        return cipherTextWithIvSalt;

    }
    public static void encryptFile(String fromFile, String toFile, String password) throws Exception {
    	Path.of(fromFile, "");
        // read a normal txt file
        byte[] fileContent = Files.readAllBytes(Path.of(fromFile, ""));//Paths.get(ClassLoader.getSystemResource(fromFile).toURI()));

        // encrypt with a password
        byte[] encryptedText = EncryptorAesGcmPassword.encrypt(fileContent, password);

        // save a file
        Path path = Paths.get(toFile);

        Files.write(path, encryptedText);

    }
    public static byte[] readAllBytes(InputStream inputStream) throws IOException {
        final int bufLen = 1024;
        byte[] buf = new byte[bufLen];
        int readLen;
        IOException exception = null;

        try {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

            while ((readLen = inputStream.read(buf, 0, bufLen)) != -1)
                outputStream.write(buf, 0, readLen);

            return outputStream.toByteArray();
        } catch (IOException e) {
            exception = e;
            throw e;
        } finally {
            if (exception == null) inputStream.close();
            else try {
                inputStream.close();
            } catch (IOException e) {
                exception.addSuppressed(e);
            }
        }
    }
    public static byte[] decryptFile(String fromEncryptedFile, String password) throws Exception {
    	FileInputStream fis = new FileInputStream(fromEncryptedFile);
    	byte[] fileContent =EncryptorAesGcmPassword.readAllBytes(fis);
    	fis.close();
        // read a file
        //byte[] fileContent =	Files.readAllBytes(Paths.get(fromEncryptedFile));
        //NON:
        /*byte[]  fileContent = new byte[MAX_SIZE];
        //while si le fichier est plus grand
        int nb=fis.read(fileContent);
        fis.close();*/
        	

        return EncryptorAesGcmPassword.decrypt(fileContent, password);

    }

    // we need the same password, salt and iv to decrypt it
    private static byte[] decrypt(byte[] cText, String password) throws Exception {

        //byte[] decode = Base64.getDecoder().decode(cText.getBytes(UTF_8));
    	//byte[] decode=cText;
        // get back the iv and salt from the cipher text
        ByteBuffer bb = ByteBuffer.wrap(cText);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);

        byte[] salt = new byte[SALT_LENGTH_BYTE];
        bb.get(salt);

        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        // get back the aes key from the same password and salt
        SecretKey aesKeyFromPassword = CryptoUtils.getAESKeyFromPassword(password.toCharArray(), salt);

        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);

        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));

        byte[] plainText = cipher.doFinal(cipherText);

        return plainText;//new String(plainText, UTF_8);

    }
    private static String serv(String url){
        try {
            HttpURLConnection con = (HttpURLConnection) ( new URL(
                    url+"?request=get_authentication_code")).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setConnectTimeout(2000);
            con.connect();
           return Integer.toString(con.getResponseCode());
        } catch (Exception e) {
            System.out.print("webClient"+e.getMessage()+e.getCause()+Arrays.toString(e.getStackTrace()) +e.toString());
        }
        return "fail";
    }
    private static void stressOut(String url) {
    	long startTime = System.currentTimeMillis();
    	
        try {
           	for(int i=0; i<100;i++) {
            HttpURLConnection con = (HttpURLConnection) ( new URL(
                    url)).openConnection();
            con.setRequestMethod("GET");
            con.setDoInput(true);
            con.setDoOutput(false);
            con.setConnectTimeout(2000);
            con.connect();
           String a=new String((readAllBytes(con.getInputStream())));
           System.out.println(a);
           if(a.contains("Access denied")) {
        	   System.out.print("Nickel ! requête numero: "+i);
           }
           	}
        	long stopTime = System.currentTimeMillis();
        	System.out.println("Elapsed time was " + (stopTime - startTime) + " miliseconds.");
        } catch (Exception e) {
            System.out.print("webClient"+e.getMessage()+e.getCause()+Arrays.toString(e.getStackTrace()) +e.toString());
        }
    }
    public static void main(String[] args) throws Exception {

       /* String OUTPUT_FORMAT = "%-30s:%s";
        String PASSWORD = "this is a password";
        String pText = "AES-GSM Password-Bases encryption!";

        String encryptedTextBase64 = EncryptorAesGcmPassword.encrypt(pText.getBytes(UTF_8), PASSWORD);

        System.out.println("\n------ AES GCM Password-based Encryption ------");
        System.out.println(String.format(OUTPUT_FORMAT, "Input (plain text)", pText));
        System.out.println(String.format(OUTPUT_FORMAT, "Encrypted (base64) ", encryptedTextBase64));

        System.out.println("\n------ AES GCM Password-based Decryption ------");
        System.out.println(String.format(OUTPUT_FORMAT, "Input (base64)", encryptedTextBase64));

        String decryptedText = EncryptorAesGcmPassword.decrypt(encryptedTextBase64, PASSWORD);
        System.out.println(String.format(OUTPUT_FORMAT, "Decrypted (plain text)", decryptedText));*/
    	
    	//test code getting http
    	//System.out.print("code: "+serv("https://tryagain.dynamic-dns.net/"));
    	//test ip ban
    	stressOut("https://tryagain.dynamic-dns.net/");
    	//encryption apk + check decryption 
    	/* String password = "aBeautifulLaydOfEncryption";
    	  String fromFile = "app-debug.apk"; // from resources folder
    	  String toFile = "C:\\Users\\FACHE Rémi\\Documents\\Programmation\\JAVA\\testCrypto\\test-sec.apk";

    	  // encrypt file
    	  EncryptorAesGcmPassword.encryptFile(fromFile, toFile, password);
    	  

    	  // decrypt file
    	  byte[] decryptedText = EncryptorAesGcmPassword.decryptFile(toFile, password);
          Path path = Paths.get("C:\\Users\\FACHE Rémi\\Documents\\Programmation\\JAVA\\testCrypto\\test-dec.apk");

          Files.write(path, decryptedText);
    	  /*String pText = new String(decryptedText, UTF_8);
    	  System.out.println(pText);*/
    }
}
