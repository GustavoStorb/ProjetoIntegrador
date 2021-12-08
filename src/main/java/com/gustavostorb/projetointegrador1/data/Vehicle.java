package com.gustavostorb.projetointegrador1.data;

import com.gustavostorb.projetointegrador1.types.VehicleType;

/**
 *
 * @author Gustavo
 */
public class Vehicle {
 
    private Integer id;
    private VehicleType type;
    private Integer year;
    private Double consumption;
    
    public Vehicle(Integer id, VehicleType type, Integer year, Double consumption) {
        this.id = id;
        this.type = type;
        this.year = year;
        this.consumption = consumption;
    }
    
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public VehicleType getType() {
        return type;
    }

    public void setType(VehicleType type) {
        this.type = type;
    }
    

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getConsumption() {
        return consumption;
    }

    public void setConsumption(Double consumption) {
        this.consumption = consumption;
    }
}
