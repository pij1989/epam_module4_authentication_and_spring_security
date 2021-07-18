package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.OrderItem;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.OrderItemRepository;
import com.epam.esm.model.repository.OrderRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@ActiveProfiles("test")
class OrderServiceImplTest {
    private Order order;
    private GiftCertificate giftCertificate;
    private OrderItem orderItem;

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @Mock
    private GiftCertificateRepository giftCertificateRepository;

    @InjectMocks
    private OrderServiceImpl orderService;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setCost(new BigDecimal("655.57"));
        order.setCreateDate(LocalDateTime.now());
        giftCertificate = new GiftCertificate();
        giftCertificate.setId(1L);
        giftCertificate.setName("New gift certificate name");
        giftCertificate.setDescription("New gift certificate description");
        giftCertificate.setPrice(new BigDecimal("55.77"));
        giftCertificate.setDuration(30);
        giftCertificate.setCreateDate(LocalDateTime.now());
        giftCertificate.setLastUpdateDate(LocalDateTime.now());
        orderItem = new OrderItem();
        orderItem.setId(1L);
        orderItem.setQuantity(10);
        orderItem.setOrder(order);
        orderItem.setGiftCertificate(giftCertificate);
    }

    @AfterEach
    void tearDown() {
        order = null;
        giftCertificate = null;
        orderItem = null;
    }

    @Test
    void createOrder() {
        when(orderRepository.save(order)).thenReturn(order);
        Optional<Order> actual = orderService.createOrder(order);
        Optional<Order> expect = Optional.of(order);
        assertEquals(expect, actual);
    }

    @Test
    void findOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        Optional<Order> actual = orderService.findOrder(1L);
        Optional<Order> expect = Optional.of(order);
        assertEquals(expect, actual);
    }

    @Test
    void addGiftCertificateToOrder() {
        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));
        when(giftCertificateRepository.findById(1L)).thenReturn(Optional.of(giftCertificate));
        when(orderItemRepository.save(orderItem)).thenReturn(orderItem);
        Optional<OrderItem> actual = orderService.addGiftCertificateToOrder(1L, 1L, orderItem);
        Optional<OrderItem> expect = Optional.of(orderItem);
        assertEquals(expect, actual);
    }
}