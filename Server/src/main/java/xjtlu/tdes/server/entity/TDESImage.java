package xjtlu.tdes.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
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

    @Column(length = 8)
    private byte[] iv;

    @Column(length = 50)
    private String salt;

    @Column()
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm", timezone = "GMT+8")
    private Date expireDate;

    public TDESImage(String imageName, String expireDateString) {
        this.imageName = imageName;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm", Locale.CHINA);
        LocalDateTime ldt = LocalDateTime.parse(expireDateString, formatter);
        this.expireDate = Date.from(ldt.atZone(ZoneId.of("UTC+8")).toInstant());
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        TDESImage tdesImage = (TDESImage) o;
        return imageId != null && Objects.equals(imageId, tdesImage.imageId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}