package com.epam.esm.model.service.impl;

import com.epam.esm.model.entity.GiftCertificate;
import com.epam.esm.model.entity.Order;
import com.epam.esm.model.entity.OrderItem;
import com.epam.esm.model.repository.GiftCertificateRepository;
import com.epam.esm.model.repository.OrderItemRepository;
import com.epam.esm.model.repository.OrderRepository;
import com.epam.esm.model.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final GiftCertificateRepository giftCertificateRepository;

    @Autowired
    public OrderServiceImpl(OrderRepository orderRepository, OrderItemRepository orderItemRepository, GiftCertificateRepository giftCertificateRepository) {
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.giftCertificateRepository = giftCertificateRepository;
    }

    @Override
    @Transactional
    public Optional<Order> createOrder(Order order) {
        if (order != null) {
            Order createdOrder = orderRepository.save(order);
            return Optional.of(createdOrder);
        }
        return Optional.empty();
    }

    @Override
    public Optional<Order> findOrder(Long id) {
        return orderRepository.findById(id);
    }

    @Override
    @Transactional
    public Optional<OrderItem> addGiftCertificateToOrder(Long orderId, Long certificateId, OrderItem orderItem) {
        Optional<Order> optionalOrder = orderRepository.findById(orderId);
        if (optionalOrder.isPresent()) {
            Optional<GiftCertificate> optionalGiftCertificate = giftCertificateRepository.findById(certificateId);
            if (optionalGiftCertificate.isPresent()) {
                Order order = optionalOrder.get();
                GiftCertificate giftCertificate = optionalGiftCertificate.get();
                BigDecimal calculatedCost = calculateCost(giftCertificate.getPrice(), order.getCost(), orderItem.getQuantity());
                order.setCost(calculatedCost);
                orderItem.setOrder(order);
                orderItem.addGiftCertificate(giftCertificate);
                return Optional.of(orderItemRepository.save(orderItem));
            }
        }
        return Optional.empty();
    }

    private BigDecimal calculateCost(BigDecimal price, BigDecimal totalCost, int quantity) {
        if (totalCost == null) {
            totalCost = BigDecimal.ZERO;
        }
        BigDecimal itemCost = price.multiply(BigDecimal.valueOf(quantity));
        return totalCost.add(itemCost);
    }
}
