package com.app.entities;

/**
 * @brief Clase que representa un cliente
 * @details Esta clase contiene la información de un cliente, incluyendo su id, nombre, email y total facturado.
 * @author Grupo 1
 * @date 2023-10-01
 * @version 1.0
 */

public class Cliente {

  /**
   * @brief idCliente
   * @details Identificador único del cliente
   */
  private int idCliente;

  /**
   * @brief nombre
   * @details Nombre del cliente
   */
  private String nombre;
  /**
   * @brief email
   * @details Email del cliente
   */
  private String email;
  private double totalFacturado;

  /**
   *
   * @param idCliente
   * @param nombre
   * @param email
   */
  public Cliente(int idCliente, String nombre, String email) {
    this.idCliente = idCliente;
    this.nombre = nombre;
    this.email = email;
  }

  public Cliente(
    int idCliente,
    String nombre,
    String email,
    double totalFacturado
  ) {
    this.idCliente = idCliente;
    this.nombre = nombre;
    this.email = email;
    this.totalFacturado = totalFacturado;
  }

  public int getIdCliente() {
    return this.idCliente;
  }

  public void setIdCliente(int idCliente) {
    this.idCliente = idCliente;
  }

  public String getNombre() {
    return this.nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public String getEmail() {
    return this.email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  @Override
  public String toString() {
    return (
      "Cliente{" +
      "idCliente=" +
      idCliente +
      ", nombre='" +
      nombre +
      '\'' +
      ", email='" +
      email +
      '\'' +
      ", totalFacturado=" +
      totalFacturado +
      '}'
    );
  }
}
