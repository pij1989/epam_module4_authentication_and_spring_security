package com.epam.esm.model.repository.impl;

import com.epam.esm.model.entity.User;
import com.epam.esm.model.repository.UserRepositoryCustom;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.Optional;

public class UserRepositoryCustomImpl implements UserRepositoryCustom {
    private static final String FIND_USER_WITH_MAX_SUM_COST_ORDERS_JPQL = "SELECT u FROM users u JOIN u.orders o GROUP BY u.id ORDER BY sum(o.cost) DESC";

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public Optional<User> findUserWithMaxSumCostOrders() {
        User user = entityManager.createQuery(FIND_USER_WITH_MAX_SUM_COST_ORDERS_JPQL, User.class)
                .setMaxResults(1)
                .getSingleResult();
        return Optional.ofNullable(user);
    }
}
