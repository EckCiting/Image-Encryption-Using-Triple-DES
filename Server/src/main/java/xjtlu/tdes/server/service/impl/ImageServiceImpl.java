package xjtlu.tdes.server.service.impl;

import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xjtlu.tdes.server.entity.TDESImage;
import xjtlu.tdes.server.repository.ImageRepository;
import xjtlu.tdes.server.service.ImageService;
import xjtlu.tdes.server.utils.CryptionModule;
import xjtlu.tdes.server.utils.SaltUtil;

import javax.crypto.BadPaddingException;
import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    @Override
    public ResponseEntity<?> imageEncryption(MultipartFile image, String password, String expireDate) {
        if (image.isEmpty() || password.isEmpty() || expireDate.isEmpty())
            return new ResponseEntity<>("Missing parameters", HttpStatus.BAD_REQUEST);
        try {
            TDESImage newImage = new TDESImage(image.getOriginalFilename(), expireDate);
            newImage.setSalt(SaltUtil.saltGeneration(32));
            MultipartFile encryptedImage = CryptionModule.TDESEncryption(image, password, newImage.getSalt(), new byte[8]);
            newImage.setImageHash(DigestUtils.md5Hex(encryptedImage.getBytes()));
            imageRepository.save(newImage);
            HttpHeaders responseHeaders = new HttpHeaders();
            responseHeaders.add("Content-Disposition", "attachment; filename=encrypted_" + newImage.getImageName());
            responseHeaders.add("Pragma", "no-cache");
            responseHeaders.add("Cache-Control", "no-cache");
            responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
            return new ResponseEntity<>(encryptedImage.getBytes(), responseHeaders, HttpStatus.OK);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.valueOf("Encryption failed"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    public ResponseEntity<?> imageDecryption(MultipartFile image, String password) {
        if (image.isEmpty() || password.isEmpty())
            return new ResponseEntity<>("Missing parameters", HttpStatus.BAD_REQUEST);

        try {
            Optional<TDESImage> res = imageRepository.findByImageHash(DigestUtils.md5Hex(image.getBytes()));
            if (res.isPresent()) {
                if (res.get().getSalt().isEmpty()) {
                    return new ResponseEntity<>("Image has expired", HttpStatus.FORBIDDEN);
                } else {
                    MultipartFile decryptedImage = CryptionModule.TDESDecryption(image, password, res.get().getSalt(), new byte[8]);
                    HttpHeaders responseHeaders = new HttpHeaders();
                    responseHeaders.add("Content-Disposition", "attachment; filename=decrypted_" + res.get().getImageName());
                    responseHeaders.add("Pragma", "no-cache");
                    responseHeaders.add("Cache-Control", "no-cache");
                    responseHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
                    return new ResponseEntity<>(decryptedImage.getBytes(), responseHeaders, HttpStatus.OK);
                }
            } else
                return new ResponseEntity<>("Image not found in databse", HttpStatus.NOT_FOUND);
        } catch (BadPaddingException e) {
            return new ResponseEntity<>("Wrong password", HttpStatus.FORBIDDEN);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ResponseEntity<>(HttpStatus.valueOf("Decryption failed"), HttpStatus.INTERNAL_SERVER_ERROR);
    }

}
