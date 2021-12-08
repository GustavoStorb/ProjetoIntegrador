package com.gustavostorb.projetointegrador1.types;

import java.util.Arrays;

/**
 *
 * @author Gustavo
 */
public enum VehicleType {
    
    STRADA("Strada", 9.1),
    FIORINO("Fiorino", 8.3),
    UNO("Uno", 9.1),
    PALIO("Pálio", 10.1),
    LOGAN("Logan", 9.8);
    
    private String name;
    private Double consumption;
    
    VehicleType(String name, Double consumption) {
        this.name = name;
        this.consumption = consumption;
              
    }
    
    public String getName() {
        return name;
    }

    public Double getConsumption() {
        return consumption;
    }
    

    public static VehicleType get(String name) {
        return Arrays.stream(VehicleType.values())
                .filter(type -> type.name.equals(name))
                .findFirst()
                .orElse(null);
    }
    
}
