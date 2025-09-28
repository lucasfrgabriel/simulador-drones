package com.api.simulador_drones.repository;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.domain.enums.DroneStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DroneRepository extends JpaRepository<Drone, Long> {

    List<Drone> findByStatus(DroneStatus status);
}