package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.domain.enums.DroneStatus;
import com.api.simulador_drones.dto.DroneDTO;
import com.api.simulador_drones.repository.DroneRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DroneServiceTest {

    @InjectMocks
    private DroneService droneService;

    @Mock
    private DroneRepository droneRepository;

    @Mock
    private LogisticaService logisticaService;

    @Test
    @DisplayName("Cria um drone e tenta alocar um pedido")
    void criarDrone() {
        DroneDTO droneDTO = new DroneDTO();
        droneDTO.setCapacidadeMaximaKg(15.0);
        droneDTO.setAutonomiaMaximaKm(100.0);

        Drone droneSalvo = new Drone(1L, 15.0, 100.0, 100.0, DroneStatus.IDLE, 0, 0);

        // Quando o método save for chamado com qualquer objeto Drone, deve retornar o "droneSalvo"
        when(droneRepository.save(any(Drone.class))).thenReturn(droneSalvo);

        Drone resultado = droneService.create(droneDTO);

        assertNotNull(resultado);
        assertEquals(15.0, resultado.getCapacidadeMaximaKg());

        verify(droneRepository, times(1)).save(any(Drone.class));
        verify(logisticaService, times(1)).alocarPedidosPendentes(droneSalvo);
    }

    @Test
    @DisplayName("Retornar drone a base")
    void deveRetornarDroneABaseComSucesso() {
        Long droneId = 1L;
        Drone drone = new Drone(droneId, 10.0, 50.0, 20.0, DroneStatus.RETORNANDO, 0, 0);

        when(droneRepository.findById(droneId)).thenReturn(Optional.of(drone));
        when(droneRepository.save(any(Drone.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Drone resultado = droneService.retornarDroneABase(droneId);

        assertEquals(DroneStatus.IDLE, resultado.getStatus());
        assertEquals(100.0, resultado.getBateriaAtual());
        verify(droneRepository, times(1)).save(drone);
    }

    @Test
    @DisplayName("Tentar finalizar um drone que não ta retornando")
    void finalizarDroneErrado() {

        Long droneId = 1L;
        Drone drone = new Drone(droneId, 10.0, 50.0, 20.0, DroneStatus.EM_VOO, 0, 0);
        when(droneRepository.findById(droneId)).thenReturn(Optional.of(drone));

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            droneService.retornarDroneABase(droneId);
        });

        assertEquals("O drone não está retornando para ser finalizado.", exception.getMessage());
        verify(droneRepository, never()).save(any(Drone.class));
    }

    @Test
    @DisplayName("Buscar drone que não existe")
    void buscarDroneErrado() {
        Long droneId = 99L;
        when(droneRepository.findById(droneId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            droneService.findById(droneId);
        });

        assertEquals("Drone não encontrado! Id: " + droneId, exception.getMessage());
    }
}