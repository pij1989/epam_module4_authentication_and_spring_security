package com.epam.esm.model.repository;

import com.epam.esm.model.entity.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status,Long> {
    Status findByStatusType(Status.StatusType statusType);
}
