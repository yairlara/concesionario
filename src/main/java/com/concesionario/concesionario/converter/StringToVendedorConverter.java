package com.concesionario.concesionario.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.concesionario.concesionario.model.Vendedor;
import com.concesionario.concesionario.repository.VendedorRepository;

@Component
public class StringToVendedorConverter implements Converter<String, Vendedor> {

    private final VendedorRepository vendedorRepository;

    public StringToVendedorConverter(VendedorRepository vendedorRepository) {
        this.vendedorRepository = vendedorRepository;
    }

    @Override
    public Vendedor convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return vendedorRepository.findById(Long.parseLong(source)).orElse(null);
    }
}
