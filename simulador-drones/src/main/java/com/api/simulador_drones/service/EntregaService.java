package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Entrega;

import com.api.simulador_drones.dto.EntregaDTO;
import com.api.simulador_drones.repository.DroneRepository;
import com.api.simulador_drones.repository.EntregaRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

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
}
