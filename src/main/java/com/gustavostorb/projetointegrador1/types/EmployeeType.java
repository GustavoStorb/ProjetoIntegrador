/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavostorb.projetointegrador1.types;

import java.util.Arrays;

/**
 *
 * @author Gustavo
 */
public enum EmployeeType {

    SUPPORT("Suporte"),
    DRIVER("Motorista"),
    MANAGER("Gerente");

    private String name;

    EmployeeType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static EmployeeType get(String name) {
        return Arrays.stream(EmployeeType.values())
                .filter(type -> type.name.equals(name))
                .findFirst()
                .orElse(null);
    }

}
