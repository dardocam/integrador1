package com.app.entities;

public class Producto {

  private int idProducto;
  private String nombre;
  private double valor;
  private double recaudacion; // Campo calculado

  public Producto(int idProducto, String nombre, double valor) {
    this.idProducto = idProducto;
    this.nombre = nombre;
    this.valor = valor;
  }

  public Producto(
    int idProducto,
    String nombre,
    double valor,
    double recaudacion
  ) {
    // Constructor para el producto más recaudador
    this.idProducto = idProducto;
    this.nombre = nombre;
    this.valor = valor;
    this.recaudacion = recaudacion;
  }

  // Getters y Setters
  public int getIdProducto() {
    return idProducto;
  }

  public void setIdProducto(int idProducto) {
    this.idProducto = idProducto;
  }

  public String getNombre() {
    return nombre;
  }

  public void setNombre(String nombre) {
    this.nombre = nombre;
  }

  public double getValor() {
    return valor;
  }

  public void setValor(double valor) {
    this.valor = valor;
  }

  public double getRecaudacion() {
    return recaudacion;
  }

  public void setRecaudacion(double recaudacion) {
    this.recaudacion = recaudacion;
  }

  @Override
  public String toString() {
    return (
      "Producto{" +
      "idProducto=" +
      idProducto +
      ", nombre='" +
      nombre +
      '\'' +
      ", valor=" +
      valor +
      ", recaudacion=" +
      recaudacion +
      '}'
    );
  }
}
