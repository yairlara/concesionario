package com.concesionario.concesionario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concesionario.concesionario.model.Venta;

public interface VentaRepository extends JpaRepository<Venta, Long> {
}
