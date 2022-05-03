package xjtlu.tdes.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xjtlu.tdes.server.entity.ImageDemoInfo;

import java.util.Optional;

@Repository
public interface ImageDemoInfoRepository extends JpaRepository<ImageDemoInfo, Integer> {
    Optional<ImageDemoInfo> findByImageHash(String imageHash);
}