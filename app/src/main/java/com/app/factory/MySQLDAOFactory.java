package com.app.factory;

import com.app.dao.ClienteDAO;
import com.app.dao.ProductoDAO;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLDAOFactory extends AbstractFactoryDAO {

  private static MySQLDAOFactory INSTANCE = null;

  public static String uri = "jdbc:mysql://mysql:3306/integrador1";

  public static Connection conn;

  private MySQLDAOFactory() {}

  public static synchronized MySQLDAOFactory getInstance() {
    if (INSTANCE == null) {
      INSTANCE = new MySQLDAOFactory();
    }
    return INSTANCE;
  }

  public static String getUri() {
    return uri;
  }

  public Connection createConnection() {
    if (conn != null) {
      return conn;
    }
    try {
      conn = DriverManager.getConnection(uri, "root", "");
      conn.setAutoCommit(false);
      System.out.println("Conexión establecida exitosamente.");
    } catch (SQLException e) {
      // Registrar el error y lanzar una excepción personalizada
      System.err.println(
        "Error al establecer la conexión con la base de datos: " +
        e.getMessage()
      );
      throw new RuntimeException("No se pudo conectar a la base de datos.", e);
    }
    return conn;
  }

  public void closeConnection() {
    try {
      conn.close();
    } catch (SQLException e) {
      System.err.println(
        "Error al intentar cerrar la conexión con la base de datos: " +
        e.getMessage()
      );
      throw new RuntimeException(
        "No se pudo cerrar la conexión con la base de datos.",
        e
      );
    }
  }

  @Override
  public ProductoDAO getProductoDAO() {
    return new ProductoDAO(createConnection());
  }

  @Override
  public ClienteDAO getClienteDAO() {
    return new ClienteDAO(createConnection());
  }
}
