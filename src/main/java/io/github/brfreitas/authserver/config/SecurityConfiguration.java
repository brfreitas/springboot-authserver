/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package io.github.brfreitas.authserver.config;

import java.util.Arrays;

import io.github.brfreitas.authserver.jwt.JWTAuthenticationFilter;
import io.github.brfreitas.authserver.jwt.JWTLoginFilter;
import io.github.brfreitas.authserver.jwt.TokenAuthenticationService;
import io.github.brfreitas.authserver.repository.AppRepository;
import io.github.brfreitas.authserver.repository.TokenRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

/**
 *
 * @author Bruno Freitas
 */
@Slf4j
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    @Autowired
    private TokenAuthenticationService tokenService;

    @Autowired
    private AppRepository appRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    protected AuthenticationManager authenticationManager() throws Exception {
        return new ProviderManager(Arrays.asList((AuthenticationProviderImpl) new AuthenticationProviderImpl(appRepository, tokenRepository)));
    }

    @Bean
    public FilterRegistrationBean corsFilter() {
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        CorsConfiguration config = new CorsConfiguration();
        config.setAllowCredentials(true);
        config.addAllowedOrigin("*");
        config.addAllowedHeader("*");
        config.addAllowedMethod("*");
        source.registerCorsConfiguration("/**", config);
        FilterRegistrationBean bean = new FilterRegistrationBean(new CorsFilter(source));
        bean.setOrder(0);
        return bean;
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.headers().cacheControl();

        http.csrf().disable()
                .authorizeRequests()
                .antMatchers("/token/valid").permitAll()
                .anyRequest().authenticated()
                .and()
                .httpBasic().disable()
                .addFilterBefore(new JWTLoginFilter("/auth", authenticationManager(), tokenService), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new JWTAuthenticationFilter(tokenService), UsernamePasswordAuthenticationFilter.class);

    }

}
