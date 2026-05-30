package com.concesionario.concesionario.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.concesionario.concesionario.model.Sucursal;
import com.concesionario.concesionario.repository.SucursalRepository;

@Service
public class SucursalService {

    @Autowired
    private SucursalRepository repository;

    public List<Sucursal> listarSucursales() {
        return repository.findAll();
    }

    public Sucursal guardarSucursal(Sucursal sucursal) {
        return repository.save(sucursal);
    }

    public Sucursal obtenerSucursalPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
