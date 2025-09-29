package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.domain.Entrega;

import com.api.simulador_drones.domain.enums.EntregaStatus;
import com.api.simulador_drones.domain.enums.PedidoStatus;
import com.api.simulador_drones.dto.EntregaDTO;
import com.api.simulador_drones.repository.DroneRepository;
import com.api.simulador_drones.repository.EntregaRepository;

import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    private static final double CONSUMO_BATERIA_POR_KM = 0.5;

    @Autowired
    private EntregaRepository entregaRepository;

    @Autowired
    private DroneRepository droneRepository;

    public List<Entrega> findAll(){
        return entregaRepository.findAll();
    }

    public Entrega findById(Long id){
        Optional<Entrega> obj = entregaRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Entrega não encontrada! Id: " + id));
    }

    public EntregaDTO calcularRota(Long entregaId) {
        Entrega entrega = findById(entregaId);

        double distanciaTotal = entrega.getDistanciaTotalKm();
        boolean autonomiaOk = entrega.getDroneAssociado().getAutonomiaMaximaKm() >= distanciaTotal;

        return new EntregaDTO(entrega.getId(), entrega.getDroneAssociado().getId(), entrega.getPedidosEntrega(), distanciaTotal, autonomiaOk);
    }

    @Transactional
    public Entrega iniciarEntrega(Long id) {
        Entrega entrega = findById(id);

        entrega.setStatus(EntregaStatus.EM_CURSO);
        entrega.setInicioEntrega(LocalDateTime.now());

        Drone drone = entrega.getDroneAssociado();
        drone.setStatus(drone.getStatus().avancarStatus());

        entrega.getPedidosEntrega().forEach(pedido -> pedido.setPedidoStatus(PedidoStatus.A_CAMINHO));

        droneRepository.save(drone);
        return entregaRepository.save(entrega);
    }

    @Transactional
    public Entrega finalizarEntrega(Long id) {
        Entrega entrega = findById(id);

        if (entrega.getStatus() != EntregaStatus.EM_CURSO) {
            throw new IllegalStateException("A entrega não está 'EM_CURSO', por isso não pode ser finalizada.");
        }

        Drone drone = entrega.getDroneAssociado();
        double distanciaPercorrida = entrega.getDistanciaTotalKm();
        double bateriaConsumida = distanciaPercorrida * CONSUMO_BATERIA_POR_KM;
        double novaBateria = drone.getBateriaAtual() - bateriaConsumida;

        drone.setBateriaAtual(Math.max(0, novaBateria));

        entrega.setStatus(EntregaStatus.FINALIZADA);
        entrega.setFimEntrega(LocalDateTime.now());

        entrega.getPedidosEntrega().forEach(pedido -> pedido.setPedidoStatus(PedidoStatus.ENTREGUE));
        drone.setStatus(drone.getStatus().avancarStatus());

        droneRepository.save(drone);
        return entregaRepository.save(entrega);
    }
}
