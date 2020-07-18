package microservices.base.proxy.security.jwt;

import microservices.base.proxy.config.Constants;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



public class JWTFilter extends OncePerRequestFilter {

    private final TokenProvider tokenProvider;

    public JWTFilter(TokenProvider tokenProvider) {
        this.tokenProvider = tokenProvider;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) throws ServletException, IOException {

        Authentication authentication = null;
        String token = resolveJWT(request);
        if ( ! StringUtils.isEmpty(token)  && tokenProvider.validate(token) ) {
            authentication = tokenProvider.tokenToAuthentication(token);
        }
        SecurityContextHolder.getContext().setAuthentication(authentication);

        chain.doFilter(request, response);
    }


    private String resolveJWT(HttpServletRequest request) {

        String baerer = request.getHeader(Constants.Http.Header.Authorization);


        if ( StringUtils.isNotEmpty(baerer) && StringUtils.startsWithIgnoreCase(baerer, Constants.Http.Header.Bearer)) {

            return StringUtils.trim( StringUtils.substring(baerer, Constants.Http.Header.Bearer.length()));
        }
        return null;

    }


}
