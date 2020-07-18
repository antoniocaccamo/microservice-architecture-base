package microservices.base.proxy.domain;


import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.HashSet;
import java.util.Set;

/**
 * @author antonio on 17/07/2020
 */
@Entity
@Table(name = "USER")
@Data
public class DomainUser extends EntityBase {


    @NotNull
    @Size(min = 1, max = 50)
    @Column(length = 50, unique = true, nullable = false)
    private String user;


    @NotNull
    @Size(min = 60, max = 60)
    @Column(name = "password_hash",length = 60)
    private String password;


    @Email
    @Size(min = 5, max = 100)
    @Column(length = 100, unique = true)
    private String email;


    @JsonIgnore
    @OneToMany (
            mappedBy = ""
    )
    @JoinTable(
            name = "USER_AUTHORITY",
            joinColumns        = {@JoinColumn(name = "USER"     , referencedColumnName = "USER")},
            inverseJoinColumns = {@JoinColumn(name = "AUTHORITY", referencedColumnName = "NAME")})
    private Set<Authority> authorities = new HashSet<>();


}
