package com.concesionario.concesionario.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import com.concesionario.concesionario.model.Sucursal;
import com.concesionario.concesionario.repository.SucursalRepository;

@Component
public class StringToSucursalConverter implements Converter<String, Sucursal> {

    private final SucursalRepository sucursalRepository;

    public StringToSucursalConverter(SucursalRepository sucursalRepository) {
        this.sucursalRepository = sucursalRepository;
    }

    @Override
    public Sucursal convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return sucursalRepository.findById(Long.parseLong(source)).orElse(null);
    }
}
