package com.epam.esm.model.repository;

import com.epam.esm.model.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {
    Optional<User> findUserWithMaxSumCostOrders();

    Optional<User> findByUsername(String username);

    Page<User> findAllByOrderByIdAsc(Pageable pageable);
}
