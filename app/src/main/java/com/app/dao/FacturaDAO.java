package com.app.dao;

import com.app.entities.Factura;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacturaDAO {

  private static final Logger logger = LoggerFactory.getLogger(
    FacturaDAO.class
  );

  private final Connection conn;

  public FacturaDAO(Connection conn) {
    this.conn = conn;
  }

  public void insertFacturas(List<Factura> facturas) {
    String query = "INSERT INTO Factura (idFactura,idCliente) VALUES (?, ?)";
    PreparedStatement ps = null;

    try {
      conn.setAutoCommit(false); // Desactivar auto-commit para manejar la transacción manualmente
      ps = conn.prepareStatement(query);

      for (Factura factura : facturas) {
        ps.setInt(1, factura.getIdFactura());
        ps.setInt(2, factura.getIdCliente());
        ps.addBatch(); // Agregar la instrucción al batch
      }

      ps.executeBatch(); // Ejecutar todas las instrucciones en el batch
      conn.commit(); // Confirmar la transacción
      logger.info("Lista de facturas insertada exitosamente.");
    } catch (SQLException e) {
      logger.error("Error al insertar facturas: ", e); // Registrar el error
      try {
        conn.rollback(); // Revertir la transacción en caso de error
      } catch (SQLException rollbackEx) {}
    } finally {
      try {
        if (ps != null) {
          ps.close();
        }
        conn.setAutoCommit(true); // Restaurar el auto-commit
      } catch (SQLException e) {
        logger.error("Error al cerrar PreparedStatement: ", e); // Registrar el error
      }
    }
  }
  // Métodos adicionales (crear, actualizar, eliminar, listar, etc.) se pueden agregar según las necesidades.
}
