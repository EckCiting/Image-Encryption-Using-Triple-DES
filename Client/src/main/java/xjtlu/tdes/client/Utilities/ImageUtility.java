package xjtlu.tdes.client.Utilities;

import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
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

}
