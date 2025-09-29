package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.domain.Entrega;
import com.api.simulador_drones.domain.Pedido;
import com.api.simulador_drones.domain.enums.DroneStatus;
import com.api.simulador_drones.domain.enums.EntregaStatus;
import com.api.simulador_drones.domain.enums.PedidoStatus;
import com.api.simulador_drones.domain.enums.Prioridade;
import com.api.simulador_drones.dto.EntregaDTO;
import com.api.simulador_drones.repository.DroneRepository;
import com.api.simulador_drones.repository.EntregaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class EntregaServiceTest {

    @InjectMocks
    private EntregaService entregaService;

    @Mock
    private EntregaRepository entregaRepository;

    @Mock
    private DroneRepository droneRepository;

    private Drone drone;
    private Entrega entrega;
    private Pedido pedido1;

    @BeforeEach
    void setUp() {
        drone = new Drone(1L, 20.0, 100.0, 100.0, DroneStatus.CARREGANDO, 0, 0);
        entrega = new Entrega(drone);
        entrega.setId(1L);
        pedido1 = new Pedido(1L, 5.0, Prioridade.ALTA, 3, 4, PedidoStatus.ALOCADO, entrega);
        entrega.adicionarPedido(pedido1);
    }

    @Test
    @DisplayName("Calcula a distancia total de uma entrega")
    void calcularRotaTest(){
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));

        EntregaDTO rota = entregaService.calcularRota(1L);

        assertEquals(10.0, rota.getDistanciaTotalKm());
        assertTrue(rota.isAutonomiaSuficiente());
    }

    @Test
    @DisplayName("Iniciar entrega")
    void iniciarEntregaTest(){
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        when(droneRepository.save(drone)).thenReturn(drone);
        when(entregaRepository.save(entrega)).thenReturn(entrega);

        Entrega entregaIniciada = entregaService.iniciarEntrega(entrega.getId());

        assertEquals(EntregaStatus.EM_CURSO, entregaIniciada.getStatus());
        assertEquals(DroneStatus.EM_VOO, drone.getStatus());
        assertEquals(PedidoStatus.A_CAMINHO, pedido1.getPedidoStatus());
        assertNotNull(entregaIniciada.getStatus());
        verify(droneRepository, times(1)).save(drone);
        verify(entregaRepository, times(1)).save(entrega);
    }

    @Test
    @DisplayName("Finalizar entrega")
    void finalizarEntregaTest(){
        entrega.setStatus(EntregaStatus.EM_CURSO);
        drone.setStatus(DroneStatus.EM_VOO);
        pedido1.setPedidoStatus(PedidoStatus.A_CAMINHO);
        entrega.setInicioEntrega(LocalDateTime.now());

        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));
        when(droneRepository.save(drone)).thenReturn(drone);
        when(entregaRepository.save(entrega)).thenReturn(entrega);

        Entrega entregaFinalizada = entregaService.finalizarEntrega(entrega.getId());

        assertNotNull(entregaFinalizada.getFimEntrega());
        assertEquals(EntregaStatus.FINALIZADA, entregaFinalizada.getStatus());
        assertEquals(DroneStatus.RETORNANDO, drone.getStatus());
        assertEquals(PedidoStatus.ENTREGUE, pedido1.getPedidoStatus());
        assertEquals(95.0, drone.getBateriaAtual());
        verify(droneRepository, times(1)).save(drone);
        verify(entregaRepository, times(1)).save(entrega);
    }

    @Test
    @DisplayName("Tentar finalizar uma entrega que nao foi iniciada")
    void finalizarEntregaErrada() {
        when(entregaRepository.findById(1L)).thenReturn(Optional.of(entrega));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            entregaService.finalizarEntrega(1L);
        });

        assertEquals("A entrega não está 'EM_CURSO', por isso não pode ser finalizada.", exception.getMessage());
    }
}