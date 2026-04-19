package com.pdsa.backend.trafficgame.repositories;

import com.pdsa.backend.trafficgame.entity.TrafficEdge;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrafficEdgeRepository extends JpaRepository<TrafficEdge, Long> {
}
