package com.ead.serviceregistry.configs;

import ch.qos.logback.core.encoder.Encoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Value(value = "${ead.serviceRegistry.username}")
    private String username;

    @Value(value = "${ead.serviceRegistry.password}")
    private String password;

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .httpBasic()
                .and()
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .formLogin();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManageBuilder) throws Exception{
        authManageBuilder.inMemoryAuthentication()
                .withUser( username )
                .password( passwordEncoder().encode( password ) )
                .roles( "ADMIN" );
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
