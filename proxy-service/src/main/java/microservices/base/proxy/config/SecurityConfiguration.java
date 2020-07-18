package microservices.base.proxy.config;

import microservices.base.proxy.domain.Authority;
import microservices.base.proxy.security.PreZuulFilter;
import microservices.base.proxy.security.jwt.JWTConfigurer;
import microservices.base.proxy.security.jwt.TokenProvider;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.servlet.HandlerExceptionResolver;

import javax.annotation.PostConstruct;

/**
 * Created by antonio on 17/07/2020.
 */

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true, securedEnabled = true)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {


    private final AuthenticationManagerBuilder authenticationManagerBuilder;

    private final UserDetailsService userDetailsService;

    //@Qualifier("handlerExceptionResolver")
    private final HandlerExceptionResolver resolver;


    public SecurityConfiguration(AuthenticationManagerBuilder authenticationManagerBuilder, UserDetailsService userDetailsService, HandlerExceptionResolver resolver) {
        this.authenticationManagerBuilder = authenticationManagerBuilder;
        this.userDetailsService = userDetailsService;
        this.resolver = resolver;
    }


    @PostConstruct
    public void postConstruct() throws Exception {
        authenticationManagerBuilder
                .userDetailsService(userDetailsService)
                .passwordEncoder(passwordEncoder())
        ;
    }

    /**
     * Define password encoder bean
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public TokenProvider tokenProvider() {
        return new TokenProvider();
    }

    @Bean
    public JWTConfigurer jwtConfigurer() {
        return new JWTConfigurer(tokenProvider());
    }

    @Bean
    public PreZuulFilter preZuulFilter(){
        return new PreZuulFilter();
    }

//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//
//        source.registerCorsConfiguration("/api/**", config);
//        source.registerCorsConfiguration("/v2/api-docs", config);
//
//        return new CorsFilter(request -> { C});
//    }


    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring()
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/i18n/**")
                .antMatchers("/content/**")
                .antMatchers("/swagger-ui/index.html")
                .antMatchers("/api-docs/**/*")
                .antMatchers("/api/account/reset-password/init")
                .antMatchers("/api/account/reset-password/finish")
                .antMatchers("/api/account/register")
                .antMatchers("/api/account/activate")
                .antMatchers("/test/**")
        ;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .exceptionHandling()
                .authenticationEntryPoint( (request, response, e) ->
                    resolver.resolveException(request,response, null, e)
                 )
                .accessDeniedHandler( (request, response, e) ->
                    resolver.resolveException( request, response, null, e)
                )
        .and()
            .cors().and().csrf().disable()
            .headers().frameOptions().disable()
        .and()
            .sessionManagement()
            .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
        .and()
            .authorizeRequests()
            .antMatchers("/api/token/generate-token").permitAll()
            .antMatchers("/api/account/register").permitAll()
            .antMatchers("/api/account/activate").permitAll()
            .antMatchers("/api/account/reset-password/init").permitAll()
            .antMatchers("/api/account/reset-password/finish").permitAll()
            .antMatchers("/api/profile-info").permitAll()
            .antMatchers("/api/**").authenticated()
            .antMatchers("/management/health").permitAll()
            .antMatchers("/management/**").hasAuthority(Authority.RoleEnum.ROLE_ADMIN.name())
            .antMatchers("/v2/api-docs/**").permitAll()
            .antMatchers("/swagger-resources/configuration/ui").permitAll()
            .antMatchers("/swagger-ui/index.html").hasAuthority(Authority.RoleEnum.ROLE_ADMIN.name())
            .antMatchers("/api-docs/**").permitAll()
        .and()
            .apply(jwtConfigurer())

        ;
    }
}
