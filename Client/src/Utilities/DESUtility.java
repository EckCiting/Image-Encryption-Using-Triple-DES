package Utilities;

import javax.crypto.Cipher;


public class DESUtility {

    static byte[] encrypt(byte[] originImage,Cipher encryptCipher) {
        try {
            return encryptCipher.doFinal(originImage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


    static byte[] decrypt(byte[] encryptImage,Cipher decryptCipher) {
        try {
            return decryptCipher.doFinal(encryptImage);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}

