package com.app.dto;

public class ClienteDTO {

  private final String nombre;
  private final Double totalFacturado;

  public ClienteDTO(String nombre, Double totalFacturado) {
    this.nombre = nombre;
    this.totalFacturado = totalFacturado;
  }

  public String getNombre() {
    return this.nombre;
  }

  public Double getTotalFacturado() {
    return this.totalFacturado;
  }

  @Override
  public String toString() {
    return (
      "{" +
      " nombre='" +
      getNombre() +
      "'" +
      ", totalFacturado='" +
      getTotalFacturado() +
      "'" +
      "}"
    );
  }
}
