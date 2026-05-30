package com.concesionario.concesionario.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.concesionario.concesionario.model.Cliente;
import com.concesionario.concesionario.repository.ClienteRepository;

@Component
public class StringToClienteConverter implements Converter<String, Cliente> {

    private final ClienteRepository clienteRepository;

    public StringToClienteConverter(ClienteRepository clienteRepository) {
        this.clienteRepository = clienteRepository;
    }

    @Override
    public Cliente convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return clienteRepository.findById(Long.parseLong(source)).orElse(null);
    }
}
