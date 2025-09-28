package com.api.simulador_drones.domain;

import com.api.simulador_drones.domain.enums.EntregaStatus;
import com.api.simulador_drones.domain.enums.Prioridade;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class EntregaTest {

    Drone drone;
    Pedido pedido;
    Entrega entrega;

    @BeforeEach
    void setUp() {
        drone = new Drone(20.0, 50.0);
        pedido = new Pedido(8, Prioridade.MEDIA, 5, 10);
        entrega = new Entrega(drone);
    }

    @Test
    @DisplayName("Deve criar uma entrega e associar um drone")
    void criarEntrega(){
        assertNotNull(entrega);
        assertEquals(EntregaStatus.AGUARDANDO_PACOTES, entrega.getStatus());
        assertNull(entrega.getInicioEntrega());
        assertNull(entrega.getFimEntrega());
        assertTrue(entrega.getPedidosEntrega().isEmpty());
        assertEquals(0, entrega.getPesoTotalKg());
        assertEquals(0, entrega.getDistanciaTotalKm());
    }

    @Test
    @DisplayName("Deve associar um pedido a entrega e recalcular o peso e distancia total")
    void associarPedido(){
        double distancia = 2*Math.sqrt(Math.pow(pedido.getPosX(), 2) + Math.pow(pedido.getPosY(), 2));

        entrega.adicionarPedido(pedido);

        assertEquals(pedido, entrega.getPedidosEntrega().getFirst());
        assertEquals(pedido.getPesoKg(), entrega.getPesoTotalKg());
        assertEquals(distancia, entrega.getDistanciaTotalKm());
    }

    @Test
    @DisplayName("Deve ordenar pedidos por ordem de prioridade")
    void ordenarPedido(){
        Pedido pedido1 = new Pedido(8, Prioridade.BAIXA, 5, 10);
        Pedido pedido2 = new Pedido(8, Prioridade.MEDIA, 5, 10);
        Pedido  pedido3 = new Pedido(8, Prioridade.ALTA, 5, 10);

        entrega.adicionarPedido(pedido1);
        entrega.adicionarPedido(pedido2);
        entrega.adicionarPedido(pedido3);

        assertEquals(Prioridade.ALTA, entrega.getPedidosEntrega().get(0).getPrioridade());
        assertEquals(Prioridade.MEDIA, entrega.getPedidosEntrega().get(1).getPrioridade());
        assertEquals(Prioridade.BAIXA, entrega.getPedidosEntrega().get(2).getPrioridade());
    }
}