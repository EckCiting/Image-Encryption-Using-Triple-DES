package xjtlu.tdes.client;

import org.apache.commons.codec.binary.Hex;


import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;
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

}
