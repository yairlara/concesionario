package com.concesionario.concesionario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.concesionario.concesionario.model.Cliente;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {

}
