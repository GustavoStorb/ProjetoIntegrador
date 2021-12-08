/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.gustavostorb.projetointegrador1.dao;

import com.gustavostorb.projetointegrador1.data.Employee;
import com.gustavostorb.projetointegrador1.types.EmployeeType;
import com.gustavostorb.projetointegrador1.database.Database;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 *
 * @author Gustavo
 */
public class EmployeeDAO {

    private Connection connection;

    public EmployeeDAO() throws SQLException {
        this.connection = Database.getConnection();
    }
    
    public void createTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS employee ("
                    + "id int(11) NOT NULL auto_increment, "
                    + "name varchar(255) NOT NULL, "
                    + "type varchar(32) NOT NULL, "
                    + "birth_date varchar(255) NOT NULL, "
                    + "licensed BIT NOT NULL DEFAULT 1, "
                    + "PRIMARY KEY (id))"); 
            
            
            return;
        } catch(SQLException exception) {
            exception.printStackTrace();
        }
        
        return;
    }

    public Employee insert(Employee employee) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO employee (name, type, birth_date, licensed) VALUES (?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {

            statement.setString(1, employee.getName());
            statement.setString(2, employee.getType().toString());
            statement.setString(3, employee.getBirthDate());
            statement.setBoolean(4, employee.getLicensed());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                employee.setId(resultSet.getInt(1));
            }

            resultSet.close();
            statement.close();

            return employee;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return employee;
    }

    public Employee update(Employee employee) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE employee SET name = ?, type = ?, birth_date = ?, licensed = ? WHERE id = ?"
        )) {
            statement.setString(1, employee.getName());
            statement.setString(2, employee.getType().toString());
            statement.setString(3, employee.getBirthDate());
            statement.setBoolean(4, employee.getLicensed());
            statement.setInt(5, employee.getId());
            statement.execute();

            statement.close();
            return employee;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return employee;
    }

    public boolean delete(Employee employee) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM employee WHERE id = ?"
        )) {
            statement.setInt(1, employee.getId());
            statement.execute();
            statement.close();

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public Employee findOneById(Integer id) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM employee WHERE id = ?"
        )) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        EmployeeType.valueOf(resultSet.getString("type")),
                        resultSet.getString("birth_date"),
                        resultSet.getBoolean("licensed")
                );
                
                statement.close();
                resultSet.close();
                return employee;
            }

            return null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public ArrayList<Employee> findAll() { 
        
       ArrayList<Employee> employees = new ArrayList<>();
       
       try ( PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM employee"
        )) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Employee employee = new Employee(
                        resultSet.getInt("id"),
                        resultSet.getString("name"),
                        EmployeeType.valueOf(resultSet.getString("type")),
                        resultSet.getString("birth_date"),
                        resultSet.getBoolean("licensed")
                );
                
                employees.add(employee);
            }
            
            resultSet.close();
            statement.close();
            
            return employees;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return employees;
    }
}
