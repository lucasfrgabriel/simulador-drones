package com.api.simulador_drones.utils;

import com.api.simulador_drones.domain.Pedido;
import com.api.simulador_drones.domain.enums.Prioridade;
import com.api.simulador_drones.util.CalculadoraDistancia;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CalculadoraDistanciaTest {
    @Test
    @DisplayName("Calcular a distancia entre dois pontos")
    void distanciaDoisPontos() {
        double distancia = CalculadoraDistancia.calcular(0, 0, 3, 4);

        assertEquals(5.0, distancia);
    }

    @Test
    @DisplayName("Rota nula ou vazia")
    void semDistancia() {
        assertEquals(0.0, CalculadoraDistancia.calcular(null));
        assertEquals(0.0, CalculadoraDistancia.calcular(Collections.emptyList()));
    }

    @Test
    @DisplayName("Distancia de uma rota (ida e volta)")
    void distanciaRotaSimples() {
        Pedido pedido = new Pedido(1.0, Prioridade.MEDIA, 3, 4);
        List<Pedido> rota = List.of(pedido);

        double distancia = CalculadoraDistancia.calcular(rota);

        assertEquals(10.0, distancia);
    }

    @Test
    @DisplayName("Distancia de uma rota com multiplos pontos (ida e volta)")
    void distanciaRotaMultiplosPontos() {
        Pedido p1 = new Pedido(1.0, Prioridade.MEDIA, 3, 4);
        Pedido p2 = new Pedido(1.0, Prioridade.ALTA, 6, 8);
        List<Pedido> rota = List.of(p2, p1); //ordem de prioridade

        double distancia = CalculadoraDistancia.calcular(rota);

        assertEquals(20.0, distancia);
    }
}