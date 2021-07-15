package com.epam.esm.controller;

import com.epam.esm.model.entity.User;
import com.epam.esm.model.exception.BadRequestException;
import com.epam.esm.model.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Optional;

@RestController
public class RegistrationController {
    private static final Logger logger = LoggerFactory.getLogger(RegistrationController.class);
    private static final String EXISTED_USER = "error.400.user.username";
    private final UserService userService;

    @Autowired
    public RegistrationController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUpUser(@RequestBody User user) throws BadRequestException {
        logger.debug("USER: " + user);
        if (userService.existsByUsername(user.getUsername())) {
            throw new BadRequestException(EXISTED_USER, new Object[]{user.getUsername()});
        }
        Optional<User> optionalUser = userService.signUpUser(user);
        return optionalUser.map(value -> ResponseEntity.created(URI.create("http://localhost:8081/users/" + value.getId()))
                .build())
                .orElseGet(() -> new ResponseEntity<>(HttpStatus.BAD_REQUEST));
    }
}
