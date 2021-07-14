package com.epam.esm.model.repository;

import com.epam.esm.model.entity.User;

import java.util.Optional;

public interface UserRepositoryCustom {
    Optional<User> findUserWithMaxSumCostOrders();
}
