package com.api.simulador_drones.controller;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.service.DroneService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}