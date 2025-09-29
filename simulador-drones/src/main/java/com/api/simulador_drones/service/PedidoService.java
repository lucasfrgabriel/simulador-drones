package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Pedido;
import com.api.simulador_drones.dto.PedidoDTO;
import com.api.simulador_drones.repository.PedidoRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PedidoService {

    @Autowired
    private PedidoRepository pedidoRepository;

    @Autowired
    private LogisticaService logisticaService;

    @Transactional
    public Pedido create(PedidoDTO pedidoDTO) {
        Pedido pedido = new Pedido(
                pedidoDTO.getPesoKg(),
                pedidoDTO.getPrioridade(),
                pedidoDTO.getPosX(),
                pedidoDTO.getPosY()
        );

        Pedido pedidoSalvo = pedidoRepository.save(pedido);
        logisticaService.alocarNovoPedido(pedidoSalvo);

        return pedidoSalvo;
    }

    public List<Pedido> findAll(){
        return pedidoRepository.findAll();
    }

    public Pedido findById(Long id) {
        Optional<Pedido> obj = pedidoRepository.findById(id);
        return obj.orElseThrow(() -> new RuntimeException("Pedido não encontrado! Id: " + id));
    }
}