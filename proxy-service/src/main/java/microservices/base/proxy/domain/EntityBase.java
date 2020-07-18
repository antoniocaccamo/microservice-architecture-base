package microservices.base.proxy.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * @author antonio on 17/07/2020
 */
@MappedSuperclass
@Getter
@Setter
public class EntityBase implements Serializable {


    @Column(name = "last_modify_at")
    private LocalDate lastModifyAt;

    @Column(name = "created_at")
    private LocalDate dataModifica;
}
