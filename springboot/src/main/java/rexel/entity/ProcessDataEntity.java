package rexel.entity;

import java.io.Serializable;
import java.sql.Timestamp;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;

@Data
@Entity
@IdClass(ProcessDataEntity.class)
@Table(name = "PROCESSED_DATA")
public class ProcessDataEntity implements Serializable {
    @Id
    @Column(name = "autoId")
    private String autoId;

    @Column(name = "id")
    private String id;

    @Column(name = "deviceUnique")
    private String deviceUnique;

    @Column(name = "collectDate")
    private String collectDate;

    @Column(name = "aluGrade")
    private String aluGrade;

    @Column(name = "aluState")
    private String aluState;

    @Column(name = "thickness")
    private float thickness;

    @Column(name = "width")
    private float width;

    @Column(name = "length")
    private float length;

    @Column(name = "isFilm")
    private int isFilm;

    @Column(name = "cnt")
    private int cnt;

    @Column(name = "status")
    private int status;

    @Column(name = "insertTime")
    @CreatedDate
    private Timestamp insertTime;

    @Column(name = "insertUser")
    private String insertUser;

    @Column(name = "updateTime")
    @CreatedDate
    private Timestamp updateTime;

    @Column(name = "updateUser")
    private String updateUser;
}