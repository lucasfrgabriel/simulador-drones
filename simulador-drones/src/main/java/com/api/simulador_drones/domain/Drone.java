package com.api.simulador_drones.domain;

import com.api.simulador_drones.domain.enums.DroneStatus;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Drone implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private double capacidadeMaximaKg;
    private double autonomiaMaximaKm;
    private double bateriaAtual = 100.00;

    private DroneStatus status = DroneStatus.IDLE;

    private int posX = 0;
    private int posY = 0;
}