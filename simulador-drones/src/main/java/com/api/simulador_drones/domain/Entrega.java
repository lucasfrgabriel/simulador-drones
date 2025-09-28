package com.api.simulador_drones.domain;

import com.api.simulador_drones.domain.enums.EntregaStatus;
import com.api.simulador_drones.util.CalculadoraDistancia;
import jakarta.persistence.*;
import lombok.*;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Setter
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

    @ManyToOne
    @JoinColumn(name = "drone_id")
    private Drone droneAssociado;

    @OneToMany(mappedBy = "entrega", cascade = CascadeType.ALL)
    @OrderBy("prioridade DESC")
    private List<Pedido> pedidosEntrega = new ArrayList<>();

    private double pesoTotalKg;
    private double distanciaTotalKm;

    @Enumerated(EnumType.STRING)
    private EntregaStatus status = EntregaStatus.AGUARDANDO_PACOTES;

    /**
     * Construtor para criar uma entrega.
     * @param droneAssociado O drone que realizará a entrega.
     */
    public Entrega(Drone droneAssociado) {
        this.droneAssociado = droneAssociado;
    }

    /**
     * Adiciona um pedido a entrega e recalcula o peso total.
     * @param pedido O pedido a ser adicionado.
     */
    public void adicionarPedido(Pedido pedido) {
        this.pedidosEntrega.add(pedido);
        this.pedidosEntrega.sort((p1, p2) -> p2.getPrioridade().compareTo(p1.getPrioridade()));
        this.pesoTotalKg = this.pedidosEntrega.stream().mapToDouble(Pedido::getPesoKg).sum();
        this.distanciaTotalKm = CalculadoraDistancia.calcular(this.pedidosEntrega);
    }
}