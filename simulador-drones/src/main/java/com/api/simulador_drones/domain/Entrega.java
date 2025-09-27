package com.api.simulador_drones.domain;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "entregas")
public class Entrega implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime inicioEntrega;
    private LocalDateTime fimEntrega;

    @Setter
    @ManyToOne
    @JoinColumn(name = "drone_id")
    private Drone droneAssociado;

    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL)
    private List<Pedido> pedidosEntrega = new ArrayList<>();

    private double pesoTotalKg;
    private double distanciaTotalKm;
}
