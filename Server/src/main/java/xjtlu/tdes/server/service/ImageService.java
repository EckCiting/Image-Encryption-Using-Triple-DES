package xjtlu.tdes.server.service;

import xjtlu.tdes.server.Response;
import xjtlu.tdes.server.entity.TDESImage;

public interface ImageService {
    Response<?> imageSaltObtain(String imageName);
    Response<?> imageInfoCreate(TDESImage newImage);
}
