package microservices.base.proxy.security;

import microservices.base.proxy.domain.Authority;
import microservices.base.proxy.domain.DomainUser;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;

import java.util.Optional;
import java.util.stream.Collectors;

/**
 * @author antonio on 18/07/2020
 */
public class SecurityUtils {


    public static User toUser(DomainUser du) {
        return new User(du.getUser(), du.getPassword(),
                du.getAuthorities().stream()
                        .map(authority -> new SimpleGrantedAuthority(authority.getName().toString()))
                        .collect(Collectors.toList())
        );
    }

    /**
     *
     * @return
     */
    public static final Optional<Authentication> getCurrentUserAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }



    public static final boolean  isAuthenticated() {

        return SecurityUtils.getCurrentUserAuthentication()
                .map( authentication -> authentication.getAuthorities().stream()
                    .noneMatch( grantedAuthority -> Authority.RoleEnum.ROLE_ANONYMOUS.name().equals(((GrantedAuthority) grantedAuthority).getAuthority()))
                )
                .orElse( Boolean.FALSE);

    }

    public static final boolean  isCurrentUserInRole(Authority.RoleEnum role) {

        return SecurityUtils.getCurrentUserAuthentication()
                .map( authentication -> authentication.getAuthorities().stream()
                        .anyMatch( grantedAuthority -> role.name().equals(((GrantedAuthority) grantedAuthority).getAuthority()))
                )
                .orElse( Boolean.FALSE);

    }
}
