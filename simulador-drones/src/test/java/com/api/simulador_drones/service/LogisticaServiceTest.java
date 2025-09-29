package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.domain.Entrega;
import com.api.simulador_drones.domain.Pedido;
import com.api.simulador_drones.domain.enums.DroneStatus;
import com.api.simulador_drones.domain.enums.PedidoStatus;
import com.api.simulador_drones.domain.enums.Prioridade;
import com.api.simulador_drones.repository.DroneRepository;
import com.api.simulador_drones.repository.EntregaRepository;
import com.api.simulador_drones.repository.PedidoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LogisticaServiceTest {

    @InjectMocks
    private LogisticaService logisticaService;

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private EntregaRepository entregaRepository;

    @Test
    @DisplayName("Novo pedido sem entrega disponivel, cria nova e aloca pedido")
    void alocarPedidoSemEntrega(){
        Pedido pedido = new Pedido(1L, 5.0, Prioridade.MEDIA, 3, 4, PedidoStatus.PENDENTE, null);
        Drone droneOcioso = new Drone(1L, 10.0, 50.0, 100.0, DroneStatus.IDLE, 0, 0);

        when(entregaRepository.findByDroneStatus(DroneStatus.CARREGANDO)).thenReturn(Collections.emptyList());
        when(droneRepository.findByStatus(DroneStatus.IDLE)).thenReturn(List.of(droneOcioso));

        logisticaService.alocarNovoPedido(pedido);

        verify(entregaRepository, times(1)).save(any(Entrega.class));
        verify(pedidoRepository, times(1)).save(pedido);
        verify(droneRepository, times(1)).save(droneOcioso);
    }

    @Test
    @DisplayName("Novo pedido com entrega disponivel, aloca pedido")
    void alocarPedidoComEntrega(){
        Pedido pedidoExistente = new Pedido(1L, 3.0, Prioridade.MEDIA, 1, 1, PedidoStatus.ALOCADO, null);
        Pedido novoPedido = new Pedido(2L, 4.0, Prioridade.BAIXA, 3, 4, PedidoStatus.PENDENTE, null);

        Drone droneEmCarregamento = new Drone(1L, 10.0, 50.0, 100.0, DroneStatus.CARREGANDO, 0, 0);

        Entrega entregaExistente = new Entrega(droneEmCarregamento);
        entregaExistente.adicionarPedido(pedidoExistente);

        when(entregaRepository.findByDroneStatus(DroneStatus.CARREGANDO)).thenReturn(List.of(entregaExistente));

        logisticaService.alocarNovoPedido(novoPedido);

        verify(entregaRepository, times(1)).save(entregaExistente);
        verify(pedidoRepository, times(1)).save(novoPedido);
        verify(droneRepository, never()).save(any(Drone.class));
    }

    @Test
    @DisplayName("Novo pedido sem drones disponiveis")
    void alocarPedidoSemDronesDisponiveis(){
        Pedido pedidoPesado = new Pedido(1L, 15.0, Prioridade.MEDIA, 3, 4, PedidoStatus.PENDENTE, null); // 15kg
        Drone dronePequeno = new Drone(1L, 10.0, 50.0, 100.0, DroneStatus.IDLE, 0, 0); // Capacidade 10kg

        when(entregaRepository.findByDroneStatus(DroneStatus.CARREGANDO)).thenReturn(Collections.emptyList());
        when(droneRepository.findByStatus(DroneStatus.IDLE)).thenReturn(List.of(dronePequeno));

        logisticaService.alocarNovoPedido(pedidoPesado);

        verify(entregaRepository, never()).save(any(Entrega.class));
        verify(pedidoRepository, never()).save(any(Pedido.class));
        verify(droneRepository, never()).save(any(Drone.class));
    }

    @Test
    @DisplayName("Cria um novo drone e aloca pedidos pendentes a ele")
    void alocarPedidosPendentesTest(){
        Pedido pedido1 = new Pedido(1L, 5.0, Prioridade.MEDIA, 3, 4, PedidoStatus.PENDENTE, null);
        Pedido pedido2 = new Pedido(2L, 2.0, Prioridade.BAIXA, 1, 2, PedidoStatus.PENDENTE, null);
        Drone novoDrone = new Drone(1L, 10.0, 50.0, 100.0, DroneStatus.IDLE, 0, 0);

        when(pedidoRepository.findByPedidoStatus(PedidoStatus.PENDENTE)).thenReturn(List.of(pedido1, pedido2));

        logisticaService.alocarPedidosPendentes(novoDrone);

        verify(entregaRepository, times(2)).save(any(Entrega.class));
        verify(pedidoRepository, times(2)).save(any(Pedido.class));
    }
}
