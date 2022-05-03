package xjtlu.tdes.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import xjtlu.tdes.server.entity.TDESImage;

import java.util.Date;
import java.util.List;
import java.util.Optional;

@Repository
public interface ImageRepository extends JpaRepository<TDESImage, Integer> {
    Optional<TDESImage> findByImageHash(String encryptedImageHash);
    List<TDESImage> findByExpireDateBefore(Date date);
}