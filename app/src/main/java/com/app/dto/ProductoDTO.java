package com.app.dto;

public class ProductoDTO {

  private final String nombre;
  private final Double recaudacion;

  // Instancia estática para representar un ProductoDTO vacío
  public static final ProductoDTO EMPTY = new ProductoDTO("N/A", 0.0);

  public ProductoDTO(String nombre, Double totalFacturado) {
    this.nombre = nombre;
    this.recaudacion = totalFacturado;
  }

  public String getNombre() {
    return this.nombre;
  }

  public Double getRecaudacion() {
    return this.recaudacion;
  }

  @Override
  public String toString() {
    return (
      "{" +
      " nombre='" +
      getNombre() +
      "'" +
      ", totalFacturado='" +
      getRecaudacion() +
      "'" +
      "}"
    );
  }
}
