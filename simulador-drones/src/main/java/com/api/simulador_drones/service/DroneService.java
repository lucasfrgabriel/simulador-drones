package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.domain.Pedido;
import com.api.simulador_drones.domain.enums.DroneStatus;
import com.api.simulador_drones.dto.DroneDTO;
import com.api.simulador_drones.dto.PedidoDTO;
import com.api.simulador_drones.repository.DroneRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private LogisticaService logisticaService;

    @Transactional
    public Drone create(DroneDTO droneDTO) {
        Drone drone = new Drone(
                droneDTO.getCapacidadeMaximaKg(),
                droneDTO.getAutonomiaMaximaKm()
        );

        Drone novoDrone = droneRepository.save(drone);
        logisticaService.alocarPedidosPendentes(novoDrone);

        return novoDrone;
    }

    public List<Drone> findAll(){
        return droneRepository.findAll();
    }

    public Drone findById(Long id){
        Optional<Drone> obj = droneRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Drone não encontrado! Id: " + id));
    }

    @Transactional
    public Drone retornarDroneABase(Long droneId) {
        Drone drone = findById(droneId);

        if (drone.getStatus() != DroneStatus.RETORNANDO) {
            throw new IllegalStateException("O drone não está retornando para ser finalizado.");
        }

        drone.setStatus(DroneStatus.IDLE);
        drone.setBateriaAtual(100.0);

        return droneRepository.save(drone);
    }
}
