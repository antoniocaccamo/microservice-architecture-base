package me.antoniocaccamo.ors.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configurers.ResourceServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.error.OAuth2AccessDeniedHandler;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;

@Configuration @EnableGlobalMethodSecurity(prePostEnabled = true)
public class ResourcesServerConfiguration  extends ResourceServerConfigurerAdapter {


    @Bean
    @ConfigurationProperties(prefix = "spring.datasource")
    public DataSource dataSource() {
        return DataSourceBuilder.create().build();

    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource());

    }

//    @Bean
//    public OAuth2AuthenticationManager oAuth2AuthenticationManager() {
//        OAuth2AuthenticationManager authenticationManager  = new OAuth2AuthenticationManager();
//        authenticationManager.setClientDetailsService().;
//    }

    @Override
    public void configure(ResourceServerSecurityConfigurer resources) throws Exception {

        resources
                .resourceId("product_api")
                .tokenStore(tokenStore())
        ;
    }




    @Override
    public void configure(HttpSecurity http) throws Exception {
        http
                .anonymous()
                    .disable()
                .authorizeRequests()
                    .antMatchers(HttpMethod.GET   , "/**").access("#oauth2.hasScope('read')")
                    .antMatchers(HttpMethod.POST  , "/**").access("#oauth2.hasScope('write')")
                    .antMatchers(HttpMethod.PATCH , "/**").access("#oauth2.hasScope('write')")
                    .antMatchers(HttpMethod.PUT   , "/**").access("#oauth2.hasScope('write')")
                    .antMatchers(HttpMethod.DELETE, "/**").access("#oauth2.hasScope('write')")
                .and()
                    .headers().addHeaderWriter((request, response) -> {
                        response.addHeader("Access-Control-Allow-Origin", "*");
                        if (request.getMethod().equals("OPTIONS")) {
                            response.setHeader("Access-Control-Allow-Methods", request.getHeader("Access-Control-Request-Method"));
                            response.setHeader("Access-Control-Allow-Headers", request.getHeader("Access-Control-Request-Headers"));
                        }
                    })
                .and()
                    .exceptionHandling()
                        .accessDeniedHandler(new OAuth2AccessDeniedHandler())
        ;
    }


}
