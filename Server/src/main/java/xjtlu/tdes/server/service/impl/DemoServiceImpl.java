package xjtlu.tdes.server.service.impl;

import com.fasterxml.jackson.databind.util.JSONPObject;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import xjtlu.tdes.server.entity.ImageDemoInfo;
import xjtlu.tdes.server.repository.ImageDemoInfoRepository;
import xjtlu.tdes.server.service.DemoService;

import java.io.IOException;
import java.util.Optional;

@Service
public class DemoServiceImpl implements DemoService {
    @Autowired
    private ImageDemoInfoRepository imageDemoInfoRepository;

    @Override
    public ResponseEntity<?> imageEncryption(MultipartFile image) {
        try {
            Optional<ImageDemoInfo> res = imageDemoInfoRepository.findByImageHash(DigestUtils.md5Hex(image.getBytes()));
            if(res.isPresent()){
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ResponseEntity<?> imageDecryption(MultipartFile image) {
        try {
            Optional<ImageDemoInfo> res = imageDemoInfoRepository.findByImageHash(DigestUtils.md5Hex(image.getBytes()));
            if(res.isPresent()){
                return new ResponseEntity<>(res.get(), HttpStatus.OK);
            }
            else
                return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}
