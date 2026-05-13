package com.concesionario.concesionario.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concesionario.concesionario.model.Categoria;
import com.concesionario.concesionario.repository.CategoriaRepository;

@Service
public class CategoriaService {
    @Autowired
    private CategoriaRepository CategoriaRepository;

    public List<Categoria> listaCategorias(){
        return CategoriaRepository.findAll();
    }
}
