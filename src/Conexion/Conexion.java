package Conexion;

import java.sql.*;

public class Conexion {

    //constructor 
    private Conexion() {

    }

    private static Connection conexion;
    private static Conexion instancia;

    private static final String URL = "jdbc:mysql://localhost:3306/dbregistroPersonas";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    //nos conectamos a la base de daros 
    public Connection ConecarBD() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            conexion = DriverManager.getConnection(URL, USER, PASSWORD);

            System.out.println("Se conecto a la base de Datos");

            return conexion;
        } catch (ClassNotFoundException | SQLException e) {
            System.out.println("Error al conectar => " + e.getMessage());
        }
        return conexion;
    }

    //metodo para desconectarnos 
    public void CerrarConecion() throws SQLException {
        try {
            conexion.close();
            System.out.println("Se desconecto de la  Bd");
        } catch (SQLException e) {
            System.out.println("Error en Cerrar bd => " + e.getMessage());
            conexion.close();
        } finally {
            conexion.close();
        }

    }

    //PATRON SINGLETON
    public static Conexion getInstancia() {
        if (instancia == null) {
            instancia = new Conexion();
        }
        return instancia;
    }
}
