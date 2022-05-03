package xjtlu.tdes.server.service;

import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

public interface DemoService {
    ResponseEntity<?> imageEncryption(MultipartFile image);
    ResponseEntity<?> imageDecryption(MultipartFile image);
}
