package com.concesionario.concesionario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concesionario.concesionario.model.Auto;
import com.concesionario.concesionario.repository.AutoRepository;

@Service
public class AutoService {

    @Autowired
    private AutoRepository Repository;

    public List<Auto> listarAutos() {
        return Repository.findAll();
    }

    public Auto guardarAuto(Auto auto) {
        return Repository.save(auto);

    }

    public Auto obtenerAutoPorId(Long id) {
        return Repository.findById(id).orElse(null);
    }

    public Auto eliminar(Long id) {
        repository.deleteById(id);

    }

}

