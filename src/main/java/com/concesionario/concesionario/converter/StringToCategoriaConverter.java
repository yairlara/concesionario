package com.concesionario.concesionario.converter;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import com.concesionario.concesionario.model.Categoria;
import com.concesionario.concesionario.repository.CategoriaRepository;

@Component
public class StringToCategoriaConverter implements Converter<String, Categoria> {

    private final CategoriaRepository categoriaRepository;

    public StringToCategoriaConverter(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria convert(String source) {
        System.out.println("[StringToCategoriaConverter] recibido source='" + source + "'");
        if (source == null || source.isBlank()) {
            return null;
        }
        Categoria c = categoriaRepository.findById(Long.parseLong(source)).orElse(null);
        System.out.println("[StringToCategoriaConverter] resuelto -> " + (c == null ? "null" : "id=" + c.getId() + " nombre=" + c.getNombre()));
        return c;
    }
}
