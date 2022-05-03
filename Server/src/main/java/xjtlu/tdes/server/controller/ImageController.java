package xjtlu.tdes.server.controller;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xjtlu.tdes.server.service.ImageService;
import xjtlu.tdes.server.service.SaltService;

import javax.annotation.Resource;
import java.io.IOException;

@CrossOrigin
@RestController
@RequestMapping("/")

public class ImageController {
    @Resource
    private ImageService imageService;

    private final SaltService saltService;

    public ImageController(SaltService saltService) {
        this.saltService = saltService;
    }

    @PostMapping("/encryptimage")
    @ExceptionHandler()
    public ResponseEntity<?> encryptImage(@RequestParam("image") @NotNull MultipartFile image, @RequestParam String password,
                                          @RequestParam String expireDate)  {
        saltService.expirDateCheck();
        return imageService.imageEncryption(image, password, expireDate);
    }

    @PostMapping("/decryptimage")
    public ResponseEntity<?> decryptImage(@RequestParam("image") @NotNull MultipartFile image, @RequestParam String password) {
        saltService.expirDateCheck();
        return imageService.imageDecryption(image, password);
    }
}
