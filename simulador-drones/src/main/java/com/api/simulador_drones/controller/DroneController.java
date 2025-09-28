package com.api.simulador_drones.controller;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.dto.DroneDTO;

import com.api.simulador_drones.service.DroneService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping(value = "/drones")
public class DroneController {

    @Autowired
    private DroneService service;

    @GetMapping
    public ResponseEntity<List<Drone>> findAll(){
        List<Drone> list = service.findAll();
        return ResponseEntity.ok().body(list);
    }

    @GetMapping(value = "/{id}")
    public ResponseEntity<Drone> findById(@PathVariable(value = "id") Long id){
        Drone obj = service.findById(id);
        return ResponseEntity.ok().body(obj);
    }

    @PostMapping("/{id}/retornar")
    public ResponseEntity<Drone> retornarABase(@PathVariable Long id) {
        Drone drone = service.retornarDroneABase(id);
        return ResponseEntity.ok().body(drone);
    }

    @PostMapping
    public ResponseEntity<Drone> create(@RequestBody DroneDTO droneDTO){
        Drone novoDrone = service.create(droneDTO);
        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(novoDrone.getId()).toUri();
        return ResponseEntity.created(uri).body(novoDrone);
    }
}