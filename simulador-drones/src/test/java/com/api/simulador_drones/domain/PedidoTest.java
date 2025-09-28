package com.api.simulador_drones.domain;

import com.api.simulador_drones.domain.enums.PedidoStatus;
import com.api.simulador_drones.domain.enums.Prioridade;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PedidoTest {

    @Test
    @DisplayName("Deve criar um pedido corretamente")
    public void criarPedidoTest(){
        double peso = 8.0;
        Prioridade prioridade = Prioridade.MEDIA;
        int posX = 5;
        int posY = 10;

        Pedido pedido = new Pedido(peso, prioridade, posX, posY);

        assertNotNull(pedido);
        assertEquals(peso, pedido.getPesoKg());
        assertEquals(prioridade, pedido.getPrioridade());
        assertEquals(posX, pedido.getPosX());
        assertEquals(posY, pedido.getPosY());
        assertEquals(PedidoStatus.PENDENTE, pedido.getPedidoStatus());
        assertNull(pedido.getEntrega());
    }
}
