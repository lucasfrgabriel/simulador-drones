package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Entrega;
import com.api.simulador_drones.repository.EntregaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EntregaService {

    @Autowired
    private EntregaRepository entregaRepository;

    public List<Entrega> findAll(){
        return entregaRepository.findAll();
    }

    public Entrega findById(Long id){
        Optional<Entrega> obj = entregaRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Entrega não encontrada! Id: " + id));
    }
}
