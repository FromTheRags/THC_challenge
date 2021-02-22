import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.ArrayList;
import java.util.List;
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

public class EncryptorAesGcmPassword {

    private static final String ENCRYPT_ALGO = "AES/GCM/NoPadding";

    private static final int TAG_LENGTH_BIT = 128;
    private static final int IV_LENGTH_BYTE = 12;
    private static final int SALT_LENGTH_BYTE = 16;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

	public static SecretKey getAESKeyFromPassword(char[] password, byte[] salt)
	            throws NoSuchAlgorithmException, InvalidKeySpecException {

	        SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
	        // iterationCount = 65536
	        // keyLength = 256
	        KeySpec spec = new PBEKeySpec(password, salt, 65536, 256);
	        SecretKey secret = new SecretKeySpec(factory.generateSecret(spec).getEncoded(), "AES");
	        return secret;

    }

    public static byte[] decryptFile(String fromEncryptedFile, String password) throws Exception {

        byte[] fileContent =	Files.readAllBytes(Paths.get(fromEncryptedFile));
        return EncryptorAesGcmPassword.decrypt(fileContent, password);

    }

    // we need the same password, salt and iv to decrypt it
    private static byte[] decrypt(byte[] cText, String password) throws Exception {
        ByteBuffer bb = ByteBuffer.wrap(cText);

        byte[] iv = new byte[IV_LENGTH_BYTE];
        bb.get(iv);

        byte[] salt = new byte[SALT_LENGTH_BYTE];
        bb.get(salt);

        byte[] cipherText = new byte[bb.remaining()];
        bb.get(cipherText);

        // get back the aes key from the same password and salt
        SecretKey aesKeyFromPassword = EncryptorAesGcmPassword.getAESKeyFromPassword(password.toCharArray(), salt);
        Cipher cipher = Cipher.getInstance(ENCRYPT_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, aesKeyFromPassword, new GCMParameterSpec(TAG_LENGTH_BIT, iv));
        return cipher.doFinal(cipherText);

    }

    public static void main(String[] args) throws Exception {

		  String password = "aBeautifulLaydOfEncryption";
    	  String fromFile = "test-sec.apk";
    	  // decrypt file
    	  byte[] decrypted = EncryptorAesGcmPassword.decryptFile(fromFile, password);
          Path path = Path.of("dechiphered.apk", "");
          Files.write(path, decrypted);
    }
}
