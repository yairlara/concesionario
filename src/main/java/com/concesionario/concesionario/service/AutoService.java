package com.concesionario.concesionario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concesionario.concesionario.model.Auto;
import com.concesionario.concesionario.repository.AutoRepository;

@Service
public class AutoService {

    @Autowired
    private AutoRepository repository;

    public List<Auto> listarAutos() {
        return repository.findAll();
    }

    public Auto guardarAuto(Auto auto) {
        return repository.save(auto);
    }

    public Auto obtenerAutoPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}

