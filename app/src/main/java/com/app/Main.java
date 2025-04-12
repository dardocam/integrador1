package com.app;

import com.app.utils.HelperMySQL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Main {

  private static final Logger logger = LoggerFactory.getLogger(Main.class);

  public static void main(String[] args) throws Exception {
    HelperMySQL dbMySQL = new HelperMySQL();
    try {
      logger.info("***********************************************");
      logger.info("Iniciando la aplicación...");
      logger.info("***********************************************");
      dbMySQL.dropTables();
      logger.info("***********************************************");
      dbMySQL.createTables();
      logger.info("***********************************************");
      dbMySQL.populateDBwithProductsCSV();
      logger.info("***********************************************");
      dbMySQL.populateDBwithClientsCSV();
      logger.info("***********************************************");
      dbMySQL.populateDBwithInvoicesCSV();
      logger.info("***********************************************");
      dbMySQL.populateDBwithFacturaProductoCSV();
      logger.info("***********************************************");
      logger.info("PRODUCTO MAS RECAUDADOR");
      logger.info("***********************************************");
      dbMySQL.getProductoMasRecaudador();
      logger.info("***********************************************");
      logger.info("CLIENTE MAS FACTURADO");
      logger.info("***********************************************");
      dbMySQL.getClientesMasFacturados();
      logger.info("***********************************************");
    } finally {
      // Asegúrate de cerrar la conexión al final
      dbMySQL.getFactory().closeConnection();
      logger.info("Conexión cerrada.");
    }
    logger.info("***********************************************");
    logger.info("Aplicación finalizada.");
    logger.info("***********************************************");
    System.exit(0);
  }
}
