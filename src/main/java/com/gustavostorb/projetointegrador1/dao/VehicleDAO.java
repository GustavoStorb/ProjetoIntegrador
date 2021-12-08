package com.gustavostorb.projetointegrador1.dao;

import com.gustavostorb.projetointegrador1.data.Vehicle;
import com.gustavostorb.projetointegrador1.database.Database;
import com.gustavostorb.projetointegrador1.types.VehicleType;
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
public class VehicleDAO {

    private Connection connection;

    public VehicleDAO() throws SQLException {
        this.connection = Database.getConnection();
    }
    
    public void createTable() {
        try (Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS vehicle ("
                    + "id int(11) NOT NULL auto_increment, "
                    + "type varchar(50) NOT NULL, "
                    + "year int(4) NOT NULL, "
                    + "consumption double NOT NULL, "
                    + "PRIMARY KEY (id))"); 
             
            
            return;
        } catch(SQLException exception) {
            exception.printStackTrace();
        }
        
        return;
    }

    public Vehicle insert(Vehicle vehicle) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO vehicle (type, year, consumption) VALUES (?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {

            statement.setString(1, vehicle.getType().toString());
            statement.setInt(2, vehicle.getYear());
            statement.setDouble(3, vehicle.getConsumption()); 
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                vehicle.setId(resultSet.getInt(1));
            }

            resultSet.close();
            statement.close();

            return vehicle;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return vehicle;
    }

    public Vehicle update(Vehicle vehicle) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE vehicle SET type = ?, year = ?, consumption = ? WHERE id = ?" // pronto
        )) {
            statement.setString(1, vehicle.getType().toString());
            statement.setInt(2, vehicle.getYear());
            statement.setDouble(3, vehicle.getConsumption());
            statement.setInt(4, vehicle.getId());
            statement.execute();

            statement.close();
            return vehicle;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return vehicle;
    }

    public boolean delete(Vehicle vehicle) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM vehicle WHERE id = ?"
        )) {
            statement.setInt(1, vehicle.getId());
            statement.execute();
            statement.close();

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public Vehicle findOneById(Integer id) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM vehicle WHERE id = ?"
        )) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                Vehicle vehicle = new Vehicle(
                        resultSet.getInt("id"),
                        VehicleType.valueOf(resultSet.getString("type")),
                        resultSet.getInt("year"),
                        resultSet.getDouble("consumption")
                );
                
                statement.close();
                resultSet.close();
                return vehicle;
            }

            return null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public ArrayList<Vehicle> findAll() { 
        
       ArrayList<Vehicle> vehicles = new ArrayList<>();
       
       try ( PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM vehicle"
        )) {

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                Vehicle vehicle = new Vehicle(
                        resultSet.getInt("id"),
                        VehicleType.valueOf(resultSet.getString("type")),
                        resultSet.getInt("year"),
                        resultSet.getDouble("consumption")
                );
                
                vehicles.add(vehicle);
            }
            
            resultSet.close();
            statement.close();
            
            return vehicles;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return vehicles;
    }
}
