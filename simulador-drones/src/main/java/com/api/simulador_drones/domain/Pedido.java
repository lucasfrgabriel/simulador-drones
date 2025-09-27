package com.api.simulador_drones.domain;

import com.api.simulador_drones.domain.enums.PedidoStatus;
import com.api.simulador_drones.domain.enums.Prioridade;

import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class Pedido implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    private Long id;

    private double pesoKg;
    private Prioridade prioridade;
    private int posX;
    private int posY;
    private PedidoStatus pedidoStatus = PedidoStatus.PENDENTE;
}