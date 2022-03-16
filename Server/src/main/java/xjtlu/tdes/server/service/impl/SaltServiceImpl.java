package xjtlu.tdes.server.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import xjtlu.tdes.server.entity.TDESImage;
import xjtlu.tdes.server.repository.ImageRepository;
import xjtlu.tdes.server.service.SaltService;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Component
public class SaltServiceImpl implements SaltService {
    @Autowired
    ImageRepository imageRepository;

    @Override
    @Scheduled(fixedRate = 5000)
    public void expirDateCheck() {
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        //LocalDateTime ldt = LocalDateTime.parse("2022-03-16 00:00:00", formatter);
        LocalDateTime now = LocalDateTime.now();
        List<TDESImage> expiredImage = imageRepository.findByExpireDateBefore(Date.from(now.atZone(ZoneId.of("UTC+8")).toInstant()));
        for (TDESImage image: expiredImage){
            image.setSalt("");
            imageRepository.save(image);
        }
    }
}

