package com.api.simulador_drones.repository;

import com.api.simulador_drones.domain.Pedido;
import com.api.simulador_drones.domain.enums.PedidoStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido, Long> {

    List<Pedido> findByPedidoStatus(PedidoStatus pedidoStatus);
}
