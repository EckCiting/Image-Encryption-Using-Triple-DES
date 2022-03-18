package xjtlu.tdes.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.commons.codec.digest.DigestUtils;


@Entity
@Data
@Builder
@AllArgsConstructor
@Table(name = "ImagesWithSalt",indexes= {
        @Index(columnList="expireDate")
})
public class TDESImage implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @Column(length = 20)
    private String imageName;

    @Column(length = 35, unique = true)
    private String imageHash;


    @Column(length = 50)
    private String salt;

    @Column()
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireDate;

    public TDESImage() {

    }

    public TDESImage(String imageName, String expireDateString){
        this.imageName = imageName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.CHINA);
        LocalDateTime ldt = LocalDateTime.parse(expireDateString, formatter);
        this.expireDate = Date.from(ldt.atZone(ZoneId.of("UTC+8")).toInstant());
    }
}