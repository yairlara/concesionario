package com.concesionario.concesionario.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.concesionario.concesionario.model.Auto;
import com.concesionario.concesionario.repository.AutoRepository;

@Component
public class StringToAutoConverter implements Converter<String, Auto> {

    private final AutoRepository autoRepository;

    public StringToAutoConverter(AutoRepository autoRepository) {
        this.autoRepository = autoRepository;
    }

    @Override
    public Auto convert(String source) {
        if (source == null || source.isBlank()) {
            return null;
        }
        return autoRepository.findById(Long.parseLong(source)).orElse(null);
    }
}
