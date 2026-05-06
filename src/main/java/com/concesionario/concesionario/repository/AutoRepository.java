package com.concesionario.concesionario.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import com.concesionario.concesionario.model.Auto;


public interface AutoRepository extends JpaRepository<Auto, Long> {
    
}
