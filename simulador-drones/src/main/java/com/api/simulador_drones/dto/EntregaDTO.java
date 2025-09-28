package com.api.simulador_drones.dto;

import com.api.simulador_drones.domain.Pedido;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Setter
@Getter
public class EntregaDTO {
    private Long entregaId;
    private Long droneId;
    private List<Pedido> pedidos;
    private double distanciaTotalKm;
    private boolean autonomiaSuficiente;

    public EntregaDTO(Long entregaId, Long droneId, List<Pedido> pedidos, double distanciaTotalKm, boolean autonomiaSuficiente) {
        this.entregaId = entregaId;
        this.droneId = droneId;
        this.pedidos = pedidos;
        this.distanciaTotalKm = distanciaTotalKm;
        this.autonomiaSuficiente = autonomiaSuficiente;
    }
}
