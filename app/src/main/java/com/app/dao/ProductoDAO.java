package com.app.dao;

import com.app.entities.Producto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ProductoDAO {

  private static final Logger logger = LoggerFactory.getLogger(
    ProductoDAO.class
  );
  private final Connection conn;

  public ProductoDAO(Connection conn) {
    this.conn = conn;
  }

  public Producto getProductoMasRecaudador() {
    Producto producto = null;
    String sql =
      "SELECT p.idProducto, p.nombre, p.valor, (SUM(fp.cantidad) * p.valor) AS recaudacion " +
      "FROM Producto p " +
      "JOIN Factura_Producto fp ON p.idProducto = fp.idProducto " +
      "GROUP BY p.idProducto, p.nombre, p.valor " +
      "ORDER BY recaudacion DESC " +
      "LIMIT 1";

    PreparedStatement ps = null;

    try {
      conn.setAutoCommit(false); // Desactivar auto-commit para manejar la transacción manualmente

      ps = conn.prepareStatement(sql);

      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          int idProducto = rs.getInt("idProducto");
          String nombre = rs.getString("nombre");
          double valor = rs.getDouble("valor");
          double recaudacion = rs.getDouble("recaudacion");

          producto = new Producto(idProducto, nombre, valor, recaudacion);
        }
        conn.commit();
      }
    } catch (SQLException e) {
      logger.error("Error al obtener el producto más recaudador: ", e); // Registrar el error
    } finally {
      if (ps != null) {
        try {
          ps.close();
        } catch (SQLException e) {
          logger.error("Error al cerrar el PreparedStatement: ", e); // Registrar el error
        }
      }
    }
    if (producto != null) {
      logger.info(
        "El producto más recaudador es: " +
        producto.getNombre() +
        " con un valor de: " +
        producto.getRecaudacion()
      );
    } else {
      logger.info("No se encontraron productos.");
    }
    return producto;
  }

  public void insertProducto(Producto producto) {
    String query =
      "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
    PreparedStatement ps = null;

    try {
      ps = conn.prepareStatement(query);
      ps.setInt(1, producto.getIdProducto()); // idPersona
      ps.setString(2, producto.getNombre()); // nombre
      ps.setDouble(3, producto.getValor()); // edad
      ps.executeUpdate();
      conn.commit();
      logger.info(
        "Producto insertado: " +
        producto.getIdProducto() +
        ", " +
        producto.getNombre() +
        ", " +
        producto.getValor()
      );
    } catch (SQLException e) {
      logger.error("Error al insertar el producto: ", e); // Registrar el error
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
      } catch (SQLException e) {
        logger.error("Error al cerrar el PreparedStatement: ", e); // Registrar el error
      }
    }
  }

  public void insertProductos(List<Producto> productos) {
    String query =
      "INSERT INTO Producto (idProducto, nombre, valor) VALUES (?, ?, ?)";
    PreparedStatement ps = null;

    try {
      conn.setAutoCommit(false); // Desactivar auto-commit para manejar la transacción manualmente
      ps = conn.prepareStatement(query);

      for (Producto producto : productos) {
        ps.setInt(1, producto.getIdProducto());
        ps.setString(2, producto.getNombre());
        ps.setDouble(3, producto.getValor());
        ps.addBatch(); // Agregar la instrucción al batch
        logger.info(
          "Producto agregado al batch: " +
          producto.getIdProducto() +
          ", " +
          producto.getNombre() +
          ", " +
          producto.getValor()
        );
      }
      ps.executeBatch(); // Ejecutar todas las instrucciones en el batch
      conn.commit(); // Confirmar la transacción
      logger.info("Lista de productos insertada exitosamente.");
    } catch (SQLException e) {
      logger.error("Error al insertar productos: ", e); // Registrar el error
      try {
        conn.rollback(); // Revertir la transacción en caso de error
      } catch (SQLException rollbackEx) {
        logger.error("Error al revertir la transacción: ", rollbackEx); // Registrar el error
      }
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        conn.setAutoCommit(true); // Restaurar el auto-commit
      } catch (SQLException e) {
        logger.error("Error al cerrar el PreparedStatement: ", e); // Registrar el error
      }
    }
  }
  // Métodos adicionales (crear, actualizar, eliminar, listar, etc.) se pueden agregar según las necesidades.
}
