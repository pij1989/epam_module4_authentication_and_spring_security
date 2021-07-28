package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.*;
import com.epam.esm.model.repository.*;
import com.epam.esm.model.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final TagRepository tagRepository;
    private final RoleRepository roleRepository;
    private final StatusRepository statusRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserServiceImpl(UserRepository userRepository, OrderRepository orderRepository, TagRepository tagRepository,
                           RoleRepository roleRepository, StatusRepository statusRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.orderRepository = orderRepository;
        this.tagRepository = tagRepository;
        this.roleRepository = roleRepository;
        this.statusRepository = statusRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public Optional<User> createUser(User user) {
        if (user != null) {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            Role role = roleRepository.findByRoleType(user.getRole().getRoleType());
            Status status = statusRepository.findByStatusType(user.getStatus().getStatusType());
            user.setRole(role);
            user.setStatus(status);
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    @Override
    public Optional<User> findUser(Long id) {
        return userRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<User> addOrderToUser(Long userId, Long orderId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            Optional<Order> optionalOrder = orderRepository.findById(orderId);
            if (optionalOrder.isPresent()) {
                User user = optionalUser.get();
                Order order = optionalOrder.get();
                user.addOrder(order);
                return Optional.of(userRepository.save(user));
            }
        }
        return Optional.empty();
    }

    @Override
    @Transactional
    public Optional<Order> createOrderForUser(Long userId, Order order) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            Order createdOrder = orderRepository.save(order);
            user.addOrder(createdOrder);
            userRepository.save(user);
            return Optional.of(createdOrder);
        }
        return Optional.empty();
    }

    @Override
    public Page<User> findUsers(int page, int size) {
        if (page == 0 || size == 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        return userRepository.findAllByOrderByIdAsc(PageRequest.of(page - 1, size));
    }

    @Override
    public Page<Order> findOrdersForUser(Long userId, int page, int size) {
        if (page == 0 || size == 0) {
            return new PageImpl<>(Collections.emptyList());
        }
        Page<Order> createdPage = null;
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            createdPage = orderRepository.findAllByOrderByIdAsc(PageRequest.of(page - 1, size));
        }
        return createdPage;
    }

    @Override
    public Optional<Order> findOrderForUser(Long userId, Long orderId) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if (optionalUser.isPresent()) {
            return orderRepository.findById(orderId);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Tag> findWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        Optional<User> optionalUser = userRepository.findUserWithMaxSumCostOrders();
        return optionalUser.map(User::getId)
                .flatMap(tagRepository::findMaxCountTagByUserId);
    }

    @Override
    @Transactional
    public Optional<User> signUpUser(User user) {
        if (user != null) {
            String password = user.getPassword();
            user.setPassword(passwordEncoder.encode(password));
            Role role = roleRepository.findByRoleType(Role.RoleType.USER);
            Status status = statusRepository.findByStatusType(Status.StatusType.ACTIVE);
            user.setRole(role);
            user.setStatus(status);
            return Optional.of(userRepository.save(user));
        }
        return Optional.empty();
    }

    @Override
    public boolean existsByUsername(String username) {
        Optional<User> optionalUser = userRepository.findByUsername(username);
        return optionalUser.isPresent();
    }
}
