package xjtlu.tdes.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import xjtlu.tdes.server.entity.TDESImage;
import xjtlu.tdes.server.repository.ImageRepository;
import xjtlu.tdes.server.service.ImageService;
import xjtlu.tdes.server.Response;
import xjtlu.tdes.server.utils.SaltUtil;

import java.util.Optional;

@Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    ImageRepository imageRepository;

    // Need to consider how to prevent replay attacks
    @Override
    public Response<?> imageSaltObtain(TDESImage image) {
        Optional<TDESImage> tdesImage = imageRepository.findByImageHash(image.getImageHash());
        if (tdesImage.isPresent()) {
            return Response.ok("Found",tdesImage.get().getSalt());
        }
        else
            return Response.ok("Image Not Found");
    }

    @Override
    public Response<?> imageInfoCreate(TDESImage newImage) {

        Optional<TDESImage> tdesImage = imageRepository.findByImageHash(newImage.getImageHash());
        if (tdesImage.isPresent()) {
            return Response.ok("Image Already Exists");
        }
        else {
            newImage.setSalt(SaltUtil.saltGeneration(32));
            imageRepository.save(newImage);
            return Response.ok("Add New File Success");
        }
    }
}
