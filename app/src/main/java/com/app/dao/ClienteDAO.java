package com.app.dao;

import com.app.entities.Cliente;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class ClienteDAO {

  private static final Logger logger = LoggerFactory.getLogger(
    ClienteDAO.class
  );
  private final Connection conn;

  public ClienteDAO(Connection conn) {
    this.conn = conn;
  }

  public void insertClientes(List<Cliente> clientes) {
    String query =
      "INSERT INTO Cliente (idCliente, nombre, email) VALUES (?, ?, ?)";
    PreparedStatement ps = null;

    try {
      conn.setAutoCommit(false); // Desactivar auto-commit para manejar la transacción manualmente
      ps = conn.prepareStatement(query);

      for (Cliente cliente : clientes) {
        ps.setInt(1, cliente.getIdCliente());
        ps.setString(2, cliente.getNombre());
        ps.setString(3, cliente.getEmail());
        ps.addBatch(); // Agregar la instrucción al batch
        logger.info(
          "Cliente agregado al batch: " +
          cliente.getIdCliente() +
          ", " +
          cliente.getNombre() +
          ", " +
          cliente.getEmail()
        );
      }

      ps.executeBatch(); // Ejecutar todas las instrucciones en el batch
      conn.commit(); // Confirmar la transacción
      logger.info("Lista de clientes insertada exitosamente.");
    } catch (SQLException e) {
      logger.error("Error al insertar clientes: ", e); // Registrar el error
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

  public List<Cliente> getClientesMasFacturados() {
    // Consulta SQL para obtener la facturación total por cliente
    String query =
      "SELECT c.idCliente, c.nombre, email, SUM(fp.cantidad * p.valor) AS total_facturado " +
      "FROM Cliente c " +
      "JOIN Factura f ON c.idCliente = f.idCliente " +
      "JOIN Factura_Producto fp ON f.idFactura = fp.idFactura " +
      "JOIN Producto p ON fp.idProducto = p.idProducto " +
      "GROUP BY c.idCliente, c.nombre " +
      "ORDER BY total_facturado DESC";

    PreparedStatement ps = null;
    List<Cliente> clientes = new ArrayList<>();
    try {
      conn.setAutoCommit(false); // Desactivar auto-commit para manejar la transacción manualmente
      ps = conn.prepareStatement(query);
      try (ResultSet rs = ps.executeQuery()) {
        while (rs.next()) {
          int idCliente = rs.getInt("idCliente");
          String nombre = rs.getString("nombre");
          String email = rs.getString("email");
          Double totalFacturado = rs.getDouble("total_facturado");

          Cliente cliente = new Cliente(
            idCliente,
            nombre,
            email,
            totalFacturado
          );
          clientes.add(cliente);
        }
      }
      conn.commit(); // Confirmar la transacción
      logger.info(
        "Consulta de clientes más facturados ejecutada exitosamente."
      );
    } catch (SQLException e) {
      logger.error("Error al obtener clientes más facturados: ", e); // Registrar el error
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
    return clientes;
  }
  // Métodos adicionales (crear, actualizar, eliminar, listar, etc.) se pueden agregar según las necesidades.
}
