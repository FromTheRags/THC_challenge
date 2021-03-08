package inc.pir.buyexpress;

import java.io.File;
import java.io.FileInputStream;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;

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
    private static final Charset UTF_8 = StandardCharsets.UTF_8;

    public static byte[] decryptFile(File fromEncryptedFile, String password) throws Exception {
        //rq:avoid custom loading file & always use readAllBytes to avoid strange issue.
        FileInputStream fis = new FileInputStream(fromEncryptedFile);
        byte[] fileContent =CryptoUtils.readAllBytes(fis);
        fis.close();
        return EncryptorAesGcmPassword.decrypt(fileContent, password);
    }
    // we need the same password, salt and iv to decrypt it
    static byte[] decrypt(byte[] cText, String password) throws Exception {

        //byte[] decode = Base64.getDecoder().decode(cText.getBytes(UTF_8));
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
}
