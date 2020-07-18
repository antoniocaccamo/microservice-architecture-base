package microservices.base.proxy.repository;

import microservices.base.proxy.domain.DomainUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * @author antonio on 17/07/2020
 */
public interface DomainUserRepository extends JpaRepository<DomainUser, String> {


    Optional<DomainUser> findOneByUser(String user);


}
