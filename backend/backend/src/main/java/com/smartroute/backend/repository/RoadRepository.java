package com.smartroute.backend.repository;

import com.smartroute.backend.model.Road;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoadRepository
        extends JpaRepository<Road, Long> {
}