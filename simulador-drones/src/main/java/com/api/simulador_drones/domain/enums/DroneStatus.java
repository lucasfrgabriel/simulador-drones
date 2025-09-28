package com.api.simulador_drones.domain.enums;

public enum DroneStatus {
    IDLE,
    CARREGANDO,
    EM_VOO,
    RETORNANDO;

    public DroneStatus avancarStatus(){
        return switch (this){
            case IDLE -> CARREGANDO;
            case CARREGANDO -> EM_VOO;
            case EM_VOO -> RETORNANDO;
            case RETORNANDO -> IDLE;
        };
    }
}