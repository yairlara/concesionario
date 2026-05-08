package com.concesionario.concesionario.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileStorageService {

    

    private static final List<String> EXTENSIONES_PERMITIDAS = Arrays.asList(
        "jpg", "jpeg", "png", "gif");

    private static final Path DIRECTORIO_UPLOADS  = Paths.get(
        System.getProperty("user.dir"),
        "src", "main", "resources", "static", "uploads");

    
    
    public String guardar(MultipartFile archivo){
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El archivo no puede estar vacío");
        }

        String extension = obtenerExtension(archivo.getOriginalFilename());
        if(!EXTENSIONES_PERMITIDAS.contains(extension)){
            throw new IllegalArgumentException(
                "Archivo no permitido. Solo se permiten: " + EXTENSIONES_PERMITIDAS);
        }

        String nombreFinal = UUID.randomUUID() + "." + extension;
        try {
            Files.createDirectories(DIRECTORIO_UPLOADS);
            Path destino = DIRECTORIO_UPLOADS.resolve(nombreFinal);
            Files.copy(archivo.getInputStream(), destino, StandardCopyOption.REPLACE_EXISTING);
        }catch (IOException e) {
            throw new RuntimeException("Error al guardar el archivo", e);
        }
        return nombreFinal;
    }  
    
    public void eliminar(String nombre) {
        if (nombre == null || nombre.isBlank()) return;
        try {
            Files.delete(DIRECTORIO_UPLOADS.resolve(nombre));
        } catch (IOException e) {
            // No queremos romper la operacion si el archivo no se puede borrar
        }
    }
    
    private String obtenerExtension(String nombreOriginal) {
    if(nombreOriginal == null) return "";
    int idx = nombreOriginal.lastIndexOf(".");
    if (idx < 0 || idx == nombreOriginal.length() -1 ) return "";
    return nombreOriginal.substring(idx + 1).toLowerCase(Locale.ROOT);
    }
    
}


