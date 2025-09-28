package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Drone;
import com.api.simulador_drones.domain.Entrega;
import com.api.simulador_drones.domain.Pedido;

import com.api.simulador_drones.domain.enums.DroneStatus;
import com.api.simulador_drones.domain.enums.PedidoStatus;

import com.api.simulador_drones.repository.DroneRepository;
import com.api.simulador_drones.repository.EntregaRepository;
import com.api.simulador_drones.repository.PedidoRepository;

import com.api.simulador_drones.util.CalculadoraDistancia;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class LogisticaService {
    @Autowired
    private DroneRepository droneRepository;

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private EntregaRepository entregaRepository;

    @Transactional
    public void alocarNovoPedido(Pedido pedido) {
        boolean foiAgrupado = adicionarEntrega(pedido);
        if (foiAgrupado) {
            return;
        }

        boolean criouNovaEntrega = criarEntrega(pedido);
        if (criouNovaEntrega) {
            return;
        }

        System.err.println("Nenhuma opção de alocação encontrada para o pedido: " + pedido.getId());
    }

    private boolean adicionarEntrega(Pedido novoPedido) {
        List<Entrega> entregasEmCarregamento = entregaRepository.findByDroneStatus(DroneStatus.CARREGANDO);

        Optional<Entrega> addEntrega = entregasEmCarregamento.stream()
                .filter(entrega -> {
                    Drone drone = entrega.getDroneAssociado();
                    boolean pesoOk = drone.getCapacidadeMaximaKg() >= (entrega.getPesoTotalKg() + novoPedido.getPesoKg());
                    if (!pesoOk) return false;

                    List<Pedido> rotaFutura = new ArrayList<>(entrega.getPedidosEntrega());
                    rotaFutura.add(novoPedido);

                    double distanciaFutura = CalculadoraDistancia.calcular(rotaFutura);

                    return drone.getAutonomiaMaximaKm() >= distanciaFutura;
                })
                .findFirst();

        if (addEntrega.isPresent()) {
            Entrega entrega = addEntrega.get();

            entrega.adicionarPedido(novoPedido);
            novoPedido.setEntrega(entrega);
            novoPedido.setPedidoStatus(PedidoStatus.ALOCADO);

            entregaRepository.save(entrega);
            pedidoRepository.save(novoPedido);

            return true;
        }
        return false;
    }

    private boolean criarEntrega(Pedido pedido) {
        List<Drone> dronesOciosos = droneRepository.findByStatus(DroneStatus.IDLE);

        Optional<Drone> droneDisponivel = dronesOciosos.stream()
                .filter(drone -> {
                    boolean pesoOk = drone.getCapacidadeMaximaKg() >= pedido.getPesoKg();
                    if (!pesoOk) return false;

                    double distanciaDaRota = CalculadoraDistancia.calcular(List.of(pedido));
                    return drone.getAutonomiaMaximaKm() >= distanciaDaRota;
                })
                .findFirst();

        if (droneDisponivel.isPresent()) {
            Drone droneEscolhido = droneDisponivel.get();

            return novaEntrega(pedido, droneEscolhido);
        }
        return false;
    }

    private boolean criarEntrega(Pedido pedido, Drone drone) {
        boolean pesoOk = drone.getCapacidadeMaximaKg() >= pedido.getPesoKg();
        if (!pesoOk) return false;

        double distanciaDaRota = CalculadoraDistancia.calcular(List.of(pedido));
        boolean autonomiaOk = drone.getAutonomiaMaximaKm() >= distanciaDaRota;

        if (autonomiaOk) {
            return novaEntrega(pedido, drone);
        }
        return false;
    }

    private boolean novaEntrega(Pedido pedido, Drone drone) {
        Entrega novaEntrega = new Entrega(drone);
        novaEntrega.adicionarPedido(pedido);

        pedido.setEntrega(novaEntrega);
        pedido.setPedidoStatus(PedidoStatus.ALOCADO);
        drone.setStatus(drone.getStatus().avancarStatus());

        entregaRepository.save(novaEntrega);
        pedidoRepository.save(pedido);
        droneRepository.save(drone);
        return true;
    }

    @Transactional
    public void alocarPedidosPendentes(Drone drone) {
        List<Pedido> pedidosPendentes = pedidoRepository.findByPedidoStatus(PedidoStatus.PENDENTE);
        pedidosPendentes.forEach(pedido -> criarEntrega(pedido, drone));
    }
}