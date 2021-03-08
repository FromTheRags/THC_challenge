package testCrypto;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.AlgorithmParameters;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class test {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		System.out.print("Hello wolrd !");

        FileOutputStream fos;
        FileInputStream fis;
		try {
			fis = new FileInputStream("test.apk");
			fos = new FileOutputStream("test-sec.apk");


        SecretKeySpec sks = new SecretKeySpec(
                ("e629ed98829a893899ddda67f582ede72e2a187dd1ddd5ada54f49cfe2c7486f").getBytes(),
                "AES/CBC/NoPadding");
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        AlgorithmParameters p=AlgorithmParameters.getInstance("AES/CBC/NoPadding");
        p.init("4f49cae2c748".getBytes());
        cipher.init(Cipher.DECRYPT_MODE, sks,p);
        CipherInputStream cis = new CipherInputStream(fis, cipher);
        int b;
        byte[] d = new byte[8];
        while ((b = cis.read(d)) != -1) {
            fos.write(d, 0, b);
        }
        fos.flush();
        fos.close();
        cis.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchPaddingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidKeyException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvalidAlgorithmParameterException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
