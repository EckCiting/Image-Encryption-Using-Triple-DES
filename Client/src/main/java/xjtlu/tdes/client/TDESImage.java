package xjtlu.tdes.client;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import org.apache.commons.codec.digest.DigestUtils;
@Data
@AllArgsConstructor
public class TDESImage {
    private String imageName;
    private String imagePath;
    private String imageHash;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireDate;

    private String salt;

    public TDESImage(String imageName){
        this.imageName = imageName;
        try (InputStream is = Files.newInputStream(Paths.get("res/encrypt_"+imageName))) {
            this.imageHash = DigestUtils.md5Hex(is);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public TDESImage(String imageName, String expireDateString){
        this(imageName);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        LocalDateTime ldt = LocalDateTime.parse(expireDateString, formatter);
        this.expireDate = Date.from(ldt.atZone(ZoneId.of("UTC+8")).toInstant());

    }

}
