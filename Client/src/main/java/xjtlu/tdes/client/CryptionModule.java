package xjtlu.tdes.client;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import xjtlu.tdes.client.Utilities.ImageUtility;
import xjtlu.tdes.client.Utilities.WebUtility;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import java.sql.SQLException;

import static xjtlu.tdes.client.TransmissionModule.addImageToDatabase;
import static xjtlu.tdes.client.TransmissionModule.getSaltFromDatabase;

public class CryptionModule {
    private static String getSalts(String imageName) throws SQLException {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(getSaltFromDatabase(imageName));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        int status = node.path("status").asInt();
        switch (status){
            case Config.IMAGE_NOT_FOUND: throw new SQLException("Image not found");
            default: return node.path("data").asText();
        }
    }
    private static void addImage(String imageName,String expireDate) throws SQLException  {
        ObjectMapper mapper = new ObjectMapper();
        JsonNode node = null;
        try {
            node = mapper.readTree(addImageToDatabase(imageName,expireDate));
        } catch (Exception e) {
            e.printStackTrace();
        }
        int status = node.path("status").asInt();
        switch (status){
            case Config.IMAGE_ALREADY_EXISTS: throw new SQLException("Image already exists");
            default:
        }
    }
    public static void TDESEncryption(String imageName, String password, byte[] b,String expireDate) throws SQLException {
        addImage(imageName, expireDate);

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

            // 这里再多一个接口吧 origin hash 和 encrypted hash 绑定到一起加到数据库中
        } catch (Exception e){
            e.printStackTrace();
        }
    }

    public static void TDESDecryption(String imageName,String password, byte[] b) throws SQLException {
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
