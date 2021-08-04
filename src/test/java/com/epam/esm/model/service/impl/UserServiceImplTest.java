package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.*;
import com.epam.esm.model.repository.*;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceImplTest {
    private User user;
    private Order firstOrder;
    private Order secondOrder;
    private User updatedUser;
    private Role role;
    private Status status;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private TagRepository tagRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private StatusRepository statusRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setFirstName("First name");
        user.setLastName("Last name");
        user.setUsername("Username");
        user.setEmail("user@gmail.com");
        user.setPassword("m5YQjQifDUhq9wj");
        role = new Role();
        role.setRoleType(Role.RoleType.USER);
        user.setRole(role);
        status = new Status();
        status.setStatusType(Status.StatusType.ACTIVE);
        user.setStatus(status);
        firstOrder = new Order();
        firstOrder.setId(1L);
        firstOrder.setCost(new BigDecimal("655.57"));
        firstOrder.setCreateDate(LocalDateTime.now());
        user.addOrder(firstOrder);
        secondOrder = new Order();
        secondOrder.setId(2L);
        secondOrder.setCost(new BigDecimal("700.00"));
        secondOrder.setCreateDate(LocalDateTime.now());
        updatedUser = new User();
        updatedUser.setId(user.getId());
        updatedUser.setFirstName(user.getFirstName());
        updatedUser.setLastName(user.getLastName());
        updatedUser.setUsername(user.getUsername());
        updatedUser.setEmail(user.getEmail());
        updatedUser.setPassword(user.getPassword());
        updatedUser.setRole(user.getRole());
        updatedUser.setStatus(user.getStatus());
        updatedUser.setOrders(user.getOrders());
        updatedUser.addOrder(secondOrder);
    }

    @AfterEach
    void tearDown() {
        user = null;
        firstOrder = null;
        secondOrder = null;
        updatedUser = null;
        role = null;
        status = null;
    }

    @Test
    void createUser() {
        String encodePassword = "m5YQjQifDUhq9wjencode";
        when(userRepository.save(user)).thenReturn(user);
        when(passwordEncoder.encode(user.getPassword())).thenReturn(encodePassword);
        when(roleRepository.findByRoleType(Role.RoleType.USER)).thenReturn(role);
        when(statusRepository.findByStatusType(Status.StatusType.ACTIVE)).thenReturn(status);
        user.setPassword(encodePassword);
        Optional<User> actual = userService.createUser(user);
        Optional<User> expect = Optional.of(user);
        assertEquals(expect, actual);
    }

    @Test
    void findUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        Optional<User> actual = userService.findUser(1L);
        Optional<User> expect = Optional.of(user);
        assertEquals(expect, actual);
    }

    @Test
    void addOrderToUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findById(2L)).thenReturn(Optional.of(secondOrder));
        when(userRepository.save(user)).thenReturn(updatedUser);
        Optional<User> actual = userService.addOrderToUser(1L, 2L);
        Optional<User> expect = Optional.of(updatedUser);
        assertEquals(expect, actual);
    }

    @Test
    void createOrderForUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.save(secondOrder)).thenReturn(secondOrder);
        when(userRepository.save(user)).thenReturn(updatedUser);
        Optional<Order> actual = userService.createOrderForUser(1L, secondOrder);
        Optional<Order> expect = Optional.of(secondOrder);
        assertEquals(expect, actual);
    }

    @Test
    void findUsers() {
        List<User> users = new ArrayList<>();
        users.add(user);
        Page<User> page = new PageImpl<>(users);
        when(userRepository.findAllByOrderByIdAsc(PageRequest.of(0, 5))).thenReturn(page);
        Page<User> actual = userService.findUsers(1, 5);
        assertEquals(page, actual);
    }

    @Test
    void findOrdersForUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        List<Order> orders = new ArrayList<>(user.getOrders());
        when(orderRepository.findAllByOrderByIdAsc(PageRequest.of(0, 5))).thenReturn(new PageImpl<>(orders));
        Page<Order> expect = new PageImpl<>(orders);
        Page<Order> actual = userService.findOrdersForUser(1L, 1, 5);
        assertEquals(expect, actual);
    }

    @Test
    void findOrderForUser() {
        when(userRepository.findById(1L)).thenReturn(Optional.of(user));
        when(orderRepository.findById(1L)).thenReturn(Optional.of(firstOrder));
        Optional<Order> actual = userService.findOrderForUser(1L, 1L);
        Optional<Order> expect = Optional.of(firstOrder);
        assertEquals(expect, actual);
    }

    @Test
    void findWidelyUsedTagForUserWithHighestCostOfAllOrders() {
        Tag tag = new Tag();
        tag.setId(1L);
        tag.setName("Tag name");
        when(userRepository.findUserWithMaxSumCostOrders()).thenReturn(Optional.of(user));
        when(tagRepository.findMaxCountTagByUserId(1L)).thenReturn(Optional.of(tag));
        Optional<Tag> actual = userService.findWidelyUsedTagForUserWithHighestCostOfAllOrders();
        Optional<Tag> expect = Optional.of(tag);
        assertEquals(expect, actual);
    }
}