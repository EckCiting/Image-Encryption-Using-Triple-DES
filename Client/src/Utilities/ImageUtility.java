package Utilities;

import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;

public class ImageUtility {
    public static void ImageEncryption(String filename, Cipher cipher){
        // read image
        BufferedImage originImage = null;
        byte [] originImageBytes = new byte[0];
        try {
            originImage = ImageIO.read(new File("res/" + filename));
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            ImageIO.write(originImage, "png", bos);
            originImageBytes = bos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }
        // encrypt image
        byte[] encryptedImageBytes = DESUtility.encrypt(originImageBytes, cipher);

        // store the encrypted image
        try {
            FileUtils.writeByteArrayToFile(new File("res/encrypt_"+ filename), encryptedImageBytes);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void ImageDecryption(String filename, Cipher cipher){
        // read encrypted image
        byte[] encryptImageBytes = new byte[0];
        File file = new File("res/encrypt_"+ filename);
        try {
            encryptImageBytes = FileUtils.readFileToByteArray(file);
        } catch (IOException e) {
            e.printStackTrace();
        }

        // decrypt image
        byte[] decryptedImageBytes = DESUtility.decrypt(encryptImageBytes, cipher);

        // store the decrypted image
        try {
            ByteArrayInputStream bis = new ByteArrayInputStream(decryptedImageBytes);
            BufferedImage bImage2 = ImageIO.read(bis);
            ImageIO.write(bImage2, "png", new File("res/decrypt_" + filename));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public static void main(String[] args) {
        try {
            String password = "meiyoumima";
            String salt1 = "flaasdfa23DJLJAa";
            String salt2 = "56DFS5678fasd5678";
            byte[] keyBytes = KeyGenerationModule.getKey(password,salt1,salt2);

            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(new byte[8]);

            final Cipher encryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, iv);
            final Cipher decryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, iv);

            ImageEncryption("XJTLU-logo.png",encryptCipher);
            ImageDecryption("XJTLU-logo.png",decryptCipher);

        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
