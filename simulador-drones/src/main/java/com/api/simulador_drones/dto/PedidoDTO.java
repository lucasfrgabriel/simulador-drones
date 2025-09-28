package com.api.simulador_drones.dto;

import com.api.simulador_drones.domain.enums.Prioridade;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PedidoDTO {
    private double pesoKg;
    private Prioridade prioridade;
    private int posX;
    private int posY;
}