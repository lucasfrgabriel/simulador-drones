package com.api.simulador_drones.repository;

import com.api.simulador_drones.domain.Entrega;

import com.api.simulador_drones.domain.enums.DroneStatus;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface EntregaRepository extends JpaRepository<Entrega, Long> {

    @Query("SELECT e FROM Entrega e WHERE e.droneAssociado.status = :status")
    List<Entrega> findByDroneStatus(@Param("status") DroneStatus status);
}
