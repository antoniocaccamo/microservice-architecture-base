package me.antoniocaccamo.ors.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.RemoteTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration @EnableResourceServer
@EnableGlobalMethodSecurity(prePostEnabled = true) @Slf4j
public class ResourcesServerConfiguration  extends ResourceServerConfigurerAdapter {

    public static final String SECURED_PATTERN     = "/**";
    public static final String SECURED_READ_SCOPE  = "#oauth2.hasScope('read')";
    public static final String SECURED_WRITE_SCOPE = "#oauth2.hasScope('write')";


    @Value("${resource.id}")
    private String resourceId;

    @Value("${resource.token-info-uri}")
    private String checkTokenEndpointUrl;

    @Value("${client.client-id}")
    private String clientId;

    @Value("${client.client-secret}")
    private String clientSecret;


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();

    }

//    @Bean
//    public TokenStore tokenStore() {
//        return new JdbcTokenStore(dataSource());
//
//    }

//    @Bean
//    public OAuth2AuthenticationManager oAuth2AuthenticationManager() {
//        OAuth2AuthenticationManager authenticationManager  = new OAuth2AuthenticationManager();
//        authenticationManager.setClientDetailsService().;
//    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources
                .resourceId(resourceId)
                .tokenServices(remoteTokenServices())
        ;

        log.info("resourceId : {}", resourceId);
    }

    @Bean
    public RemoteTokenServices remoteTokenServices() {
        final RemoteTokenServices tokenService = new RemoteTokenServices();
        tokenService.setCheckTokenEndpointUrl(checkTokenEndpointUrl);
        tokenService.setClientId(clientId);
        tokenService.setClientSecret(clientSecret);

        log.info("remoteTokenServices : checkTokenEndpointUrl {} clientId {} clientSecret {}", checkTokenEndpointUrl, clientId, clientSecret);
        return tokenService;
    }


    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                    .disable()
                .csrf()
                    .disable()
                .requestMatchers()
                    .antMatchers(SECURED_PATTERN)
                        .and()
                        .authorizeRequests()
                            .antMatchers(HttpMethod.GET   , SECURED_PATTERN).access(SECURED_READ_SCOPE)
                            .antMatchers(HttpMethod.POST  , SECURED_PATTERN).access(SECURED_WRITE_SCOPE)
                            .antMatchers(HttpMethod.PATCH , SECURED_PATTERN).access(SECURED_WRITE_SCOPE)
                            .antMatchers(HttpMethod.PUT   , SECURED_PATTERN).access(SECURED_WRITE_SCOPE)
                            .antMatchers(HttpMethod.DELETE, SECURED_PATTERN).access(SECURED_WRITE_SCOPE)
//                .and()
//                    .headers().addHeaderWriter((request, response) -> {
//                        response.addHeader("Access-Control-Allow-Origin", "*");
//                        if (request.getMethod().equals("OPTIONS")) {
//                            response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
//                            response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
//                        }
//                    })
                .and()
                    .exceptionHandling()
                        .accessDeniedHandler(new OAuth2AccessDeniedHandler())
        ;
    }


}
