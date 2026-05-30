package com.concesionario.concesionario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.concesionario.concesionario.model.Vendedor;

public interface VendedorRepository extends JpaRepository<Vendedor, Long> {

}
