package microservices.base.proxy.security.jwt;


import io.jsonwebtoken.*;
import microservices.base.proxy.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;


public class TokenProvider {

    @Value("${spring.application.security.jwt.secret-key}")
    @NotNull
    private String secretKey;


    public String create(Authentication authentication) {

        return Jwts.builder()
                .setSubject(authentication.getName())
                .claim( Constants.JWT.Auth,
                        authentication.getAuthorities().stream()
                                .map(GrantedAuthority::getAuthority).collect(Collectors.joining(","))

                )
                .setExpiration(
                        Date.from(
                            LocalDateTime.now()
                                    .plusHours(1)
                                    .atZone(ZoneId.systemDefault())
                                    .toInstant()
                        )
                )
                .signWith(SignatureAlgorithm.HS512, secretKey)
                .compact();
    }


    public boolean validate(String token) {

        boolean valid = false;
        try {
            parse(token);
            valid = true;
        } catch (ExpiredJwtException | UnsupportedJwtException | MalformedJwtException | SignatureException| IllegalArgumentException e) {
            e.printStackTrace();
        } finally {
            return valid;
        }

    }

    private Jws<Claims> parse(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                ;
    }

    public Authentication tokenToAuthentication(String token) {
        Authentication authentication = null;

        Jws<Claims>  claims  = parse(token);
        String subject = claims.getBody().getSubject();
        Collection<? extends GrantedAuthority> authorities =
                Arrays.asList(StringUtils.split(claims.getBody().get(Constants.JWT.Auth, String.class),","))
                .stream()
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());


                ;

        User user = new User(subject, StringUtils.EMPTY, authorities);
        authentication  = new UsernamePasswordAuthenticationToken(user, token, authorities);

        return authentication;
    }
}
