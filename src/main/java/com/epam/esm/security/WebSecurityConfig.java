package com.epam.esm.security;

import com.epam.esm.model.entity.Role;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.authority.AuthorityUtils;

@Configuration
@EnableWebSecurity(debug = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    private static final String ROLE = "ROLE_";

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.anonymous().authorities(AuthorityUtils.createAuthorityList(ROLE + Role.RoleType.GUEST));
        http.authorizeRequests().antMatchers(HttpMethod.GET, "/tags/**", "/gift_certificates/**").hasRole(Role.RoleType.GUEST.toString())
                .antMatchers(HttpMethod.POST, "/users/signup", "users/login").hasRole(Role.RoleType.GUEST.toString())
                .anyRequest().authenticated().and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .csrf().disable();
    }
}
