package Conexion;

import java.awt.HeadlessException;
import java.sql.*;
import javax.swing.JOptionPane;

public class frmRegistros extends javax.swing.JFrame {

    Conexion conectar = Conexion.getInstancia();

    public frmRegistros() {
        initComponents();
        this.setTitle("Bienvenido al sistema");

    }

    private boolean ComprobarRepetidos(String nombre, String apellido, String profesion, String telefono, String residencia) {
        try {
            Connection conexion = conectar.ConecarBD();
            /*
            nombre varchar (50) not null,
            apellido varchar (50) not null,
            profesion varchar (50),
            telefono varchar (50),
            residencia varchar (50)
            select * from personas where nombre ='Juan Alexander' and apellido ='Gomez Peralta' and profesion = 'Ingenieria Electrica' and telefono = '945323212' and residencia ='Ucayali';
             */
            String consulta
                    = "select * from personas where nombre = '" + nombre + "' and apellido ='" + apellido + "' and profesion = '" + profesion + "' and telefono = '" + telefono + "' and residencia ='" + residencia + "'";

            PreparedStatement seleccion = conexion.prepareStatement(consulta);

            ResultSet consultando = seleccion.executeQuery();

            if (consultando.next()) {
                return true;
            }

            this.conectar.CerrarConecion();
        } catch (SQLException e) {
            System.out.println("Error al comprovar Repetidos " + e.getMessage());
        }
        return false;

    }

    public void RegistraraBD() {
        try {
            Connection conexion = conectar.ConecarBD();
            conectar.ConecarBD();
        } catch (Exception e) {
            System.out.println("Error al conectar");
        }
    }

    private boolean ComprovarCampos() {
        return txtApeliidos.getText().isEmpty()
                || txtNombres.getText().isEmpty()
                || txtTelefono.getText().isEmpty()
                || cbxDepartamento.getSelectedItem().equals("=Seleccionar=")
                || cbxProfesion.getSelectedItem().equals("=Seleccionar=");

    }

    private void LimoiarCampos() {
        txtApeliidos.setText(null);
        txtNombres.setText(null);
        txtTelefono.setText(null);
        cbxDepartamento.setSelectedIndex(0);
        cbxProfesion.setSelectedIndex(0);
        txtNombres.requestFocus();
        txtBuscarID.setText(null);
    }

    private boolean ComprobarTele(String telefono) {
        return telefono.length() == 9;
    }

    public boolean VerificarTablaPersonas() {
        try {
            Connection conexion = conectar.ConecarBD();
            String consulta = "Select * from personas";
            PreparedStatement seleccionar = conexion.prepareStatement(consulta);

            ResultSet consultar = seleccionar.executeQuery();
            if (consultar.next()) {
                //JOptionPane.showMessageDialog(rootPane, "La tabla Personas si Tiene datos", "MI BASE DE DATOS", JOptionPane.WARNING_MESSAGE);
                conectar.CerrarConecion();

                return true;
            }

        } catch (HeadlessException | SQLException e) {
            System.out.println("Error al momento de verificar => " + e.getMessage());
        }

        return false;
    }

    private void RegistrarDatos() {
        String nombre = null;
        String apellido = null;
        String profesion = null;
        String telefono = null;
        String residencia = null;

        try {
            Connection conexion = conectar.ConecarBD();
            String insert = "insert into personas values (?,?,?,?,?,?) ";
            PreparedStatement insertar = conexion.prepareStatement(insert);
            if (ComprovarCampos() == true) {
                JOptionPane.showMessageDialog(rootPane, "POR FAVOR COMPLETAR LOS CAMPOS", "UPSS", JOptionPane.WARNING_MESSAGE);
            } else {
                nombre = txtNombres.getText();
                apellido = txtApeliidos.getText();
                telefono = txtTelefono.getText();
                profesion = cbxProfesion.getSelectedItem().toString();
                residencia = cbxDepartamento.getSelectedItem().toString();

                if (!ComprobarTele(telefono)) {
                    JOptionPane.showMessageDialog(rootPane, "El telefono debe ser de 9 digitos");
                } else if (ComprobarRepetidos(nombre, apellido, profesion, telefono, residencia) == true) {
                    JOptionPane.showMessageDialog(rootPane, "Registros repetidos /Ya hay uno en la base de Datos");
                    LimoiarCampos();
                } else {
                    insertar.setString(1, "0");
                    insertar.setString(2, nombre);
                    insertar.setString(3, apellido);
                    insertar.setString(4, profesion);
                    insertar.setString(5, telefono);
                    insertar.setString(6, residencia);

                    //ejecutamos la consulta
                    insertar.executeUpdate();

                    LimoiarCampos();
                    JOptionPane.showMessageDialog(rootPane, "Registrado correctamente", "Perfecto", JOptionPane.INFORMATION_MESSAGE);

                }

            }
            conectar.CerrarConecion();
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error al registrar datos => " + e.getMessage());
        }
    }

