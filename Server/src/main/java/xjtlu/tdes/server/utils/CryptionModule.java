package xjtlu.tdes.server.utils;


import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


public class CryptionModule {
    public static MultipartFile TDESEncryption(MultipartFile image, String password, String salts, byte[] b) {
        MultipartFile encryptedImage = null;
        String salt1 = salts.substring(0, 16);
        String salt2 = salts.substring(16, 32);
        byte[] keyBytes = KeyGenerationModule.getKey(password, salt1, salt2);

        try {
            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(b); // [0,0,0,0,0,0,0,0]
            final Cipher encryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, iv);
            byte[] encryptedImageByte = encryptCipher.doFinal(image.getBytes());
            encryptedImage = new MockMultipartFile(image.getName(),
                    encryptedImageByte);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedImage;
    }

    public static MultipartFile TDESDecryption(MultipartFile image, String password, String salts, byte[] b) throws BadPaddingException {
        MultipartFile decryptedImage = null;
        String salt1 = salts.substring(0, 16);
        String salt2 = salts.substring(16, 32);

        byte[] keyBytes = KeyGenerationModule.getKey(password, salt1, salt2);
        try {
            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(b); // [0,0,0,0,0,0,0,0]
            final Cipher decryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, iv);
            byte[] decryptedImageByte = decryptCipher.doFinal(image.getBytes());
            decryptedImage = new MockMultipartFile(image.getName(),
                    decryptedImageByte);
        } catch (BadPaddingException e) {
            throw new BadPaddingException();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedImage;
    }
}
