package com.app.dao;

import com.app.entities.FacturaProducto;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FacturaProductoDAO {

  private static final Logger logger = LoggerFactory.getLogger(
    FacturaProductoDAO.class
  );

  private final Connection conn;

  public FacturaProductoDAO(Connection conn) {
    this.conn = conn;
  }

  public void insertFacturaProducto(List<FacturaProducto> shopingCart) {
    String query =
      "INSERT INTO Factura_Producto (idFactura,idProducto,cantidad) VALUES (?, ?, ?)";
    PreparedStatement ps = null;

    try {
      conn.setAutoCommit(false); // Desactivar auto-commit para manejar la transacción manualmente
      ps = conn.prepareStatement(query);

      for (FacturaProducto item : shopingCart) {
        ps.setInt(1, item.getIdFactura());
        ps.setInt(2, item.getIdProducto());
        ps.setInt(3, item.getCantidad());

        ps.addBatch(); // Agregar la instrucción al batch
      }

      ps.executeBatch(); // Ejecutar todas las instrucciones en el batch
      conn.commit(); // Confirmar la transacción
      logger.info("Lista de items insertada al carrito exitosamente.");
    } catch (SQLException e) {
      logger.error("Error al insertar items del carrito: ", e); // Registrar el error
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
        logger.error("Error al cerrar PreparedStatement: ", e); // Registrar el error
      }
    }
  }
  // Métodos adicionales (crear, actualizar, eliminar, listar, etc.) se pueden agregar según las necesidades.
}
