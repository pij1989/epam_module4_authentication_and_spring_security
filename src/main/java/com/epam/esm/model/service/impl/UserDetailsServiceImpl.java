package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

import static org.springframework.security.core.authority.AuthorityUtils.createAuthorityList;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    private static final String ROLE = "ROLE_";
    private final UserRepository userRepository;

    @Autowired
    public UserDetailsServiceImpl(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.map(user -> new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(),
                createAuthorityList(ROLE + user.getRole().getRoleType())))
                .orElseThrow(() -> new UsernameNotFoundException(username));
    }
}
