package com.concesionario.concesionario.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.concesionario.concesionario.model.Vendedor;
import com.concesionario.concesionario.repository.VendedorRepository;

@Service
public class VendedorService {

    @Autowired
    private VendedorRepository repository;

    public List<Vendedor> listarVendedores() {
        return repository.findAll();
    }

    public Vendedor guardarVendedor(Vendedor vendedor) {
        return repository.save(vendedor);
    }

    public Vendedor obtenerVendedorPorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
