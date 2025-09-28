package com.api.simulador_drones.domain;

import com.api.simulador_drones.domain.enums.DroneStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class DroneTest {

    @Test
    @DisplayName("Deve instanciar corretamente o drone")
    void instanciarDroneTest() {
        double autonomiaKm = 50.0;
        double capacidadeKg = 20;
        Drone drone = new Drone(capacidadeKg, autonomiaKm);

        assertNotNull(drone);
        assertEquals(capacidadeKg, drone.getCapacidadeMaximaKg());
        assertEquals(autonomiaKm, drone.getAutonomiaMaximaKm());
        assertEquals(100, drone.getBateriaAtual());
        assertEquals(DroneStatus.IDLE, drone.getStatus());
    }
}
