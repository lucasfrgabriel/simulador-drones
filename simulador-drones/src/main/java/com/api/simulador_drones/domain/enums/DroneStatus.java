package com.api.simulador_drones.domain.enums;

public enum DroneStatus {
    IDLE,
    CARREGANDO,
    EM_VOO,
    ENTREGANDO,
    RETORNANDO;

    public DroneStatus avancarStatus(){
        return switch (this){
            case IDLE -> CARREGANDO;
            case CARREGANDO -> EM_VOO;
            case EM_VOO -> ENTREGANDO;
            case ENTREGANDO -> RETORNANDO;
            case RETORNANDO -> IDLE;
        };
    }
}