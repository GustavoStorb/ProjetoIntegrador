package com.gustavostorb.projetointegrador1;

import com.gustavostorb.projetointegrador1.dao.EmployeeDAO;
import com.gustavostorb.projetointegrador1.dao.TicketDAO;
import com.gustavostorb.projetointegrador1.dao.VehicleDAO;
import com.gustavostorb.projetointegrador1.data.Employee;
import com.gustavostorb.projetointegrador1.data.Ticket;
import com.gustavostorb.projetointegrador1.data.Vehicle;
import com.gustavostorb.projetointegrador1.types.EmployeeType;
import com.gustavostorb.projetointegrador1.types.VehicleType;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import javax.swing.JOptionPane;
import javax.swing.table.DefaultTableModel;
import java.lang.Math;
import java.text.ParseException;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Gustavo
 */
public class MenuInicial extends javax.swing.JFrame {

    String modeEmployee;
    String modeVehicle;
    String modeTicket;

    public MenuInicial() {
        setLocation(650, 200);
        initComponents();
        try {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            employeeDAO.createTable();

            VehicleDAO vehicleDAO = new VehicleDAO();
            vehicleDAO.createTable();

            TicketDAO ticketDAO = new TicketDAO();
            ticketDAO.createTable();

        } catch (SQLException exception) {
            exception.printStackTrace();
            System.out.println("Erro ao criar tabelas.");
        }

        showAllEmployees();
        showAllVehicles();
        showAllTickets();
        modeEmployee = "Home";
        modeVehicle = "Home";
        modeTicket = "Home";
        changeInterfaceButtonsEmployee();
        changeInterfaceButtonsVehicle();
        changeInterfaceButtonsTicket();
    }

