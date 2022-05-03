package xjtlu.tdes.server.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.apache.commons.codec.binary.Hex;
import org.springframework.web.multipart.MultipartFile;

import javax.persistence.*;
import java.io.Serializable;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;

@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@Builder
@AllArgsConstructor
@Table(name = "ImagesDemoInfo")
public class ImageDemoInfo implements Serializable {
    private static final long serialVersionUID = 1L;

    @Id
    @JsonIgnore
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer imageId;

    @Column
    private int type; // 0 for encryption, 1 for decryption

    @Column(length = 35, unique = true)
    private String imageHash;

    @Column(length = 50)
    String salt;

    @Column(length = 50)
    String key1;

    @Column(length = 50)
    String key2;

    @Column(length = 50)
    String key3;

    @Column(length = 50)
    String stage1;

    @Column(length = 100)
    String stage2;

    @Column(length = 100)
    String stage3;

    public ImageDemoInfo(int type, String imageHash, String salt, byte[] keyBytes, byte[] stage1, byte[] stage2, byte[] stage3){
        this.type = type;
        byte[] key1 = Arrays.copyOfRange(keyBytes, 0, 8) ;
        byte[] key2 = Arrays.copyOfRange(keyBytes, 8, 16);
        byte[] key3 = Arrays.copyOfRange(keyBytes, 16, 24);
//        ImageDemoInfo.builder().type(type).salt(salt).imageHash(imageHash)
//                .key1(Hex.encodeHexString(key1)).key2(Hex.encodeHexString(key2)).key3(Hex.encodeHexString(key3))
//                .stage1(Hex.encodeHexString(Arrays.copyOfRange(stage1,0,15)))
//                .stage2(Hex.encodeHexString(Arrays.copyOfRange(stage2,0,15)))
//                .stage3(Hex.encodeHexString(Arrays.copyOfRange(stage3,0,15))).build();

        this.imageHash = imageHash;
        this.salt = salt;
        this.key1 = Hex.encodeHexString(key1);
        this.key2 = Hex.encodeHexString(key2);
        this.key3 = Hex.encodeHexString(key3);
        this.stage1 = Hex.encodeHexString(Arrays.copyOfRange(stage1,0,15));
        this.stage2 = Hex.encodeHexString(Arrays.copyOfRange(stage2,0,15));
        this.stage3 = Hex.encodeHexString(Arrays.copyOfRange(stage3,0,15));
    }

}
