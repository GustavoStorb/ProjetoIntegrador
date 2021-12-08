package com.gustavostorb.projetointegrador1.data;

import com.gustavostorb.projetointegrador1.types.EmployeeType;

/**
 *
 * @author Gustavo
 */
public class Employee {
    
    private Integer id;
    private String name;
    private EmployeeType type;
    private String birthDate;
    private Boolean licensed;
    
    public Employee(Integer id, String name, EmployeeType type, String birthDate, Boolean licensed) {
        this.id = id;
        this.name = name;
        this.type = type;
        this.birthDate = birthDate;
        this.licensed = licensed;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public EmployeeType getType() {
        return type;
    }

    public void setType(EmployeeType type) {
        this.type = type;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }

    public Boolean getLicensed() {
        return licensed;
    }

    public void setLicensed(Boolean licensed) {
        this.licensed = licensed;
    }   
}
