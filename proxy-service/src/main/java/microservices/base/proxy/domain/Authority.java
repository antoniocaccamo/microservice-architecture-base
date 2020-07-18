package microservices.base.proxy.domain;

import lombok.Data;

import javax.persistence.*;

/**
 * @author antonio on 17/07/2020
 */

@Entity
@Table(name = "AUTHORITY")
@Data
public class Authority extends EntityBase{

    public enum RoleEnum {
        ROLE_ADMIN,
        ROLE_USER ,
        ROLE_ANONYMOUS
    }

    @Id
    @Enumerated(EnumType.STRING)
    private RoleEnum name;

}
