package xjtlu.tdes.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import xjtlu.tdes.client.Utilities.ImageUtility;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import static xjtlu.tdes.client.TransmissionModule.addImageToDatabase;
import static xjtlu.tdes.client.TransmissionModule.getSaltFromDatabase;

public class CryptionModule {
    public static String getSalts(String imageName){
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(getSaltFromDatabase(imageName));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return node.path("data").asText();
    }

    public static void TDESEncryption(String imageName, String password, byte[] b,String expireDate){
        try {
            addImageToDatabase(imageName,expireDate);
        } catch (Exception e) {
            e.printStackTrace();
        }

        // get salt from remote server
        String salts = getSalts(imageName);
        String salt1 = salts.substring(0,16);
        String salt2 = salts.substring(16,32);

        try {
            byte[] keyBytes = KeyGenerationModule.getKey(password,salt1,salt2);
            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(b); // [0,0,0,0,0,0,0,0]
            final Cipher encryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            encryptCipher.init(Cipher.ENCRYPT_MODE, key, iv);
            ImageUtility.ImageEncryption("XJTLU-logo.png",encryptCipher);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void TDESDecryption(String imageName,String password, byte[] b){
        String salts = getSalts(imageName);
        String salt1 = salts.substring(0,16);
        String salt2 = salts.substring(16,32);
        try {
            byte[] keyBytes = KeyGenerationModule.getKey(password,salt1,salt2);
            final SecretKey key = new SecretKeySpec(keyBytes, "DESede");
            final IvParameterSpec iv = new IvParameterSpec(b); // [0,0,0,0,0,0,0,0]
            final Cipher decryptCipher = Cipher.getInstance("DESede/CBC/PKCS5Padding");
            decryptCipher.init(Cipher.DECRYPT_MODE, key, iv);
            ImageUtility.ImageDecryption("XJTLU-logo.png",decryptCipher);
        } catch (Exception e){
            e.printStackTrace();
        }
    }

}
