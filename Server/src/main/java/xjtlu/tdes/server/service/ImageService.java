package xjtlu.tdes.server.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface ImageService {
    ResponseEntity<?> imageEncryption(MultipartFile image, String password, String expireDate);
    ResponseEntity<?> imageDecryption(MultipartFile image, String password);
}
