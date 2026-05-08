package com.concesionario.concesionario.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
// import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;


@Entity
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long id;

    @NotBlank(message = "El campo marca no puede estar vacío")
    private String marca;

    @NotBlank(message = "El campo modelo no puede estar vacío")
    private String modelo;

    @NotBlank(message = "El campo año no puede estar vacío")
    @Size(min = 4, max = 4, message = "El campo año debe tener exactamente 4 caracteres")
    private String año;
    @NotBlank(message = "El campo precio no puede estar vacío")
    private int precio;
    private boolean vendido=false;
    @Column(name = "vendido")
    private boolean disponible;

    public Auto() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getAnio() {
        return anio;
    }

<<<<<<< HEAD
    public void setAño(String año) {
        this.año = año;
    }
        public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }   

    public boolean isVendido() {
        return vendido;
=======
    public void setAnio(String anio) {
        this.anio = anio;
>>>>>>> 1ec7fc3d79b2f1a66be49e8b7fca037d39b12f6c
    }

    public double getPrecio() {
        return precio;
    }

    public void setPrecio(double precio) {
        this.precio = precio;
    }

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

}
