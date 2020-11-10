package rexel.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@IdClass(AccessTokenEntity.class)
@Table(name = "ACCESS_TOKEN")
public class AccessTokenEntity implements Serializable {
    @Id
    @Column(name = "accessId")
    private String accessId;

    @Id
    @Column(name = "accessKey")
    private String accessKey;

    @Id
    @Column(name = "deviceId")
    private String deviceId;

    @Column(name = "token")
    private String token;

    @Column(name = "insertTime")
    private Timestamp insertTime;
}
