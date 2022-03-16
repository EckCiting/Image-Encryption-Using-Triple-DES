package xjtlu.tdes.server.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;


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

    @Column(length = 35, unique = true)
    private String encryptedImageHash;

    @Column(length = 50)
    private String salt;

    @Column()
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date expireDate;

    public TDESImage() {

    }
}