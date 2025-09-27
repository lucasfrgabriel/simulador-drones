package com.api.simulador_drones.domain;

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
public class Entrega implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private LocalDateTime inicioEntrega;
    private LocalDateTime fimEntrega;

    private Drone droneAssociado;
    private List<Pedido> pedidosEntrega = new ArrayList<>();

    private double pesoTotalKg;
    private double distanciaTotalKm;
}
