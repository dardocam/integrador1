package com.app.factory;

import com.app.dao.ClienteDAO;
import com.app.dao.ProductoDAO;

/**
 * @brief Clase abstracta para la creación de fábricas de DAOs.
 *
 * Esta clase define una interfaz para obtener DAOs específicos, como {@link ProductoDAO}
 * y {@link ClienteDAO}, y proporciona un método estático para obtener una instancia
 * de una fábrica concreta basada en el tipo de base de datos.
 *
 * @details
 * Las subclases de esta clase deben implementar los métodos para proporcionar
 * instancias de DAOs específicos. Actualmente, soporta las siguientes fábricas:
 * - MySQL JDBC
 * - Derby JDBC (no implementado)
 *
 * @author Dardo
 * @version 1.0
 * @since 2023-10-01
 */
public abstract class AbstractFactoryDAO {

  /**
   * @brief Identificador para la fábrica de MySQL JDBC.
   */
  public static final int MYSQL_JDBC = 1;

  /**
   * @brief Identificador para la fábrica de Derby JDBC.
   */
  public static final int DERBY_JDBC = 2;

  /**
   * @brief Obtiene una instancia de {@link ProductoDAO}.
   *
   * Este método debe ser implementado por las subclases para proporcionar
   * una instancia de DAO que permita interactuar con la tabla `Producto`.
   *
   * @return Una instancia de {@link ProductoDAO}.
   */
  public abstract ProductoDAO getProductoDAO();

  /**
   * @brief Obtiene una instancia de {@link ClienteDAO}.
   *
   * Este método debe ser implementado por las subclases para proporcionar
   * una instancia de DAO que permita interactuar con la tabla `Cliente`.
   *
   * @return Una instancia de {@link ClienteDAO}.
   */
  public abstract ClienteDAO getClienteDAO();

  /**
   * @brief Obtiene una fábrica concreta basada en el tipo de base de datos.
   *
   * Este método devuelve una instancia de una fábrica concreta dependiendo
   * del identificador proporcionado. Actualmente, solo se encuentra implementada
   * la fábrica para MySQL JDBC.
   *
   * @param whichFactory Identificador del tipo de fábrica. Puede ser:
   * - {@link #MYSQL_JDBC} para MySQL JDBC.
   * - {@link #DERBY_JDBC} para Derby JDBC (no implementado).
   *
   * @return Una instancia de {@link AbstractFactoryDAO} correspondiente al tipo
   * de fábrica solicitado, o `null` si el tipo no está soportado.
   */
  public static AbstractFactoryDAO getDAOFactory(int whichFactory) {
    switch (whichFactory) {
      case MYSQL_JDBC:
        {
          return MySQLDAOFactory.getInstance();
        }
      case DERBY_JDBC:
        return null;
      default:
        return null;
    }
  }
}
