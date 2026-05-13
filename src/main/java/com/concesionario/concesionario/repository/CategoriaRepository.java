package com.concesionario.concesionario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concesionario.concesionario.model.Categoria;

public interface CategoriaRepository extends JpaRepository<Categoria, Long> {
    
}
