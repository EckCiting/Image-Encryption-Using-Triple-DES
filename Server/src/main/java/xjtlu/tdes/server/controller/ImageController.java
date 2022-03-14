package xjtlu.tdes.server.controller;
import org.springframework.web.bind.annotation.*;
import xjtlu.tdes.server.Response;
import xjtlu.tdes.server.entity.TDESImage;
import xjtlu.tdes.server.service.ImageService;

import javax.annotation.Resource;

@RestController
@RequestMapping("/")
public class ImageController {
    @Resource
    private ImageService imageService;

    @PostMapping("/getsalt/")
    public Response<?> getImageSalt(@RequestBody TDESImage tdesImage){
        return imageService.imageSaltObtain(tdesImage);
    }

    @PostMapping("/addimage/")
    public Response<?> addImageInfo(@RequestBody TDESImage tdesImage){
        return imageService.imageInfoCreate(tdesImage);
    }
}
