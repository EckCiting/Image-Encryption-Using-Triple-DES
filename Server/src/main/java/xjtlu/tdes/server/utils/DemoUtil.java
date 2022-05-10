package xjtlu.tdes.server.utils;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import xjtlu.tdes.server.entity.ImageDemoInfo;
import xjtlu.tdes.server.repository.ImageDemoInfoRepository;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.lang.reflect.Array;
import java.util.Arrays;

import static javax.crypto.Cipher.DECRYPT_MODE;
import static javax.crypto.Cipher.ENCRYPT_MODE;

@Component
public class DemoUtil {

    @Autowired
    private ImageDemoInfoRepository imageDemoInfoRepository;

    public byte[] ManualTDESEncryption(byte[] image, String password, String salts, byte[] b) throws BadPaddingException {
        String salt1 = salts.substring(0, 16);
        String salt2 = salts.substring(16, 32);

        byte[] keyBytes = KeyGenerationModule.getKey(password, salt1, salt2);
        byte[] encryptedImageByte = null;
        try {
            //System.out.println("keyBytes: " + Arrays.toString(keyBytes));

            /*
             *  The size of the key used in each stage is 8 Bytes (64 bits)
             *  8 bits are used for checksum, so the valid key bits are 56 bits
             *  Therefore the valid bits of 3DES key is 56 * 3 = 168 bit
             */
            final SecretKey key2 = new SecretKeySpec(Arrays.copyOfRange(keyBytes, 0, 8), "DES");
            final SecretKey key3 = new SecretKeySpec(Arrays.copyOfRange(keyBytes, 8, 16), "DES");
            final SecretKey key4 = new SecretKeySpec(Arrays.copyOfRange(keyBytes, 16, 24), "DES");


            /*
             *  Encryption operation requires the image being padded to a multiple of 8 bits
             *  Even if its size is already a multiple of 8 bits (It will then be added 8bit with PKCS5Padding)
             */
            /*
            final Cipher encryptCipher1  = Cipher.getInstance("DES/ECB/PKCS5Padding");
            encryptCipher1.init(Cipher.ENCRYPT_MODE, key2);
            byte[] stage1 = encryptCipher1.doFinal(image);
            //System.out.println("DESKey1: " + Arrays.toString(key2.getEncoded()));
            //System.out.println("After the first stage: encryption: " + Arrays.toString(stage1));

            final Cipher encryptCipher2  = Cipher.getInstance("DES/ECB/NoPadding");
            encryptCipher2.init(DECRYPT_MODE, key3);
            byte[] stage2 = encryptCipher2.doFinal(stage1);
            //System.out.println("DESKey2: " + Arrays.toString(key3.getEncoded()));
            //System.out.println("After the second stage: decryption: " + Arrays.toString(stage2));

            final Cipher encryptCipher3  = Cipher.getInstance("DES/ECB/NoPadding");
            encryptCipher3.init(Cipher.ENCRYPT_MODE, key4);
            byte[] stage3 = encryptCipher3.doFinal(stage2);
            //System.out.println("DESKey3: " + Arrays.toString(key4.getEncoded()));
            //System.out.println("After the third stage: encryption: " + Arrays.toString(stage3));*/


            ManualDES des = new ManualDES();
            byte[] stage1 = des.Cipher(image,key2.getEncoded(),ENCRYPT_MODE);
            byte[] stage2 = des.Cipher(stage1,key3.getEncoded(),DECRYPT_MODE);
            byte[] stage3 = des.Cipher(stage2,key4.getEncoded(),ENCRYPT_MODE);
            storeToDatabase(1, image,salts,keyBytes,stage1,stage2,stage3);

            encryptedImageByte = stage3;

           storeToDatabase(0, image,salts,keyBytes,stage1,stage2,stage3);


        }/* catch (BadPaddingException e) {
            e.printStackTrace();
            throw new BadPaddingException();
        } */catch (Exception e) {
            e.printStackTrace();
        }
        return encryptedImageByte;
    }

    public byte[] ManualTDESDecryption(byte[] image, String password, String salts, byte[] b) throws BadPaddingException {
        byte[] decryptedImageByte = null;
        String salt1 = salts.substring(0, 16);
        String salt2 = salts.substring(16, 32);

        byte[] keyBytes = KeyGenerationModule.getKey(password, salt1, salt2);
        try {

            System.out.println("keyBytes: " + Arrays.toString(keyBytes));

            /*
             *  The size of the key used in each stage is 8 Bytes (64 bits)
             *  8 bits are used for checksum, so the valid key bits are 56 bits
             *  Therefore the valid bits of 3DES key is 56 * 3 = 168 bit
             */
            final SecretKey key2 = new SecretKeySpec(Arrays.copyOfRange(keyBytes, 16, 24), "DES");
            final SecretKey key3 = new SecretKeySpec(Arrays.copyOfRange(keyBytes, 8, 16), "DES");
            final SecretKey key4 = new SecretKeySpec(Arrays.copyOfRange(keyBytes, 0, 8), "DES");


            /*
             *  The size of the encrypted image is already the multiple of 8 bit
             *  If still use PKCS5Padding at the first stage, it will add an extra 8 bits to the end of the line
             *  Then a BadPaddingException will be thrown.
             */

            /*
            final Cipher decryptCipher1  = Cipher.getInstance("DES/ECB/NoPadding");
            decryptCipher1.init(Cipher.DECRYPT_MODE, key2);
            byte[] stage1 = decryptCipher1.doFinal(image);

            //System.out.println("DESKey1: " + Arrays.toString(key2.getEncoded()));
            //System.out.println("After the first stage: encryption: " + Arrays.toString(stage1));

            final Cipher decryptCipher2  = Cipher.getInstance("DES/ECB/NoPadding");
            decryptCipher2.init(Cipher.ENCRYPT_MODE, key3);
            byte[] stage2 = decryptCipher2.doFinal(stage1);
            //System.out.println("DESKey2: " + Arrays.toString(key3.getEncoded()));
            //System.out.println("After the second stage: decryption: " + Arrays.toString(stage2));

            final Cipher decryptCipher3  = Cipher.getInstance("DES/ECB/NoPadding");
            decryptCipher3.init(Cipher.DECRYPT_MODE, key4);
            byte[] stage3 = decryptCipher3.doFinal(stage2);
            //System.out.println("DESKey3: " + Arrays.toString(key4.getEncoded()));
            //System.out.println("After the third stage: encryption: " + Arrays.toString(stage3));*/

            ManualDES des = new ManualDES();
            byte[] stage1 = des.Cipher(image,key2.getEncoded(),DECRYPT_MODE);
            byte[] stage2 = des.Cipher(stage1,key3.getEncoded(),ENCRYPT_MODE);
            byte[] stage3 = des.Cipher(stage2,key4.getEncoded(),DECRYPT_MODE);
            decryptedImageByte = stage3;
            storeToDatabase(1, image,salts,keyBytes,stage1,stage2,stage3);


        } /*catch (BadPaddingException e) {
            throw new BadPaddingException();
        }*/ catch (Exception e) {
            e.printStackTrace();
        }
        return decryptedImageByte;
    }

    public void storeToDatabase(int type, byte[] image, String salt, byte[] keyBytes, byte[] stage1, byte[] stage2, byte[] stage3) {
        String imageHash = DigestUtils.md5Hex(image);

        ImageDemoInfo imageInfo = new ImageDemoInfo(type, imageHash, salt, keyBytes, stage1, stage2, stage3);
        try {
            imageDemoInfoRepository.save(imageInfo);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
}
