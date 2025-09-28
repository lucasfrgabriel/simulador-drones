package com.api.simulador_drones.domain;

import com.api.simulador_drones.domain.enums.PedidoStatus;
import com.api.simulador_drones.domain.enums.Prioridade;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
@Entity
@Table(name = "pedidos")
public class Pedido implements Serializable {

    @Serial
    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private double pesoKg;

    @Enumerated(EnumType.STRING)
    private Prioridade prioridade;

    private int posX;
    private int posY;

    @Enumerated(EnumType.STRING)
    private PedidoStatus pedidoStatus = PedidoStatus.PENDENTE;

    @ManyToOne
    @JoinColumn(name = "entrega_id")
    private Entrega entrega;

    public Pedido(double pesoKg, Prioridade prioridade, int posX, int posY) {
        this.pesoKg = pesoKg;
        this.prioridade = prioridade;
        this.posX = posX;
        this.posY = posY;
    }
}