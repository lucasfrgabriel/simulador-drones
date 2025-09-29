package com.api.simulador_drones.service;

import com.api.simulador_drones.domain.Pedido;
import com.api.simulador_drones.domain.enums.Prioridade;
import com.api.simulador_drones.dto.PedidoDTO;
import com.api.simulador_drones.repository.PedidoRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PedidoServiceTest {

    @InjectMocks
    private PedidoService pedidoService;

    @Mock
    private PedidoRepository pedidoRepository;

    @Mock
    private LogisticaService logisticaService;

    @Test
    @DisplayName("Criar novo pedido")
    void criarPedido() {
        PedidoDTO pedidoDTO = new PedidoDTO();
        pedidoDTO.setPesoKg(5.0);
        pedidoDTO.setPrioridade(Prioridade.ALTA);
        pedidoDTO.setPosX(10);
        pedidoDTO.setPosY(20);

        Pedido pedidoSalvo = new Pedido(5.0, Prioridade.ALTA, 10, 20);
        pedidoSalvo.setId(1L);

        // "Ensina" o mock do repositório a retornar o "pedidoSalvo" quando o método save for chamado
        when(pedidoRepository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        Pedido resultado = pedidoService.create(pedidoDTO);

        assertNotNull(resultado);
        assertEquals(1L, resultado.getId());
        assertEquals(5.0, resultado.getPesoKg());

        verify(pedidoRepository, times(1)).save(any(Pedido.class));
        verify(logisticaService, times(1)).alocarNovoPedido(pedidoSalvo);
    }

    @Test
    @DisplayName("Buscar pedido que não existe")
    void buscarPedidoErrado() {
        Long pedidoId = 1L;

        when(pedidoRepository.findById(pedidoId)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            pedidoService.findById(pedidoId);
        });

        assertEquals("Pedido não encontrado! Id: " + pedidoId, exception.getMessage());
    }
}
