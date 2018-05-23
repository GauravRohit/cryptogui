import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException; 
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;

public class MyCryptoUtils {
	
	private static final String ALG = "AES";


	public static void encrypt(String key, File inputFile, File outputFile) {

		doCrypto(Cipher.ENCRYPT_MODE, key, inputFile, outputFile);
	}


	public static void decrypt(String key, File inputFile, File outputFile) {

		doCrypto(Cipher.DECRYPT_MODE, key, inputFile, outputFile);
	}
	

	public static class CustomException extends RuntimeException {
		public CustomException(String s) {
			super(s);
		}
	}
	

	private static void doCrypto(int cipherMode, String key, File inputFile, File outputFile) {

		try {
			byte[] keyBytes = key.getBytes("UTF-8");
			keyBytes = Arrays.copyOf(keyBytes, 16);
			Key secretKey = new SecretKeySpec(keyBytes, ALG);
			Cipher cipher = Cipher.getInstance(ALG);
			cipher.init(cipherMode, secretKey);
				
			FileInputStream inputStream = new FileInputStream(inputFile);
			byte[] inputBytes = new byte[(int) inputFile.length()];
			inputStream.read(inputBytes);
				
			byte[] outputBytes = cipher.doFinal(inputBytes);
	
			FileOutputStream outputStream = new FileOutputStream(outputFile);
			outputStream.write(outputBytes);
			
			inputStream.close();
			outputStream.close();
		} catch (Exception e) {
			throw new CustomException(e.getClass().getSimpleName());
		}
	}
}
