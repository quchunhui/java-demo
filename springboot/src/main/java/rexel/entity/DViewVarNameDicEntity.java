package rexel.entity;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;
import lombok.Data;

@Data
@Entity
@IdClass(DViewVarNameDicEntity.class)
@Table(name = "DVIEW_VAR_NAME_DIC")
public class DViewVarNameDicEntity implements Serializable {
    @Id
    @Column(name = "apiItemName")
    private String apiItemName;

    @Id
    @Column(name = "dviewVarType")
    private String dviewVarType;

    @Id
    @Column(name = "dviewVarName")
    private String dviewVarName;
}
