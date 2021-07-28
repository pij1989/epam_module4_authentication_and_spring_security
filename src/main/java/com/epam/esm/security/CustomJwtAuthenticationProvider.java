package com.epam.esm.security;

import com.epam.esm.util.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.server.resource.BearerTokenAuthenticationToken;
import org.springframework.stereotype.Component;

@Profile("oauth2")
@Component
public class CustomJwtAuthenticationProvider implements AuthenticationProvider {
    private final JwtTokenUtil jwtTokenUtil;
    private final UserDetailsService userDetailsService;

    @Autowired
    public CustomJwtAuthenticationProvider(JwtTokenUtil jwtTokenUtil, UserDetailsService userDetailsServiceImpl) {
        this.jwtTokenUtil = jwtTokenUtil;
        this.userDetailsService = userDetailsServiceImpl;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        BearerTokenAuthenticationToken bearerTokenAuthenticationToken = (BearerTokenAuthenticationToken) authentication;
        String token = bearerTokenAuthenticationToken.getToken();
        if (jwtTokenUtil.validate(token)) {
            String username = jwtTokenUtil.getUsernameFromToken(token);
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(userDetails,
                    null, userDetails.getAuthorities()
            );

            usernamePasswordAuthenticationToken.setDetails(authentication.getDetails());
            return usernamePasswordAuthenticationToken;
        }
        return null;
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return BearerTokenAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
