package com.api.simulador_drones.domain;

import com.api.simulador_drones.domain.enums.DroneStatus;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "drones")
public class Drone implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double capacidadeMaximaKg;
    private double autonomiaMaximaKm;

    @Setter
    private double bateriaAtual = 100.00;

    @Setter
    @Enumerated(EnumType.STRING)
    private DroneStatus status = DroneStatus.IDLE;

    private int posX = 0;
    private int posY = 0;

    public Drone(double capacidadeMaximaKg, double autonomiaMaximaKm) {
        this.capacidadeMaximaKg = capacidadeMaximaKg;
        this.autonomiaMaximaKm = autonomiaMaximaKm;
    }
}