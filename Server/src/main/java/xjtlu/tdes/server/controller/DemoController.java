package xjtlu.tdes.server.controller;
import com.sun.istack.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import xjtlu.tdes.server.entity.ImageDemoInfo;
import xjtlu.tdes.server.service.DemoService;
import xjtlu.tdes.server.service.ImageService;
import xjtlu.tdes.server.service.SaltService;

import javax.annotation.Resource;

@CrossOrigin
@RestController
@RequestMapping("/")
public class DemoController {
    @Resource
    private DemoService demoService;

    @PostMapping("/encryptimagedemo")
    @ExceptionHandler()
    public ResponseEntity<?> encryptImageDemo(@RequestParam("image") @NotNull MultipartFile image) {
       //saltService.expirDateCheck();
        return demoService.imageEncryption(image);
    }

    @PostMapping("/decryptimagedemo")
    public ResponseEntity<?> decryptImageDemo(@RequestParam("image") @NotNull MultipartFile image) {
//        saltService.expirDateCheck();
        return demoService.imageDecryption(image);
    }
}