    public void refreshTicketContent() {
        vehicleTicketSelector.removeAllItems();
        employeeTicketSelector.removeAllItems();

        try {
            EmployeeDAO employeeDAO = new EmployeeDAO();
            VehicleDAO vehicleDAO = new VehicleDAO();

            ArrayList<Employee> employees = employeeDAO.findAll();
            ArrayList<Vehicle> vehicles = vehicleDAO.findAll();

            employees.forEach((employee) -> {
                if (employee.getLicensed() == true) {
                    employeeTicketSelector.addItem(new ComboItem(employee.getName(), employee.getId().toString()).toString()); // 1 é pra transformar ID em string e o outro é pra transformar a class em to string

                }
            });

            vehicles.forEach((vehicle) -> {
                vehicleTicketSelector.addItem(new ComboItem(vehicle.getType().toString(), vehicle.getId().toString()).toString());
            });
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void showAllEmployees() {
        try {
            EmployeeDAO dao = new EmployeeDAO();
            ArrayList<Employee> employeesList = dao.findAll();
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Nome", "Função", "Habilitado", "Nascimento"}, 0);
            for (int i = 0; i < employeesList.size(); i++) {
                Employee employee = employeesList.get(i);

                model.addRow(new Object[]{
                    employee.getId(),
                    employee.getName(),
                    employee.getType().getName(),
                    employee.getLicensed() ? "Sim" : "Não",
                    employee.getBirthDate()
                });

                employeeTable.setModel(model);
            }

            employeeTable.getColumnModel().getColumn(0).setResizable(false);
            employeeTable.getColumnModel().getColumn(0).setHeaderValue("ID");
            employeeTable.getColumnModel().getColumn(1).setResizable(false);
            employeeTable.getColumnModel().getColumn(1).setHeaderValue("Nome");
            employeeTable.getColumnModel().getColumn(2).setResizable(false);
            employeeTable.getColumnModel().getColumn(2).setHeaderValue("Função");
            employeeTable.getColumnModel().getColumn(3).setResizable(false);
            employeeTable.getColumnModel().getColumn(3).setHeaderValue("Habilitado");
            employeeTable.getColumnModel().getColumn(4).setResizable(false);
            employeeTable.getColumnModel().getColumn(4).setHeaderValue("Nascimento");
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void showAllVehicles() {
        try {
            VehicleDAO dao = new VehicleDAO();
            ArrayList<Vehicle> vehicleList = dao.findAll();
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Modelo", "Ano", "Consumo"}, 0);
            for (int i = 0; i < vehicleList.size(); i++) {
                Vehicle vehicle = vehicleList.get(i);

                model.addRow(new Object[]{
                    vehicle.getId(),
                    vehicle.getType().getName(),
                    vehicle.getYear(),
                    vehicle.getConsumption()
                });
                vehicleTable.setModel(model);
            }

            vehicleTable.getColumnModel().getColumn(0).setResizable(false);
            vehicleTable.getColumnModel().getColumn(0).setHeaderValue("ID");
            vehicleTable.getColumnModel().getColumn(1).setResizable(false);
            vehicleTable.getColumnModel().getColumn(1).setHeaderValue("Modelo");
            vehicleTable.getColumnModel().getColumn(2).setResizable(false);
            vehicleTable.getColumnModel().getColumn(2).setHeaderValue("Ano");
            vehicleTable.getColumnModel().getColumn(3).setResizable(false);
            vehicleTable.getColumnModel().getColumn(3).setHeaderValue("Consumo");

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void showAllTickets() {
        try {
            TicketDAO dao = new TicketDAO();
            ArrayList<Ticket> ticketList = dao.findAll();
            DefaultTableModel model = new DefaultTableModel(new Object[]{"ID", "Endereço", "Tempo", "Quilometragem", "Funcionario", "Veiculo", "CG"}, 0);
            for (int i = 0; i < ticketList.size(); i++) {
                Ticket ticket = ticketList.get(i);
                Double CG = (ticket.getKm() * ticket.getVehicle().getConsumption()) * 0.73 * 0.75 * 3.7;
                model.addRow(new Object[]{
                    ticket.getId(),
                    ticket.getAddress(),
                    ticket.getTime(),
                    ticket.getKm(),
                    ticket.getEmployee().getName(),
                    ticket.getVehicle().getType().getName(),
                    String.format("%.2f ", CG)
                });
                //System.out.println(ticket.getVehicle().getConsumption());

                ticketTable.setModel(model);

            }

            refreshTicketContent();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }

    public void changeInterfaceButtonsTicket() {
        switch (modeTicket) {
            case "Home":
                btnNewTicket.setEnabled(true);
                btnEditTicket.setEnabled(false);
                btnDeleteTicket.setEnabled(false);
                employeeTicketSelector.setEnabled(false);
                vehicleTicketSelector.setEnabled(false);
                btnCancelRegisterTicket.setEnabled(false);
                btnSaveRegisterTicket.setEnabled(false);
                adressTicket.setEnabled(false);
                timeTicket.setEnabled(false);
                kmTicket.setEnabled(false);
                adressTicket.setText("");
                timeTicket.setText("");
                kmTicket.setText("");
                break;
            case "New":
                btnNewTicket.setEnabled(false);
                btnEditTicket.setEnabled(false);
                btnDeleteTicket.setEnabled(false);
                employeeTicketSelector.setEnabled(true);
                vehicleTicketSelector.setEnabled(true);
                btnCancelRegisterTicket.setEnabled(true);
                btnSaveRegisterTicket.setEnabled(true);
                adressTicket.setEnabled(true);
                timeTicket.setEnabled(true);
                kmTicket.setEnabled(true);
                break;
            case "Edit":
                btnNewTicket.setEnabled(true);
                btnEditTicket.setEnabled(false);
                btnDeleteTicket.setEnabled(false);
                employeeTicketSelector.setEnabled(true);
                vehicleTicketSelector.setEnabled(true);
                btnCancelRegisterTicket.setEnabled(true);
                btnSaveRegisterTicket.setEnabled(true);
                adressTicket.setEnabled(true);
                timeTicket.setEnabled(true);
                kmTicket.setEnabled(true);
                break;
            case "Delete":
                btnNewTicket.setEnabled(true);
                btnEditTicket.setEnabled(false);
                btnDeleteTicket.setEnabled(false);
                employeeTicketSelector.setEnabled(false);
                vehicleTicketSelector.setEnabled(false);
                btnCancelRegisterTicket.setEnabled(false);
                btnSaveRegisterTicket.setEnabled(false);
                adressTicket.setEnabled(false);
                timeTicket.setEnabled(false);
                kmTicket.setEnabled(false);
                break;
            case "Select":
                btnNewTicket.setEnabled(true);
                btnEditTicket.setEnabled(true);
                btnDeleteTicket.setEnabled(true);
                employeeTicketSelector.setEnabled(false);
                vehicleTicketSelector.setEnabled(false);
                btnCancelRegisterTicket.setEnabled(false);
                btnSaveRegisterTicket.setEnabled(false);
                adressTicket.setEnabled(false);
                timeTicket.setEnabled(false);
                kmTicket.setEnabled(false);
                break;
            default:
                JOptionPane.showMessageDialog(null, null, "Opção Invalida", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void changeInterfaceButtonsVehicle() {
        switch (modeVehicle) {
            case "Home":
                btnNewVehicle.setEnabled(true);
                btnEditVehicle.setEnabled(false);
                btnDeleteVehicle.setEnabled(false);
                modelVehicleSelector.setEnabled(false);
                btnSaveRegisterVehicle.setEnabled(false);
                btnCancelRegisterVehicle.setEnabled(false);
                yearVehicleSelector.setEnabled(false);
                break;
            case "New":
                btnNewVehicle.setEnabled(false);
                btnEditVehicle.setEnabled(false);
                btnDeleteVehicle.setEnabled(false);
                modelVehicleSelector.setEnabled(true);
                btnSaveRegisterVehicle.setEnabled(true);
                btnCancelRegisterVehicle.setEnabled(true);
                yearVehicleSelector.setEnabled(true);
                break;
            case "Edit":
                btnNewVehicle.setEnabled(true);
                btnEditVehicle.setEnabled(false);
                btnDeleteVehicle.setEnabled(false);
                modelVehicleSelector.setEnabled(true);
                btnSaveRegisterVehicle.setEnabled(true);
                btnCancelRegisterVehicle.setEnabled(true);
                yearVehicleSelector.setEnabled(true);
                break;
            case "Delete":
                btnNewVehicle.setEnabled(true);
                btnEditVehicle.setEnabled(false);
                btnDeleteVehicle.setEnabled(false);
                modelVehicleSelector.setEnabled(false);
                btnSaveRegisterVehicle.setEnabled(false);
                btnCancelRegisterVehicle.setEnabled(false);
                yearVehicleSelector.setEnabled(false);
                break;
            case "Select":
                btnNewVehicle.setEnabled(true);
                btnEditVehicle.setEnabled(true);
                btnDeleteVehicle.setEnabled(true);
                modelVehicleSelector.setEnabled(false);
                btnSaveRegisterVehicle.setEnabled(false);
                btnCancelRegisterVehicle.setEnabled(false);
                yearVehicleSelector.setEnabled(false);
                break;
            default:
                JOptionPane.showMessageDialog(null, null, "Opção Invalida", JOptionPane.ERROR_MESSAGE);
        }
    }

    public void changeInterfaceButtonsEmployee() {
        switch (modeEmployee) {
            case "Home":
                btnNewEmployee.setEnabled(true);
                btnEditEmployee.setEnabled(false);
                btnDeleteEmployee.setEnabled(false);
                employeeTypeSelector.setEnabled(false);
                btnSaveRegisterEmployee.setEnabled(false);
                btnCancelRegisterEmployee.setEnabled(false);
                nameEmployee.setEnabled(false);
                licensedTrue.setEnabled(false);
                licensedFalse.setEnabled(false);
                employeeBirthdate.setEnabled(false);
                nameEmployee.setText("");
                btnGroupEmployee.clearSelection();
                employeeBirthdate.setCalendar(null);
                break;
            case "New":
                nameEmployee.setText("");
                btnNewEmployee.setEnabled(false);
                btnEditEmployee.setEnabled(false);
                btnDeleteEmployee.setEnabled(false);
                employeeTypeSelector.setEnabled(true);
                btnSaveRegisterEmployee.setEnabled(true);
                btnCancelRegisterEmployee.setEnabled(true);
                nameEmployee.setEnabled(true);
                licensedTrue.setEnabled(true);
                licensedFalse.setEnabled(true);
                employeeBirthdate.setEnabled(true);
                break;
            case "Edit":
                btnNewEmployee.setEnabled(true);
                btnEditEmployee.setEnabled(false);
                btnDeleteEmployee.setEnabled(false);
                employeeTypeSelector.setEnabled(true);
                btnSaveRegisterEmployee.setEnabled(true);
                btnCancelRegisterEmployee.setEnabled(true);
                nameEmployee.setEnabled(true);
                licensedTrue.setEnabled(true);
                licensedFalse.setEnabled(true);
                employeeBirthdate.setEnabled(true);
                break;
            case "Delete":
                btnNewEmployee.setEnabled(true);
                btnEditEmployee.setEnabled(false);
                btnDeleteEmployee.setEnabled(false);
                employeeTypeSelector.setEnabled(false);
                btnSaveRegisterEmployee.setEnabled(false);
                btnCancelRegisterEmployee.setEnabled(false);
                nameEmployee.setEnabled(false);
                licensedTrue.setEnabled(false);
                licensedFalse.setEnabled(false);
                employeeBirthdate.setEnabled(false);
                break;
            case "Select":
                btnNewEmployee.setEnabled(true);
                btnEditEmployee.setEnabled(true);
                btnDeleteEmployee.setEnabled(true);
                employeeTypeSelector.setEnabled(false);
                btnSaveRegisterEmployee.setEnabled(false);
                btnCancelRegisterEmployee.setEnabled(false);
                nameEmployee.setEnabled(false);
                licensedTrue.setEnabled(false);
                licensedFalse.setEnabled(false);
                employeeBirthdate.setEnabled(false);
                break;
            default:
                JOptionPane.showMessageDialog(null, null, "Opção Invalida", JOptionPane.ERROR_MESSAGE);
        }
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jTabbedPane2 = new javax.swing.JTabbedPane();
        jRadioButtonMenuItem1 = new javax.swing.JRadioButtonMenuItem();
        jRadioButtonMenuItem2 = new javax.swing.JRadioButtonMenuItem();
        btnGroupEmployee = new javax.swing.ButtonGroup();
        calculateCarbonTab = new javax.swing.JTabbedPane();
        jPanel2 = new javax.swing.JPanel();
        jScrollPane2 = new javax.swing.JScrollPane();
        employeeTable = new javax.swing.JTable();
        jPanel5 = new javax.swing.JPanel();
        jLabel2 = new javax.swing.JLabel();
        nameEmployee = new javax.swing.JTextField();
        jLabel3 = new javax.swing.JLabel();
        licensedFalse = new javax.swing.JRadioButton();
        licensedTrue = new javax.swing.JRadioButton();
        jLabel4 = new javax.swing.JLabel();
        employeeBirthdate = new com.toedter.calendar.JDateChooser();
        btnSaveRegisterEmployee = new javax.swing.JButton();
        btnCancelRegisterEmployee = new javax.swing.JButton();
        jLabel1 = new javax.swing.JLabel();
        employeeTypeSelector = new javax.swing.JComboBox<>();
        btnNewEmployee = new javax.swing.JButton();
        btnEditEmployee = new javax.swing.JButton();
        btnDeleteEmployee = new javax.swing.JButton();
        jPanel3 = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        vehicleTable = new javax.swing.JTable();
        btnNewVehicle = new javax.swing.JButton();
        btnDeleteVehicle = new javax.swing.JButton();
        btnEditVehicle = new javax.swing.JButton();
        jPanel7 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        yearVehicleSelector = new com.toedter.calendar.JYearChooser();
        modelVehicleSelector = new javax.swing.JComboBox<>();
        btnSaveRegisterVehicle = new javax.swing.JButton();
        btnCancelRegisterVehicle = new javax.swing.JButton();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        ticketTable = new javax.swing.JTable();
        jPanel1 = new javax.swing.JPanel();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel12 = new javax.swing.JLabel();
        jLabel13 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        adressTicket = new javax.swing.JTextField();
        employeeTicketSelector = new javax.swing.JComboBox<>();
        vehicleTicketSelector = new javax.swing.JComboBox<>();
        timeTicket = new javax.swing.JTextField();
        btnSaveRegisterTicket = new javax.swing.JButton();
        btnCancelRegisterTicket = new javax.swing.JButton();
        kmTicket = new javax.swing.JTextField();
        btnNewTicket = new javax.swing.JButton();
        btnEditTicket = new javax.swing.JButton();
        btnDeleteTicket = new javax.swing.JButton();

        jRadioButtonMenuItem1.setSelected(true);
        jRadioButtonMenuItem1.setText("jRadioButtonMenuItem1");

        jRadioButtonMenuItem2.setSelected(true);
        jRadioButtonMenuItem2.setText("jRadioButtonMenuItem2");

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("Cadastrador");
        setCursor(new java.awt.Cursor(java.awt.Cursor.DEFAULT_CURSOR));

        calculateCarbonTab.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                calculateCarbonTabMouseClicked(evt);
            }
        });

        jPanel2.setPreferredSize(new java.awt.Dimension(655, 400));

        employeeTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null},
                {null, null, null, null, null}
            },
            new String [] {
                "ID", "Nome", "Função", "Habilitado", "Nascimento"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.String.class, java.lang.Boolean.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        employeeTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeeTableClick(evt);
            }
        });
        jScrollPane2.setViewportView(employeeTable);
        if (employeeTable.getColumnModel().getColumnCount() > 0) {
            employeeTable.getColumnModel().getColumn(0).setResizable(false);
            employeeTable.getColumnModel().getColumn(1).setResizable(false);
            employeeTable.getColumnModel().getColumn(2).setResizable(false);
            employeeTable.getColumnModel().getColumn(3).setResizable(false);
            employeeTable.getColumnModel().getColumn(4).setResizable(false);
        }

        jPanel5.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Cadastro de Funcionario"));
        jPanel5.setFont(new java.awt.Font("Segoe UI", 1, 14)); // NOI18N

        jLabel2.setText("Nome:");

        nameEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nameEmployeeActionPerformed(evt);
            }
        });

        jLabel3.setText("Habilitado:");

        btnGroupEmployee.add(licensedFalse);
        licensedFalse.setText("Não");
        licensedFalse.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                licensedFalseActionPerformed(evt);
            }
        });

        btnGroupEmployee.add(licensedTrue);
        licensedTrue.setText("Sim");
        licensedTrue.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                licensedTrueActionPerformed(evt);
            }
        });

        jLabel4.setText("Data de Nascimento:");

        employeeBirthdate.setDateFormatString("dd/MM/yyyy");
        employeeBirthdate.setMaxSelectableDate(new java.util.Date(1609473693000L));

        btnSaveRegisterEmployee.setText("Salvar");
        btnSaveRegisterEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveRegisterEmployeeActionPerformed(evt);
            }
        });

        btnCancelRegisterEmployee.setText("Cancelar");
        btnCancelRegisterEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelRegisterEmployeeActionPerformed(evt);
            }
        });

        jLabel1.setText("Função:");

        employeeTypeSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Suporte", "Motorista", "Gerente" }));
        employeeTypeSelector.setSelectedIndex(-1);
        employeeTypeSelector.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeeTypeSelectorMouseClicked(evt);
            }
        });
        employeeTypeSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeTypeSelectorActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel4)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(employeeBirthdate, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createSequentialGroup()
                        .addComponent(jLabel2)
                        .addGap(18, 18, 18)
                        .addComponent(nameEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 350, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel3)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                            .addComponent(licensedTrue)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(licensedFalse))
                        .addGroup(javax.swing.GroupLayout.Alignment.LEADING, jPanel5Layout.createSequentialGroup()
                            .addComponent(jLabel1)
                            .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                            .addComponent(employeeTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addContainerGap(235, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel5Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnSaveRegisterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(btnCancelRegisterEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(195, 195, 195))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel2)
                    .addComponent(nameEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel1)
                    .addComponent(employeeTypeSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(3, 3, 3)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel3)
                    .addComponent(licensedTrue)
                    .addComponent(licensedFalse))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(employeeBirthdate, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jLabel4, javax.swing.GroupLayout.PREFERRED_SIZE, 22, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveRegisterEmployee)
                    .addComponent(btnCancelRegisterEmployee))
                .addGap(28, 28, 28))
        );

        btnNewEmployee.setText("Novo");
        btnNewEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewEmployeeActionPerformed(evt);
            }
        });

        btnEditEmployee.setText("Editar");
        btnEditEmployee.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                btnEditEmployeeMouseClicked(evt);
            }
        });
        btnEditEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditEmployeeActionPerformed(evt);
            }
        });

        btnDeleteEmployee.setText("Excluir");
        btnDeleteEmployee.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteEmployeeActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane2)
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel2Layout.createSequentialGroup()
                .addContainerGap(114, Short.MAX_VALUE)
                .addComponent(btnNewEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEditEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteEmployee, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
        );
        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewEmployee)
                    .addComponent(btnEditEmployee)
                    .addComponent(btnDeleteEmployee))
                .addGap(12, 12, 12)
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(201, Short.MAX_VALUE))
        );

        calculateCarbonTab.addTab("Funcionarios", jPanel2);

        vehicleTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null},
                {null, null, null, null}
            },
            new String [] {
                "ID", "Modelo", "Ano", "Consumo"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.Integer.class, java.lang.String.class, java.lang.Integer.class, java.lang.Double.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        vehicleTable.getTableHeader().setReorderingAllowed(false);
        vehicleTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                vehicleTableClick(evt);
            }
        });
        jScrollPane3.setViewportView(vehicleTable);
        if (vehicleTable.getColumnModel().getColumnCount() > 0) {
            vehicleTable.getColumnModel().getColumn(0).setResizable(false);
            vehicleTable.getColumnModel().getColumn(1).setResizable(false);
            vehicleTable.getColumnModel().getColumn(2).setResizable(false);
            vehicleTable.getColumnModel().getColumn(3).setResizable(false);
        }

        btnNewVehicle.setText("Novo");
        btnNewVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewVehicleActionPerformed(evt);
            }
        });

        btnDeleteVehicle.setText("Excluir");
        btnDeleteVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteVehicleActionPerformed(evt);
            }
        });

        btnEditVehicle.setText("Editar");
        btnEditVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditVehicleActionPerformed(evt);
            }
        });

        jPanel7.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Cadastro de Veiculo"));

        jLabel6.setText("Modelo:");

        jLabel8.setText("Ano:");

        yearVehicleSelector.setEndYear(2022);
        yearVehicleSelector.setMinimumSize(new java.awt.Dimension(72, 22));
        yearVehicleSelector.setPreferredSize(new java.awt.Dimension(72, 22));

        modelVehicleSelector.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "Strada", "Fiorino", "Uno", "Pálio", "Logan" }));
        modelVehicleSelector.setSelectedIndex(-1);
        modelVehicleSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                modelVehicleSelectorActionPerformed(evt);
            }
        });

        btnSaveRegisterVehicle.setText("Salvar");
        btnSaveRegisterVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveRegisterVehicleActionPerformed(evt);
            }
        });

        btnCancelRegisterVehicle.setText("Cancelar");
        btnCancelRegisterVehicle.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelRegisterVehicleActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel7Layout = new javax.swing.GroupLayout(jPanel7);
        jPanel7.setLayout(jPanel7Layout);
        jPanel7Layout.setHorizontalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel6)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel7Layout.createSequentialGroup()
                        .addComponent(modelVehicleSelector, javax.swing.GroupLayout.PREFERRED_SIZE, 77, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(64, 64, 64)
                        .addComponent(btnSaveRegisterVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelRegisterVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(yearVehicleSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel7Layout.setVerticalGroup(
            jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel7Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel6)
                    .addComponent(modelVehicleSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(yearVehicleSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 12, Short.MAX_VALUE)
                .addGroup(jPanel7Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnSaveRegisterVehicle)
                    .addComponent(btnCancelRegisterVehicle))
                .addContainerGap())
        );

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane3)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel3Layout.createSequentialGroup()
                .addContainerGap(114, Short.MAX_VALUE)
                .addComponent(btnNewVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEditVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteVehicle, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(85, 85, 85))
            .addComponent(jPanel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewVehicle)
                    .addComponent(btnEditVehicle)
                    .addComponent(btnDeleteVehicle))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel7, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap())
        );

        calculateCarbonTab.addTab("Veiculos", jPanel3);

        ticketTable.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null},
                {null, null, null, null, null, null, null}
            },
            new String [] {
                "ID", "Endereço", "Tempo", "Quilometragem", "Funcionario", "Veiculo", "CG"
            }
        ) {
            Class[] types = new Class [] {
                java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.String.class, java.lang.Object.class, java.lang.Object.class, java.lang.String.class
            };
            boolean[] canEdit = new boolean [] {
                false, false, false, false, false, false, false
            };

            public Class getColumnClass(int columnIndex) {
                return types [columnIndex];
            }

            public boolean isCellEditable(int rowIndex, int columnIndex) {
                return canEdit [columnIndex];
            }
        });
        ticketTable.getTableHeader().setReorderingAllowed(false);
        ticketTable.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                ticketTableMouseClicked(evt);
            }
        });
        jScrollPane4.setViewportView(ticketTable);
        if (ticketTable.getColumnModel().getColumnCount() > 0) {
            ticketTable.getColumnModel().getColumn(0).setResizable(false);
            ticketTable.getColumnModel().getColumn(1).setResizable(false);
            ticketTable.getColumnModel().getColumn(2).setResizable(false);
            ticketTable.getColumnModel().getColumn(3).setResizable(false);
            ticketTable.getColumnModel().getColumn(4).setResizable(false);
            ticketTable.getColumnModel().getColumn(5).setResizable(false);
            ticketTable.getColumnModel().getColumn(6).setResizable(false);
        }

        jPanel1.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(0, 0, 0)), "Cadastro de Chamado"));

        jLabel10.setText("Endereço:");

        jLabel11.setText("Tempo:");

        jLabel12.setText("Quilometragem:");

        jLabel13.setText("Funcionario:");

        jLabel14.setText("Veiculo:");

        employeeTicketSelector.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                employeeTicketSelectorMouseClicked(evt);
            }
        });
        employeeTicketSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                employeeTicketSelectorActionPerformed(evt);
            }
        });

        vehicleTicketSelector.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                vehicleTicketSelectorActionPerformed(evt);
            }
        });

        btnSaveRegisterTicket.setText("Salvar");
        btnSaveRegisterTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnSaveRegisterTicketActionPerformed(evt);
            }
        });

        btnCancelRegisterTicket.setText("Cancelar");
        btnCancelRegisterTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnCancelRegisterTicketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel12)
                    .addComponent(jLabel10)
                    .addComponent(jLabel13)
                    .addComponent(jLabel14)
                    .addComponent(jLabel11))
                .addGap(18, 18, 18)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(btnSaveRegisterTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnCancelRegisterTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(216, 216, 216))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(kmTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 406, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(timeTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 100, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(adressTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 450, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(employeeTicketSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(vehicleTicketSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addContainerGap(84, Short.MAX_VALUE))))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel10)
                    .addComponent(adressTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel11)
                    .addComponent(timeTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(jLabel12)
                    .addComponent(kmTicket, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(btnSaveRegisterTicket)
                            .addComponent(btnCancelRegisterTicket)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(employeeTicketSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel13))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(vehicleTicketSelector, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addGap(0, 22, Short.MAX_VALUE)))
                .addContainerGap())
        );

        btnNewTicket.setText("Novo");
        btnNewTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNewTicketActionPerformed(evt);
            }
        });

        btnEditTicket.setText("Editar");
        btnEditTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEditTicketActionPerformed(evt);
            }
        });

        btnDeleteTicket.setText("Excluir");
        btnDeleteTicket.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnDeleteTicketActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel4Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(btnNewTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnEditTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(btnDeleteTicket, javax.swing.GroupLayout.PREFERRED_SIZE, 140, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(88, 88, 88))
            .addComponent(jScrollPane4)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jScrollPane4, javax.swing.GroupLayout.PREFERRED_SIZE, 90, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnNewTicket)
                    .addComponent(btnEditTicket)
                    .addComponent(btnDeleteTicket))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(jPanel1, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 169, Short.MAX_VALUE))
        );

        calculateCarbonTab.addTab("Chamados", jPanel4);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(calculateCarbonTab, javax.swing.GroupLayout.PREFERRED_SIZE, 655, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(calculateCarbonTab, javax.swing.GroupLayout.PREFERRED_SIZE, 535, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void calculateCarbonTabMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_calculateCarbonTabMouseClicked

    }//GEN-LAST:event_calculateCarbonTabMouseClicked

    private void btnDeleteTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteTicketActionPerformed
        try {
            int selected = ticketTable.getSelectedRow();
            TicketDAO dao = new TicketDAO();
            ArrayList<Ticket> ticketList = dao.findAll();
            if (selected >= 0 && selected < ticketList.size()) {
                for (int i = 0; i < ticketList.size(); i++) {
                    Ticket ticket = ticketList.get(i);
                    adressTicket.removeAll();
                    timeTicket.removeAll();
                    kmTicket.removeAll();
                    ticketList.remove(selected);
                    ticketList.remove(dao.delete(ticket));
                    showAllTickets();
                    refreshTicketContent();
                }

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        modeEmployee = "Home";
        changeInterfaceButtonsEmployee();
    }//GEN-LAST:event_btnDeleteTicketActionPerformed

    private void btnEditTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditTicketActionPerformed
        modeTicket = "Edit";
        changeInterfaceButtonsTicket();
    }//GEN-LAST:event_btnEditTicketActionPerformed

    private void btnNewTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewTicketActionPerformed
        modeTicket = "New";
        changeInterfaceButtonsTicket();
    }//GEN-LAST:event_btnNewTicketActionPerformed

    private void btnCancelRegisterTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelRegisterTicketActionPerformed
        modeTicket = "Home";
        changeInterfaceButtonsTicket();
    }//GEN-LAST:event_btnCancelRegisterTicketActionPerformed

    private void btnSaveRegisterTicketActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveRegisterTicketActionPerformed
        try {
            if (modeTicket.equals("New")) {
                TicketDAO ticketDAO = new TicketDAO();
                EmployeeDAO employeeDAO = new EmployeeDAO();
                VehicleDAO vehicleDAO = new VehicleDAO();

                ArrayList<Employee> employees = employeeDAO.findAll();
                ArrayList<Vehicle> vehicles = vehicleDAO.findAll();

                Employee employee = employees.get(employeeTicketSelector.getSelectedIndex());
                Vehicle vehicle = vehicles.get(vehicleTicketSelector.getSelectedIndex());

                ticketDAO.insert(new Ticket(
                        0,
                        adressTicket.getText(),
                        timeTicket.getText(),
                        Double.parseDouble(kmTicket.getText()),
                        employee,
                        vehicle
                ));

                showAllTickets();
                modeTicket = "Home";
                changeInterfaceButtonsTicket();
            } else if (modeTicket.equals("Edit")) {
                int selected = ticketTable.getSelectedRow();
                TicketDAO ticketDAO = new TicketDAO();
                EmployeeDAO employeeDAO = new EmployeeDAO();
                VehicleDAO vehicleDAO = new VehicleDAO();

                ArrayList<Employee> employees = employeeDAO.findAll();
                ArrayList<Vehicle> vehicles = vehicleDAO.findAll();

                Employee employee = employees.get(employeeTicketSelector.getSelectedIndex());
                Vehicle vehicle = vehicles.get(vehicleTicketSelector.getSelectedIndex());

                ArrayList<Ticket> ticketList = ticketDAO.findAll();

                Ticket ticket = ticketList.get(selected);

                ticket.setAddress(adressTicket.getText());
                ticket.setTime(timeTicket.getText());
                ticket.setKm(Double.parseDouble(kmTicket.getText()));

                ticket.setEmployee(employee);
                ticket.setVehicle(vehicle);

                ticketDAO.update(ticket);
                showAllTickets();
                refreshTicketContent();
                modeTicket = "Home";
                changeInterfaceButtonsTicket();

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveRegisterTicketActionPerformed

    private void vehicleTicketSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_vehicleTicketSelectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_vehicleTicketSelectorActionPerformed

    private void employeeTicketSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeTicketSelectorActionPerformed

    }//GEN-LAST:event_employeeTicketSelectorActionPerformed

    private void employeeTicketSelectorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeTicketSelectorMouseClicked
        // TODO add your handling code here:
    }//GEN-LAST:event_employeeTicketSelectorMouseClicked

    private void ticketTableMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_ticketTableMouseClicked
        try {
            int selected = ticketTable.getSelectedRow();
            TicketDAO dao = new TicketDAO();
            ArrayList<Ticket> ticketList = dao.findAll();
            if (selected >= 0 && selected < ticketList.size()) {
                for (int i = 0; i < ticketList.size(); i++) {
                    Ticket ticket = ticketList.get(i);
                    adressTicket.setText(ticket.getAddress());// '-'
                    timeTicket.setText(ticket.getTime());// '-'
                    kmTicket.setText(ticket.getKm().toString());
                    employeeTicketSelector.setSelectedItem(ticket.getEmployee());
                    vehicleTicketSelector.setSelectedItem(ticket.getVehicle());
                    modeTicket = "Select";
                    changeInterfaceButtonsTicket();
                }
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }//GEN-LAST:event_ticketTableMouseClicked

    private void btnCancelRegisterVehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelRegisterVehicleActionPerformed
        modeVehicle = "Home";
        changeInterfaceButtonsVehicle();
    }//GEN-LAST:event_btnCancelRegisterVehicleActionPerformed

    private void btnSaveRegisterVehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveRegisterVehicleActionPerformed
        try {
            if (modeVehicle.equals("New")) {
                VehicleDAO vehicleDAO = new VehicleDAO();
                VehicleType type = VehicleType.get(modelVehicleSelector.getSelectedItem().toString());

                vehicleDAO.insert(new Vehicle(
                        0,
                        type,
                        yearVehicleSelector.getYear(),
                        type.getConsumption()
                ));

                modeVehicle = "Home";
                changeInterfaceButtonsVehicle();
                showAllVehicles();
                showAllTickets();
                showAllEmployees();
                refreshTicketContent();
            } else if (modeVehicle.equals("Edit")) {
                int selected = vehicleTable.getSelectedRow();
                VehicleDAO vehicleDAO = new VehicleDAO();
                ArrayList<Vehicle> vehicleList = vehicleDAO.findAll();

                Vehicle vehicle = vehicleList.get(selected);

                VehicleType type = VehicleType.get(modelVehicleSelector.getSelectedItem().toString());
                Integer year = yearVehicleSelector.getValue();

                vehicle.setType(type);
                if (year != 0) {
                    vehicle.setYear(year);
                }

                vehicleDAO.update(vehicle);
                showAllVehicles();
                showAllTickets();
                showAllEmployees();
                refreshTicketContent();
                modeVehicle = "Home";
                changeInterfaceButtonsVehicle();

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveRegisterVehicleActionPerformed

    private void modelVehicleSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_modelVehicleSelectorActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_modelVehicleSelectorActionPerformed

    private void btnEditVehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditVehicleActionPerformed
        modeVehicle = "Edit";
        changeInterfaceButtonsVehicle();
    }//GEN-LAST:event_btnEditVehicleActionPerformed

    private void btnDeleteVehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteVehicleActionPerformed
        try {
            int selected = vehicleTable.getSelectedRow(); // funcionou no employee porem nao funcionou direito ta meio bug aqui nao entendi
            VehicleDAO dao = new VehicleDAO();
            ArrayList<Vehicle> vehicleList = dao.findAll();
            if (selected >= 0 && selected < vehicleList.size()) {
                for (int i = 0; i < vehicleList.size(); i++) {
                    Vehicle vehicle = vehicleList.get(i);
                    modelVehicleSelector.removeAll();
                    yearVehicleSelector.setEnabled(false);
                    dao.delete(vehicle);
                    vehicleList.remove(vehicle);
                    showAllVehicles();
                }

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        modeVehicle = "Home";
        changeInterfaceButtonsVehicle();
    }//GEN-LAST:event_btnDeleteVehicleActionPerformed

    private void btnNewVehicleActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewVehicleActionPerformed
        modeVehicle = "New";
        changeInterfaceButtonsVehicle();
    }//GEN-LAST:event_btnNewVehicleActionPerformed

    private void vehicleTableClick(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_vehicleTableClick
        
        int selected = vehicleTable.getSelectedRow();
        
        //System.out.println(vehicleTable.getValueAt(selected, 1));
        if (vehicleTable.getValueAt(selected, 1) != null) {
            modeVehicle = "Select";
            changeInterfaceButtonsVehicle();
            modelVehicleSelector.setSelectedItem(vehicleTable.getValueAt(selected, 1));// modelo
            yearVehicleSelector.setValue(Integer.parseInt(vehicleTable.getValueAt(selected, 2).toString()));// ano
        }


    }//GEN-LAST:event_vehicleTableClick

    private void btnDeleteEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnDeleteEmployeeActionPerformed
        try {
            int selected = employeeTable.getSelectedRow();
            EmployeeDAO dao = new EmployeeDAO();
            ArrayList<Employee> employeesList = dao.findAll();
            if (selected >= 0 && selected < employeesList.size()) {
                for (int i = 0; i < employeesList.size(); i++) {
                    Employee employee = employeesList.get(i);
                    employeeTypeSelector.removeAll();
                    employeesList.remove(selected);
                    employeesList.remove(dao.delete(employee));

                    showAllEmployees();
                }

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        modeEmployee = "Home";
        changeInterfaceButtonsEmployee();
    }//GEN-LAST:event_btnDeleteEmployeeActionPerformed

    private void btnEditEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEditEmployeeActionPerformed
        modeEmployee = "Edit";
        changeInterfaceButtonsEmployee();
    }//GEN-LAST:event_btnEditEmployeeActionPerformed

    private void btnEditEmployeeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_btnEditEmployeeMouseClicked
        // TODO add y21our handling code here:
    }//GEN-LAST:event_btnEditEmployeeMouseClicked

    private void btnNewEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNewEmployeeActionPerformed
        modeEmployee = "New";
        changeInterfaceButtonsEmployee();
        btnGroupEmployee.clearSelection();
    }//GEN-LAST:event_btnNewEmployeeActionPerformed

    private void employeeTypeSelectorActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_employeeTypeSelectorActionPerformed
        if (EmployeeType.get(employeeTypeSelector.getSelectedItem().toString()) == EmployeeType.DRIVER) { // setar para caso SUPPORT seja escolhido a opção de habilitado true fique desativada
            licensedTrue.setEnabled(false);
            licensedTrue.setSelected(true);
            licensedFalse.setEnabled(false);
            licensedFalse.setSelected(false);
            return;
        }

        if (EmployeeType.get(employeeTypeSelector.getSelectedItem().toString()) == EmployeeType.SUPPORT) { // setar para caso SUPPORT seja escolhido a opção de habilitado true fique desativada
            licensedTrue.setEnabled(false);
            licensedTrue.setSelected(false);
            licensedFalse.setEnabled(false);
            licensedFalse.setSelected(true);
        } else {
            licensedTrue.setEnabled(true);
            licensedFalse.setEnabled(true);
            btnGroupEmployee.clearSelection();
        }
    }//GEN-LAST:event_employeeTypeSelectorActionPerformed

    private void employeeTypeSelectorMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeTypeSelectorMouseClicked

    }//GEN-LAST:event_employeeTypeSelectorMouseClicked

    private void btnCancelRegisterEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnCancelRegisterEmployeeActionPerformed
        modeEmployee = "Home";
        changeInterfaceButtonsEmployee();
    }//GEN-LAST:event_btnCancelRegisterEmployeeActionPerformed

    private void btnSaveRegisterEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnSaveRegisterEmployeeActionPerformed
        try {
            if (modeEmployee.equals("New")) {
                EmployeeDAO employeeDAO = new EmployeeDAO();
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
                EmployeeType type = EmployeeType.get(employeeTypeSelector.getSelectedItem().toString());

                employeeDAO.insert(new Employee(
                        0,
                        nameEmployee.getText(),
                        type,
                        simpleDateFormat.format(employeeBirthdate.getCalendar().getTime()),
                        licensedTrue.isSelected()
                ));

                showAllEmployees();
                modeEmployee = "Home";
                changeInterfaceButtonsEmployee();
                refreshTicketContent();
            } else if (modeEmployee.equals("Edit")) {
                int selected = employeeTable.getSelectedRow();

                EmployeeDAO employeeDAO = new EmployeeDAO();
                ArrayList<Employee> employeeList = employeeDAO.findAll();

                Employee employee = employeeList.get(selected);
                EmployeeType type = EmployeeType.get(employeeTypeSelector.getSelectedItem().toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");

                employee.setName(nameEmployee.getText());
                employee.setType(type);
                employee.setLicensed(licensedTrue.isSelected());
                employee.setBirthDate(simpleDateFormat.format(employeeBirthdate.getCalendar().getTime()));

                employeeDAO.update(employee);

                showAllVehicles();
                showAllTickets();
                showAllEmployees();

                modeEmployee = "Home";
                changeInterfaceButtonsEmployee();
                refreshTicketContent();

            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
    }//GEN-LAST:event_btnSaveRegisterEmployeeActionPerformed

    private void licensedTrueActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_licensedTrueActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_licensedTrueActionPerformed

    private void licensedFalseActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_licensedFalseActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_licensedFalseActionPerformed

    private void nameEmployeeActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nameEmployeeActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_nameEmployeeActionPerformed

    private void employeeTableClick(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_employeeTableClick
        try {
            int selected = employeeTable.getSelectedRow();
            EmployeeDAO dao = new EmployeeDAO();
            ArrayList<Employee> employeesList = dao.findAll();

            if (selected >= 0 && selected < employeesList.size()) {
                for (int i = 0; i < employeesList.size(); i++) {
                    Employee employee = employeesList.get(i);
                }
                Employee employee = employeesList.get(selected);
                System.out.println(employee.getType().getName());
                employeeTypeSelector.setSelectedItem(employee.getType().getName());
                //employeeTypeSelector.setSelectedItem(employee.getType());
                nameEmployee.setText(employee.getName());
                licensedTrue.isSelected();
                var licensed = employee.getLicensed();
                if (licensed) {
                    licensedTrue.setSelected(true);
                    licensedFalse.setSelected(false);
                } else {
                    licensedTrue.setSelected(false);
                    licensedFalse.setSelected(true);
                }
                employee.getLicensed();

                Date date = new SimpleDateFormat("dd/MM/yyyy").parse(employee.getBirthDate());

                employeeBirthdate.setDate(date);
                modeEmployee = "Select";
                changeInterfaceButtonsEmployee();
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        } catch (ParseException ex) {
            Logger.getLogger(MenuInicial.class.getName()).log(Level.SEVERE, null, ex);
        }
    }//GEN-LAST:event_employeeTableClick

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Windows".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(MenuInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(MenuInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(MenuInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(MenuInicial.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new MenuInicial().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField adressTicket;
    private javax.swing.JButton btnCancelRegisterEmployee;
    private javax.swing.JButton btnCancelRegisterTicket;
    private javax.swing.JButton btnCancelRegisterVehicle;
    private javax.swing.JButton btnDeleteEmployee;
    private javax.swing.JButton btnDeleteTicket;
    private javax.swing.JButton btnDeleteVehicle;
    private javax.swing.JButton btnEditEmployee;
    private javax.swing.JButton btnEditTicket;
    private javax.swing.JButton btnEditVehicle;
    private javax.swing.ButtonGroup btnGroupEmployee;
    private javax.swing.JButton btnNewEmployee;
    private javax.swing.JButton btnNewTicket;
    private javax.swing.JButton btnNewVehicle;
    private javax.swing.JButton btnSaveRegisterEmployee;
    private javax.swing.JButton btnSaveRegisterTicket;
    private javax.swing.JButton btnSaveRegisterVehicle;
    private javax.swing.JTabbedPane calculateCarbonTab;
    private com.toedter.calendar.JDateChooser employeeBirthdate;
    private javax.swing.JTable employeeTable;
    private javax.swing.JComboBox<String> employeeTicketSelector;
    private javax.swing.JComboBox<String> employeeTypeSelector;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JPanel jPanel7;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem1;
    private javax.swing.JRadioButtonMenuItem jRadioButtonMenuItem2;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JTabbedPane jTabbedPane2;
    private javax.swing.JTextField kmTicket;
    private javax.swing.JRadioButton licensedFalse;
    private javax.swing.JRadioButton licensedTrue;
    private javax.swing.JComboBox<String> modelVehicleSelector;
    private javax.swing.JTextField nameEmployee;
    private javax.swing.JTable ticketTable;
    private javax.swing.JTextField timeTicket;
    private javax.swing.JTable vehicleTable;
    private javax.swing.JComboBox<String> vehicleTicketSelector;
    private com.toedter.calendar.JYearChooser yearVehicleSelector;
    // End of variables declaration//GEN-END:variables
}
