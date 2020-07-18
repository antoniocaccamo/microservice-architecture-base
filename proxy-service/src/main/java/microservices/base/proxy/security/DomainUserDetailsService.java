package microservices.base.proxy.security;

import microservices.base.proxy.domain.DomainUser;
import microservices.base.proxy.repository.DomainUserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

import java.util.Optional;


@Component("userDetailsService")
public class DomainUserDetailsService implements UserDetailsService {

    private final DomainUserRepository domainUserRepository;

    public DomainUserDetailsService(DomainUserRepository domainUserRepository) {
        this.domainUserRepository = domainUserRepository;
    }


    @Override
    public UserDetails loadUserByUsername(String user) throws UsernameNotFoundException {

        Optional<DomainUser> domainUser =domainUserRepository.findOneByUser(user);

        return domainUser
                .map(SecurityUtils::toUser)
                .orElseThrow( () -> new UsernameNotFoundException(String.format("%s : not found", user)));
    }

}
