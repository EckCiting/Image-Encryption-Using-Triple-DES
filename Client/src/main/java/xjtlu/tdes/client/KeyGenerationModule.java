package xjtlu.tdes.client;

import org.apache.commons.codec.binary.Hex;
import xjtlu.tdes.client.Utilities.ImageUtility;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
public class KeyGenerationModule {

    // NOTE: Need to consider the security of this design in transfer keys across classes in the program
    static byte[] getKey(String password, String salt1, String salt2){
        return makeKey(password, salt1, salt2);
    }

    private static byte[] slowHash(final char[] password, final byte[] salt, final int iterations, final int keyLength ) {
        try {
            SecretKeyFactory skf = SecretKeyFactory.getInstance( "PBKDF2WithHmacSHA512" );
            PBEKeySpec spec = new PBEKeySpec( password, salt, iterations, keyLength );
            SecretKey key = skf.generateSecret( spec );
            return key.getEncoded();
        } catch ( NoSuchAlgorithmException | InvalidKeySpecException e ) {
            throw new RuntimeException( e );
        }
    }

    private static byte[] makeKey(String password, String salt1, String salt2){
        int iterations = 100000;
        int keyLength = 192;
        char[] passwordChars = password.toCharArray();
        byte[] salt1Bytes = salt1.getBytes();
        byte[] salt2Bytes = salt2.getBytes();

        /*
         *  salt1, salt2 = salt.split()
         *  hashvalue1 = SlowHash(password + salt1)
         *  hashvalue2 = SlowHash(hashvalue1 + salt2)
         */
        byte[] hashValue1 = slowHash(passwordChars, salt1Bytes, iterations, keyLength);
        String hashValueString1 = Hex.encodeHexString(hashValue1);
        char[] hashValueChars1 = hashValueString1.toCharArray();

        byte[] hashValue2 = slowHash(hashValueChars1, salt2Bytes, iterations, keyLength);

        return hashValue2;
    }

    public static void main(String[] args) {
        try {
            String password = "meiyoumima";
            String salt1 = "flaasdfa23DJLJAab";
            String salt2 = "56DFS5678fasd5678";
            byte[] keyBytes = KeyGenerationModule.getKey(password,salt1,salt2);

            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);

            final Cipher encryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, iv);
            final Cipher decryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, iv);

            ImageUtility.ImageEncryption("XJTLU-logo.png",encryptCipher);
            ImageUtility.ImageDecryption("XJTLU-logo.png",decryptCipher);

        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