    private void VerRegistros() {
        taDatos.setText(null);
        try {
            if (VerificarTablaPersonas() == true) {
                JOptionPane.showMessageDialog(null, "La tabla Personas si Tiene Datos", "UPSS", JOptionPane.WARNING_MESSAGE);

                Connection conexion = conectar.ConecarBD();
                String consulta = "select * from personas";
                PreparedStatement seleccion = conexion.prepareStatement(consulta);
                ResultSet consut = seleccion.executeQuery();

                while (consut.next()) {
                    taDatos.append(consut.getString(1));
                    taDatos.append("     ");
                    taDatos.append(consut.getString(2));
                    taDatos.append("     ");
                    taDatos.append(consut.getString(3));
                    taDatos.append("     ");
                    taDatos.append(consut.getString(4));
                    taDatos.append("     ");
                    taDatos.append(consut.getString(5));
                    taDatos.append("     ");
                    taDatos.append(consut.getString(6));
                    taDatos.append("     ");
                    taDatos.append("\n");

                }

                conectar.CerrarConecion();
            } else {
                JOptionPane.showMessageDialog(rootPane, "La tabla Personas no Tiene Datos", "UPSS", JOptionPane.WARNING_MESSAGE);
            }
        } catch (HeadlessException | SQLException e) {
            System.out.println("Error al Momento de Actualizar los datos => " + e.getMessage());
        }
    }

    private boolean ComprobarID(int id) {
        try {
            Connection conexion = conectar.ConecarBD();
            String select = "select * from personas where id = ?";

            PreparedStatement consulta = conexion.prepareStatement(select);
            consulta.setInt(1, id);
            ResultSet buscar = consulta.executeQuery();
            if (buscar.next()) {
                return true;
            }
            conectar.CerrarConecion();
        } catch (SQLException e) {
            System.out.println("Error al comprobar Id: ");
        }
        return false;
    }

