package com.api.simulador_drones.util;

import com.api.simulador_drones.domain.Pedido;

import java.util.List;

public class CalculadoraDistancia {
    /**
     * Calcula a distância euclidiana entre dois pontos (x1, y1) e (x2, y2).
     * A fórmula é a raiz quadrada de ((x2-x1)^2 + (y2-y1)^2).
     */
    public static double calcular(int x1, int y1, int x2, int y2) {
        return Math.sqrt(Math.pow(x2 - x1, 2) + Math.pow(y2 - y1, 2));
    }

    /**
     * Calcula a distância total de uma rota de entrega.
     * A rota começa na base (0,0), passa por cada pedido na ordem da lista,
     * e retorna para a base.
     * @param rota A lista de pedidos que compõem a rota.
     * @return A distância total percorrida.
     */
    public static double calcular(List<Pedido> rota) {
        if (rota == null || rota.isEmpty()) {
            return 0.0;
        }

        int pontoPartidaX = 0;
        int pontoPartidaY = 0;
        double distanciaTotal = 0.0;

        for (Pedido pedido : rota) {
            distanciaTotal += calcular(pontoPartidaX, pontoPartidaY, pedido.getPosX(), pedido.getPosY());
            pontoPartidaX = pedido.getPosX();
            pontoPartidaY = pedido.getPosY();
        }

        distanciaTotal += calcular(pontoPartidaX, pontoPartidaY, 0, 0);

        return distanciaTotal;
    }
}