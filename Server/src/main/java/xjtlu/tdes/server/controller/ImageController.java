package xjtlu.tdes.server.controller;
import com.sun.istack.NotNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xjtlu.tdes.server.service.ImageService;

import javax.annotation.Resource;


@RestController
@RequestMapping("/")
public class ImageController {
    @Resource
    private ImageService imageService;

    @PostMapping("/encryptimage")
    public ResponseEntity<?> encryptImage(@RequestParam("image") @NotNull MultipartFile image, @RequestParam String password,
                                          @RequestParam String expireDate){
        try {
            return imageService.imageEncryption(image,password,expireDate);
        }catch (Exception e){
            return new ResponseEntity<>("Could not upload the image",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/decryptimage")
    public ResponseEntity<?> decryptImage(@RequestParam("image") @NotNull MultipartFile image, @RequestParam String password){
        try {
            return imageService.imageDecryption(image,password);
        }catch (Exception e){
            return new ResponseEntity<>("Could not upload the image",HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
