package com.concesionario.concesionario.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.concesionario.concesionario.model.Cliente;
import com.concesionario.concesionario.repository.ClienteRepository;

@Service
public class ClienteService {

    @Autowired
    private ClienteRepository repository;

    public List<Cliente> listarClientes() {
        return repository.findAll();
    }

    public Cliente guardarCliente(Cliente cliente) {
        return repository.save(cliente);
    }

    public Cliente obtenerClientePorId(Long id) {
        return repository.findById(id).orElse(null);
    }

    public void eliminar(Long id) {
        repository.deleteById(id);
    }
}
