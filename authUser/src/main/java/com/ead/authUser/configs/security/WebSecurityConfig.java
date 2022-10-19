package com.ead.authUser.configs.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import javax.servlet.Filter;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailService;

    @Autowired
    private AuthenticationEntryPointImpl authenticationEntryPoint;

    private static final String[] AUTH_WHITELIST ={
      "/auth/**"
    };

    @Override
    protected void configure(HttpSecurity httpSecurity) throws Exception{
        httpSecurity
                .exceptionHandling().authenticationEntryPoint( authenticationEntryPoint )
                .and()
                .sessionManagement().sessionCreationPolicy( SessionCreationPolicy.STATELESS )
                .and()
                .authorizeRequests()
                .antMatchers(AUTH_WHITELIST).permitAll()
                .anyRequest().authenticated()
                .and()
                .csrf().disable();
        httpSecurity.addFilterBefore( authenticationJwtFilter(), UsernamePasswordAuthenticationFilter.class);
    }

    @Bean
    public AuthenticationJwtFilter authenticationJwtFilter() {
        return new AuthenticationJwtFilter();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder authManageBuilder) throws Exception{
        authManageBuilder.userDetailsService( userDetailService )
                .passwordEncoder( passwordEncoder() );
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception{
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
