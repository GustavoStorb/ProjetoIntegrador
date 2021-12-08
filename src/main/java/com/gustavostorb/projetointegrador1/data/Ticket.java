package com.gustavostorb.projetointegrador1.data;

/**
 *
 * @author Gustavo
 */
public class Ticket {

    private Integer id;
    private String address;
    private String time;
    private Double km;
    private Employee employee;
    private Vehicle vehicle;

    public Ticket(Integer id, String address, String time, Double km, Employee employee, Vehicle vehicle) {
        this.id = id;
        this.address = address;
        this.time = time;
        this.km = km;
        this.employee = employee;
        this.vehicle = vehicle;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Double getKm() {
        return km;
    }

    public void setKm(Double km) {
        this.km = km;
    }

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }
}
