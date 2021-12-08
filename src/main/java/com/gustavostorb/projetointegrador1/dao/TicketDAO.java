package com.gustavostorb.projetointegrador1.dao;

import com.gustavostorb.projetointegrador1.data.Employee;
import com.gustavostorb.projetointegrador1.data.Ticket;
import com.gustavostorb.projetointegrador1.data.Vehicle;
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
public class TicketDAO {

    private Connection connection;

    public TicketDAO() throws SQLException {
        this.connection = Database.getConnection();
    }

    public void createTable() {
        try ( Statement statement = this.connection.createStatement()) {
            statement.executeUpdate("CREATE TABLE IF NOT EXISTS ticket ("
                    + "id int(11) NOT NULL auto_increment, "
                    + "address varchar(255) NOT NULL, "
                    + "time varchar(100) NOT NULL, "
                    + "km double NOT NULL, "
                    + "employee_id int(11) NOT NULL, "
                    + "vehicle_id int(11) NOT NULL, "
                    + "PRIMARY KEY (id))");

            return;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return;
    }

    public Ticket insert(Ticket ticket) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "INSERT INTO ticket (address, time, km, employee_id, vehicle_id) VALUES (?, ?, ?, ?, ?)",
                Statement.RETURN_GENERATED_KEYS
        )) {

            statement.setString(1, ticket.getAddress());
            statement.setString(2, ticket.getTime());
            statement.setDouble(3, ticket.getKm());
            statement.setInt(4, ticket.getEmployee().getId());
            statement.setInt(5, ticket.getVehicle().getId());
            statement.execute();

            ResultSet resultSet = statement.getGeneratedKeys();
            if (resultSet.next()) {
                ticket.setId(resultSet.getInt(1));
            }

            resultSet.close();
            statement.close();

            return ticket;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return ticket;
    }

    public Ticket update(Ticket ticket) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE ticket SET address = ?, time = ?, km = ?, employee_id = ?, vehicle_id = ? WHERE id = ?"
        )) {
            statement.setString(1, ticket.getAddress());
            statement.setString(2, ticket.getTime());
            statement.setDouble(3, ticket.getKm());
            statement.setInt(4, ticket.getEmployee().getId());
            statement.setInt(5, ticket.getVehicle().getId());
            statement.setInt(6, ticket.getId()); // tenta ai

            statement.execute(); // dale 

            statement.close();
            return ticket;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return ticket;
    }

    public boolean delete(Ticket ticket) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "DELETE FROM ticket WHERE id = ?"
        )) {
            statement.setInt(1, ticket.getId());
            statement.execute();
            statement.close();

            return true;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return false;
    }

    public Ticket findOneById(Integer id) {
        try ( PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM ticket WHERE id = ?"
        )) {

            statement.setInt(1, id);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                EmployeeDAO employeeDAO = new EmployeeDAO();
                VehicleDAO vehicleDAO = new VehicleDAO();

                Ticket ticket = new Ticket(
                        resultSet.getInt("id"),
                        resultSet.getString("address"),
                        resultSet.getString("time"),
                        resultSet.getDouble("km"),
                        employeeDAO.findOneById(resultSet.getInt("employee_id")),
                        vehicleDAO.findOneById(resultSet.getInt("vehicle_id"))
                );

                statement.close();
                resultSet.close();
                return ticket;
            }

            return null;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return null;
    }

    public ArrayList<Ticket> findAll() {

        ArrayList<Ticket> tickets = new ArrayList<>();

        try ( PreparedStatement statement = this.connection.prepareStatement(
                "SELECT * FROM ticket"
        )) {

            EmployeeDAO employeeDAO = new EmployeeDAO();
            VehicleDAO vehicleDAO = new VehicleDAO();

            ArrayList<Employee> employees = employeeDAO.findAll();
            ArrayList<Vehicle> vehicles = vehicleDAO.findAll();

            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {

                int employeeId = resultSet.getInt("employee_id");
                int vehicleId = resultSet.getInt("vehicle_id");

                Employee employee = employees.stream()
                        .filter((_employee) -> _employee.getId() == employeeId)
                        .findFirst()
                        .orElse(null);

                Vehicle vehicle = vehicles.stream()
                        .filter((_vehicle) -> _vehicle.getId() == vehicleId)
                        .findFirst()
                        .orElse(null);

                Ticket ticket = new Ticket(
                        resultSet.getInt("id"),
                        resultSet.getString("address"),
                        resultSet.getString("time"),
                        resultSet.getDouble("km"),
                        employee,
                        vehicle
                );

                tickets.add(ticket);
            }

            resultSet.close();
            statement.close();

            return tickets;
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        return tickets;
    }
}