    private void eliminarRegistro() {
        try {
            int id;
            if (txtBuscarID.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "DIGITAR ID", "UPSS", JOptionPane.WARNING_MESSAGE);
            } else {
                id = Integer.parseInt(txtBuscarID.getText());
                if (ComprobarID(id) == false) {
                    JOptionPane.showMessageDialog(rootPane, "El Regitro con el id " + id
                            + "\n No existe ", "Alerta", JOptionPane.WARNING_MESSAGE);
                } else {
                    Connection conexion = conectar.ConecarBD();
                    String eliminarConsulta = "delete from personas where id = ?";
                    PreparedStatement eliminar = conexion.prepareStatement(eliminarConsulta);

                    eliminar.setString(1, id + "");
                    eliminar.executeUpdate();
                    LimoiarCampos();
                    JOptionPane.showMessageDialog(rootPane, "El registro con el ID = " + id + " a sido eliminado");
                }

                conectar.CerrarConecion();
            }

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            System.out.println("Error al eliminar => " + e.getMessage());
        }
    }

    private void ModificarRegistro() {
        int id;
        String nombres, apellidos, telefono, profesion, residencia;
        try {
            if (txtBuscarID.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Por favor ingresar el Id", "UPSS", JOptionPane.WARNING_MESSAGE);
            } else {
                id = Integer.parseInt(txtBuscarID.getText());

                if (ComprobarID(id) == true) {
                    Connection conexion = conectar.ConecarBD();
                    String modificar = "update personas set nombre = ?, apellido = ?, telefono = ? , profesion = ?, residencia = ? where id =   " + id;
                    PreparedStatement modificando = conexion.prepareStatement(modificar);
                    nombres = txtNombres.getText();
                    apellidos = txtApeliidos.getText();
                    telefono = txtTelefono.getText();
                    profesion = cbxProfesion.getSelectedItem().toString();
                    residencia = cbxDepartamento.getSelectedItem().toString();

                    if (ComprovarCampos() == true) {
                        JOptionPane.showMessageDialog(rootPane, "POR FAVOR COMPLETAR LOS CAMPOS", "UPSS", JOptionPane.WARNING_MESSAGE);
                    } else if (!ComprobarTele(telefono) == true) {
                        JOptionPane.showMessageDialog(rootPane, "El telefono debe ser de 9 digitos");
                    } else {
                        modificando.setString(1, nombres);
                        modificando.setString(2, apellidos);
                        modificando.setString(3, telefono);
                        modificando.setString(4, profesion);
                        modificando.setString(5, residencia);

                        //limpiamos los campos 
                        LimoiarCampos();
                        modificando.executeUpdate();
                        JOptionPane.showMessageDialog(rootPane, "Registro con ID => " + id + " Modificado Exictosamente");

                        conectar.CerrarConecion();
                    }

                } else {
                    JOptionPane.showMessageDialog(rootPane, "Registro con ID => " + id + " no encontrado", "UPSS", JOptionPane.WARNING_MESSAGE);
                }
            }

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            System.out.println("Error en Modificar => " + e.getMessage());
        }
    }

    private void botonBuscar() {
        int id;
        try {

            if (txtBuscarID.getText().trim().isEmpty()) {
                JOptionPane.showMessageDialog(rootPane, "Por favor Completar el Campo de Buscar ID", "UPSS", JOptionPane.WARNING_MESSAGE);
            } else {
                id = Integer.parseInt(txtBuscarID.getText());
                if (ComprobarID(id) == true) {
                    String consulta = "select * from personas where id = ? ";

                    Connection conexion = conectar.ConecarBD();
                    PreparedStatement mostrar = conexion.prepareStatement(consulta);
                    mostrar.setString(1, id + "");

                    ResultSet mostrando = mostrar.executeQuery();

                    while (mostrando.next()) {
                        txtNombres.setText(mostrando.getString("nombre"));
                        cbxProfesion.setSelectedItem(mostrando.getString("profesion"));
                        cbxDepartamento.setSelectedItem(mostrando.getString("residencia"));

                        txtTelefono.setText(mostrando.getString("telefono"));

                        txtApeliidos.setText(mostrando.getString("apellido"));
                    }
                    JOptionPane.showMessageDialog(rootPane, "SE ENCONTRANDOS DATOS DEL REGISTRO CON ID => " + id, "PERFECTO", JOptionPane.WARNING_MESSAGE);

                    conectar.CerrarConecion();
                } else {
                    JOptionPane.showMessageDialog(rootPane, "No se encontraron datos con el ID => " + id);
                }
            }

        } catch (HeadlessException | NumberFormatException | SQLException e) {
            System.out.println("Error en buscar => " + e.getMessage());
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        btnRegistrar4 = new javax.swing.JButton();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jSeparator1 = new javax.swing.JSeparator();
        txtNombres = new javax.swing.JTextField();
        txtApeliidos = new javax.swing.JTextField();
        cbxProfesion = new javax.swing.JComboBox<>();
        txtTelefono = new javax.swing.JTextField();
        cbxDepartamento = new javax.swing.JComboBox<>();
        btnRegistrar = new javax.swing.JButton();
        btnVerRegistros = new javax.swing.JButton();
        btnModificar = new javax.swing.JButton();
        btnEliminar = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();
        txtBuscarID = new javax.swing.JTextField();
        jScrollPane1 = new javax.swing.JScrollPane();
        taDatos = new javax.swing.JTextArea();
        btnBuscarID = new javax.swing.JButton();

        btnRegistrar4.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        btnRegistrar4.setText("Eliminar");
        btnRegistrar4.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrar4ActionPerformed(evt);
            }
        });

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowOpened(java.awt.event.WindowEvent evt) {
                formWindowOpened(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(204, 204, 255));

        jLabel1.setBackground(new java.awt.Color(51, 51, 255));
        jLabel1.setFont(new java.awt.Font("Cascadia Code", 0, 18)); // NOI18N
        jLabel1.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel1.setText("Registro de Personal");

        txtNombres.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        txtNombres.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(41, 43, 45)), "Nombre:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 14))); // NOI18N

        txtApeliidos.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        txtApeliidos.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(41, 43, 45)), "Apellidos:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 14))); // NOI18N

        cbxProfesion.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=Seleccionar=" }));
        cbxProfesion.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(41, 43, 45)), "Profesión:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 14))); // NOI18N

        txtTelefono.setFont(new java.awt.Font("Consolas", 0, 12)); // NOI18N
        txtTelefono.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(41, 43, 45)), "Telefóno:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 14))); // NOI18N

        cbxDepartamento.setModel(new javax.swing.DefaultComboBoxModel<>(new String[] { "=Seleccionar=" }));
        cbxDepartamento.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createLineBorder(new java.awt.Color(41, 43, 45)), "Departamento:", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Cascadia Code", 0, 14))); // NOI18N

        btnRegistrar.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        btnRegistrar.setText("Registrar");
        btnRegistrar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnRegistrarActionPerformed(evt);
            }
        });

        btnVerRegistros.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        btnVerRegistros.setText("Consultar/Ver Registros");
        btnVerRegistros.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnVerRegistrosActionPerformed(evt);
            }
        });

        btnModificar.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        btnModificar.setText("Modificar");
        btnModificar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnModificarActionPerformed(evt);
            }
        });

        btnEliminar.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        btnEliminar.setText("Eliminar");
        btnEliminar.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnEliminarActionPerformed(evt);
            }
        });

        jLabel2.setFont(new java.awt.Font("Cascadia Code", 0, 14)); // NOI18N
        jLabel2.setText("Buscar Persona por ID:");

        taDatos.setColumns(20);
        taDatos.setRows(5);
        jScrollPane1.setViewportView(taDatos);

        btnBuscarID.setFont(new java.awt.Font("Consolas", 0, 14)); // NOI18N
        btnBuscarID.setText("Buscar POR ID");
        btnBuscarID.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBuscarIDActionPerformed(evt);
            }
        });

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addComponent(jSeparator1)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(23, 23, 23)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(btnRegistrar, javax.swing.GroupLayout.PREFERRED_SIZE, 155, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(btnVerRegistros, javax.swing.GroupLayout.PREFERRED_SIZE, 207, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnModificar, javax.swing.GroupLayout.PREFERRED_SIZE, 168, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnEliminar, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addComponent(btnBuscarID, javax.swing.GroupLayout.PREFERRED_SIZE, 167, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(0, 0, Short.MAX_VALUE))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(txtNombres, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtApeliidos, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(40, 40, 40)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(cbxProfesion, javax.swing.GroupLayout.PREFERRED_SIZE, 271, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 45, Short.MAX_VALUE)
                                .addComponent(cbxDepartamento, javax.swing.GroupLayout.PREFERRED_SIZE, 264, javax.swing.GroupLayout.PREFERRED_SIZE))
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 224, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jLabel2)
                                .addGap(18, 18, 18)
                                .addComponent(txtBuscarID)))
                        .addGap(90, 90, 90))))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.PREFERRED_SIZE, 730, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(66, 66, 66))
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 37, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(txtNombres, javax.swing.GroupLayout.DEFAULT_SIZE, 54, Short.MAX_VALUE)
                    .addComponent(cbxProfesion)
                    .addComponent(cbxDepartamento))
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtApeliidos, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtTelefono, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(21, 21, 21)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(txtBuscarID, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                    .addComponent(btnRegistrar)
                    .addComponent(btnVerRegistros)
                    .addComponent(btnModificar)
                    .addComponent(btnEliminar)
                    .addComponent(btnBuscarID))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 270, Short.MAX_VALUE)
                .addGap(12, 12, 12))
        );

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void formWindowOpened(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowOpened
        // TODO add your handling code here:
        cbxProfesion.addItem("Ingenieria Civil");
        cbxProfesion.addItem("Ingenieria Mecanicó");
        cbxProfesion.addItem("Ingenieria  de Sistemas");
        cbxProfesion.addItem("Ingenieria de Software");
        cbxProfesion.addItem("Ingenieria de Minas");
        cbxProfesion.addItem("Ingeniero Agranómo");
        cbxProfesion.addItem("Ingenierria de Ciencia de Datos");
        cbxProfesion.addItem("Ingenierria de Ciberciguridad");
        cbxProfesion.addItem("Medicó");
        cbxProfesion.addItem("Neurolgo");
        cbxProfesion.addItem("Administración");
        cbxProfesion.addItem("Contabilidad");
        cbxProfesion.addItem("Arquitectura");
        cbxProfesion.addItem("Psicologiá");
        cbxProfesion.addItem("Nutricion");
        cbxProfesion.addItem("Abogado");
        cbxProfesion.addItem("Diseño Graficó");
        cbxProfesion.addItem("Periodista");
        cbxProfesion.addItem("Profesor");
        cbxProfesion.addItem("Mecanico");
        cbxProfesion.addItem("Ingenieria Electrica");
        cbxProfesion.addItem("Ingenieria Espacial");

        cbxDepartamento.addItem("Amazonas");
        cbxDepartamento.addItem("Áncash");
        cbxDepartamento.addItem("Apurímac");
        cbxDepartamento.addItem("Arequipa");
        cbxDepartamento.addItem("Ayacuho");
        cbxDepartamento.addItem("Cajamarca");
        cbxDepartamento.addItem("Callao");
        cbxDepartamento.addItem("Cusco");
        cbxDepartamento.addItem("Huancavelica");
        cbxDepartamento.addItem("Huanuco");
        cbxDepartamento.addItem("Ica");
        cbxDepartamento.addItem("Junín");
        cbxDepartamento.addItem("La libertad");
        cbxDepartamento.addItem("Lambayeque");
        cbxDepartamento.addItem("Lima");
        cbxDepartamento.addItem("Loreto ");
        cbxDepartamento.addItem("Madre de Dios");
        cbxDepartamento.addItem("Moquegua");
        cbxDepartamento.addItem("Pasco");
        cbxDepartamento.addItem("Piura");
        cbxDepartamento.addItem("Puno");
        cbxDepartamento.addItem("Piura");
        cbxDepartamento.addItem("Sam Martín");
        cbxDepartamento.addItem("Tacna");
        cbxDepartamento.addItem("Tumbes");
        cbxDepartamento.addItem("Ucayali");

    }//GEN-LAST:event_formWindowOpened

    private void btnRegistrarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrarActionPerformed
        //RegistraraBD();
        RegistrarDatos();
    }//GEN-LAST:event_btnRegistrarActionPerformed

    private void btnVerRegistrosActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnVerRegistrosActionPerformed
        VerRegistros();
    }//GEN-LAST:event_btnVerRegistrosActionPerformed

    private void btnModificarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnModificarActionPerformed
        ModificarRegistro();
    }//GEN-LAST:event_btnModificarActionPerformed

    private void btnEliminarActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnEliminarActionPerformed
        eliminarRegistro();
    }//GEN-LAST:event_btnEliminarActionPerformed

    private void btnRegistrar4ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnRegistrar4ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_btnRegistrar4ActionPerformed

    private void btnBuscarIDActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBuscarIDActionPerformed
        botonBuscar();

    }//GEN-LAST:event_btnBuscarIDActionPerformed

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
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(frmRegistros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(frmRegistros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(frmRegistros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(frmRegistros.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new frmRegistros().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBuscarID;
    private javax.swing.JButton btnEliminar;
    private javax.swing.JButton btnModificar;
    private javax.swing.JButton btnRegistrar;
    private javax.swing.JButton btnRegistrar4;
    private javax.swing.JButton btnVerRegistros;
    private javax.swing.JComboBox<String> cbxDepartamento;
    private javax.swing.JComboBox<String> cbxProfesion;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JTextArea taDatos;
    private javax.swing.JTextField txtApeliidos;
    private javax.swing.JTextField txtBuscarID;
    private javax.swing.JTextField txtNombres;
    private javax.swing.JTextField txtTelefono;
    // End of variables declaration//GEN-END:variables
}
