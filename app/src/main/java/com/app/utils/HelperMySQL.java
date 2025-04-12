package com.app.utils;

import com.app.dao.ClienteDAO;
import com.app.dao.FacturaDAO;
import com.app.dao.FacturaProductoDAO;
import com.app.dao.ProductoDAO;
import com.app.entities.Cliente;
import com.app.entities.Factura;
import com.app.entities.FacturaProducto;
import com.app.entities.Producto;
import com.app.factory.MySQLDAOFactory;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HelperMySQL {

  private Connection conn = null;
  private MySQLDAOFactory factory = null;

  private static final Logger logger = LoggerFactory.getLogger(
    HelperMySQL.class
  );

  public HelperMySQL() {
    this.factory = MySQLDAOFactory.getInstance();
    this.conn = this.factory.createConnection();
    logger.info("Conexión a la base de datos establecida correctamente.");
  }

  public Connection getConnection() {
    return conn;
  }

  public MySQLDAOFactory getFactory() {
    return factory;
  }

  public void close() {
    if (conn != null) {
      try {
        conn.close();
        logger.info("Conexión a la base de datos cerrada correctamente.");
      } catch (SQLException e) {
        logger.error(
          "Error al cerrar la conexión a la base de datos: " + e.getMessage(),
          e
        );
      }
    }
  }

  // public void createDatabase() throws SQLException {
  //   // Sentencia SQL para crear la base de datos "integrador1" solo si no existe
  //   String createTable = "CREATE DATABASE IF NOT EXISTS integrador1";
  //   // Usando try-with-resources para asegurar el cierre automático de recursos
  //   try {
  //     this.conn.setAutoCommit(false); // Iniciar transacción
  //     this.conn.prepareStatement(createTable).execute();
  //     this.conn.commit(); // Confirmar transacción
  //     System.out.println("Base de datos 'integrador1' creada correctamente.");
  //   } catch (SQLException e) {
  //     this.conn.rollback(); // Revertir cambios en caso de error
  //     throw new RuntimeException(
  //       "Error al crear la base de datos integrador1",
  //       e
  //     );
  //   } finally {
  //     this.conn.setAutoCommit(true); // Restaurar el modo de auto-commit
  //   }
  // }

  public void dropTables() throws SQLException {
    List<String> tables = new ArrayList<>();
    tables.add("Factura_Producto");
    tables.add("Factura");
    tables.add("Cliente");
    tables.add("Producto");

    try {
      this.conn.setAutoCommit(false); // Iniciar transacción
      for (String table : tables) {
        String dropTable = "DROP TABLE IF EXISTS " + table;
        this.conn.prepareStatement(dropTable).execute();
      }
      this.conn.commit(); // Confirmar transacción
      System.out.println("¡Esquema eliminado exitosamente!");
    } catch (SQLException e) {
      this.conn.rollback(); // Revertir cambios en caso de error
      throw new RuntimeException("Error al eliminar las tablas.", e);
    } finally {
      this.conn.setAutoCommit(true); // Restaurar el modo de auto-commit
    }
    System.out.println("¡Esquema eliminado exitosamente!");
  }

  public void createTables() throws SQLException {
    try {
      this.conn.setAutoCommit(false); // Iniciar transacción

      // Crear la tabla Cliente
      String tableCliente =
        "CREATE TABLE IF NOT EXISTS Cliente (" +
        "idCliente INT NOT NULL AUTO_INCREMENT, " +
        "nombre VARCHAR(500) NOT NULL, " +
        "email VARCHAR(153) NOT NULL, " +
        "PRIMARY KEY (idCliente)" +
        ") ENGINE=InnoDB";
      this.conn.prepareStatement(tableCliente).execute();
      logger.info("Tabla 'Cliente' creada.");

      // Crear la tabla Factura
      String tableFactura =
        "CREATE TABLE IF NOT EXISTS Factura (" +
        "idFactura INT NOT NULL AUTO_INCREMENT, " +
        "idCliente INT NOT NULL, " +
        "PRIMARY KEY (idFactura), " +
        "FOREIGN KEY (idCliente) REFERENCES Cliente(idCliente)" +
        ") ENGINE=InnoDB";
      this.conn.prepareStatement(tableFactura).execute();
      logger.info("Tabla 'Factura' creada.");

      // Crear la tabla Producto
      String tableProducto =
        "CREATE TABLE IF NOT EXISTS Producto (" +
        "idProducto INT NOT NULL AUTO_INCREMENT, " +
        "nombre VARCHAR(45) NOT NULL, " +
        "valor FLOAT NOT NULL, " +
        "PRIMARY KEY (idProducto)" +
        ") ENGINE=InnoDB";
      this.conn.prepareStatement(tableProducto).execute();
      logger.info("Tabla 'Producto' creada.");

      // Crear la tabla Factura_Producto
      String tableFacturaProducto =
        "CREATE TABLE IF NOT EXISTS Factura_Producto (" +
        "idFactura INT NOT NULL, " +
        "idProducto INT NOT NULL, " +
        "cantidad INT NOT NULL, " +
        "PRIMARY KEY (idFactura, idProducto), " +
        "FOREIGN KEY (idFactura) REFERENCES Factura(idFactura), " +
        "FOREIGN KEY (idProducto) REFERENCES Producto(idProducto)" +
        ") ENGINE=InnoDB";
      this.conn.prepareStatement(tableFacturaProducto).execute();
      logger.info("Tabla 'Factura_Producto' creada.");

      this.conn.commit(); // Confirmar transacción
      System.out.println("¡Esquema creado exitosamente!");
    } catch (SQLException e) {
      this.conn.rollback(); // Revertir cambios en caso de error
      throw new RuntimeException(
        "No se pudo crear el esquema de base de datos.",
        e
      );
    } finally {
      this.conn.setAutoCommit(true); // Restaurar el modo de auto-commit
    }
  }

  private Iterable<CSVRecord> getData(String archivo) throws IOException {
    String path = "src/main/resources/" + archivo; // Cambiar \\ por /
    File file = new File(path);
    if (!file.exists()) {
      throw new IOException("El archivo no existe: " + path);
    }
    Reader in = new FileReader(file);
    String[] header = {}; // Puedes configurar tu encabezado personalizado aquí si es necesario
    @SuppressWarnings("deprecation")
    CSVParser csvParser = CSVFormat.EXCEL.withHeader(header).parse(in);

    return csvParser.getRecords();
  }

  public void populateDBwithClientsCSV() throws Exception {
    logger.info("Populating DB...");
    logger.info("Leyendo archivo clientes.csv");
    List<Cliente> clientes = new ArrayList<>();
    for (CSVRecord row : getData("clientes.csv")) {
      if (row.size() >= 1) { // Verificar que hay al menos 1 campo en el CSVRecord
        String idString = row.get(0);
        String nombreString = row.get(1);
        String emailString = row.get(2);
        if (
          !idString.isEmpty() &&
          !nombreString.isEmpty() &&
          !emailString.isEmpty()
        ) {
          try {
            int id = Integer.parseInt(idString);
            String nombre = nombreString;
            String email = emailString;
            Cliente cliente = new Cliente(id, nombre, email);
            clientes.add(cliente);
          } catch (NumberFormatException e) {
            logger.error(
              "Error de formato en datos de producto: " + e.getMessage()
            );
          }
        }
      }
    }
    if (!clientes.isEmpty()) {
      // Realizar operaciones con la base de datos
      ClienteDAO clienteDAO = new ClienteDAO(conn);
      clienteDAO.insertClientes(clientes);
    }
  }

  public void populateDBwithProductsCSV() throws Exception {
    logger.info("Populating DB...");
    logger.info("Leyendo archivo productos.csv");
    List<Producto> productos = new ArrayList<>();
    for (CSVRecord row : getData("productos.csv")) {
      if (row.size() >= 1) { // Verificar que hay al menos 4 campos en el CSVRecord
        String idString = row.get(0);
        String nombreString = row.get(1);
        String valorString = row.get(2);
        if (
          !idString.isEmpty() &&
          !nombreString.isEmpty() &&
          !valorString.isEmpty()
        ) {
          try {
            int id = Integer.parseInt(idString);
            float valor = Float.parseFloat(valorString);
            String nombre = nombreString;
            Producto producto = new Producto(id, nombre, valor);
            productos.add(producto);
          } catch (NumberFormatException e) {
            logger.error(
              "Error de formato en datos de producto: " + e.getMessage()
            );
          }
        }
      }
    }
    if (!productos.isEmpty()) {
      // Realizar operaciones con la base de datos
      // el factory crea el DAO
      ProductoDAO productoDAO = factory.getProductoDAO();

      productoDAO.insertProductos(productos);
    }
  }

  public void populateDBwithInvoicesCSV() throws Exception {
    logger.info("Populating DB...");
    logger.info("Leyendo archivo facturas.csv");

    List<Factura> facturas = new ArrayList<>();
    for (CSVRecord row : getData("facturas.csv")) {
      if (row.size() >= 1) { // Verificar que hay al menos 1 campo en el CSVRecord
        String idFacturaString = row.get(0);
        String idClienteString = row.get(1);
        if (!idFacturaString.isEmpty() && !idFacturaString.isEmpty()) {
          try {
            int idFactura = Integer.parseInt(idFacturaString);
            int idCliente = Integer.parseInt(idClienteString);

            Factura factura = new Factura(idFactura, idCliente);
            facturas.add(factura);
          } catch (NumberFormatException e) {
            logger.error(
              "Error de formato en datos de producto: " + e.getMessage()
            );
          }
        }
      }
    }
    if (!facturas.isEmpty()) {
      // Realizar operaciones con la base de datos
      FacturaDAO facturaDAO = new FacturaDAO(conn);
      facturaDAO.insertFacturas(facturas);
    }
  }

  public void populateDBwithFacturaProductoCSV() throws Exception {
    logger.info("Populating DB...");
    logger.info("Leyendo archivo factura_producto.csv");
    List<FacturaProducto> items = new ArrayList<>();
    for (CSVRecord row : getData("factura_producto.csv")) {
      if (row.size() >= 1) { // Verificar que hay al menos 1 campo en el CSVRecord
        String idFacturaString = row.get(0);
        String idProductoString = row.get(1);
        String cantidadString = row.get(2);
        if (
          !idFacturaString.isEmpty() &&
          !idProductoString.isEmpty() &&
          !cantidadString.isEmpty()
        ) {
          try {
            int idFactura = Integer.parseInt(idFacturaString);
            int idProducto = Integer.parseInt(idProductoString);
            int cantidad = Integer.parseInt(cantidadString);

            FacturaProducto item = new FacturaProducto(
              idFactura,
              idProducto,
              cantidad
            );
            items.add(item);
          } catch (NumberFormatException e) {
            logger.error(
              "Error de formato en datos de producto: " + e.getMessage()
            );
          }
        }
      }
    }
    if (!items.isEmpty()) {
      // Realizar operaciones con la base de datos
      FacturaProductoDAO facturaProductoDAO = new FacturaProductoDAO(conn);
      facturaProductoDAO.insertFacturaProducto(items);
    }
  }

  public String getProductoMasRecaudador() {
    ProductoDAO productoDAO = new ProductoDAO(conn);
    Producto productoMasRecaudador = productoDAO.getProductoMasRecaudador();
    return productoMasRecaudador.toString();
  }

  public void getClientesMasFacturados() {
    ClienteDAO clienteDAO = new ClienteDAO(conn);
    List<Cliente> clientesMasFacturados = clienteDAO.getClientesMasFacturados();
    for (Cliente cliente : clientesMasFacturados) {
      logger.info(cliente.toString());
    }
  }
}
