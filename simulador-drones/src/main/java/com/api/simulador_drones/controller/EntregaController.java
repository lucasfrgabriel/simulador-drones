package com.api.simulador_drones.controller;

import com.api.simulador_drones.domain.Entrega;
import com.api.simulador_drones.dto.EntregaDTO;
import com.api.simulador_drones.service.EntregaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/entregas")
public class EntregaController {

    @Autowired
    private EntregaService entregaService;

    @GetMapping(value = "/{id}/rota")
    public ResponseEntity<EntregaDTO> getRota(@PathVariable Long id) {
        EntregaDTO rota = entregaService.calcularRota(id);
        return ResponseEntity.ok().body(rota);
    }

    @PostMapping("/{id}/iniciar")
    public ResponseEntity<Entrega> iniciarEntrega(@PathVariable Long id) {
        Entrega entregaIniciada = entregaService.iniciarEntrega(id);
        return ResponseEntity.ok().body(entregaIniciada);
    }
    @PostMapping("/{id}/finalizar")
    public ResponseEntity<Entrega> finalizarEntrega(@PathVariable Long id) {
        Entrega entregaFinalizada = entregaService.finalizarEntrega(id);
        return ResponseEntity.ok().body(entregaFinalizada);
    }
}