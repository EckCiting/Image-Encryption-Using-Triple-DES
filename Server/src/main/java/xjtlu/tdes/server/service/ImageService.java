package xjtlu.tdes.server.service;

import xjtlu.tdes.server.Response;
import xjtlu.tdes.server.entity.TDESImage;

public interface ImageService {
    Response<?> imageSaltObtain(TDESImage image);
    Response<?> imageInfoCreate(TDESImage newImage);
}
