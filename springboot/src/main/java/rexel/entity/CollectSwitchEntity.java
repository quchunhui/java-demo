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
@IdClass(CollectSwitchEntity.class)
@Table(name = "COLLECT_SWITCH")
public class CollectSwitchEntity implements Serializable {
    @Id
    @Column(name = "collect")
    private int collect;
}