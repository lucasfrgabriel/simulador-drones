package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.repository.DroneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DroneService {

    @Autowired
    private DroneRepository droneRepository;

    public List<Drone> findAll(){
        return droneRepository.findAll();
    }

    public Drone findById(Long id){
        Optional<Drone> obj = droneRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Drone não encontrado! Id: " + id));
    }
}
