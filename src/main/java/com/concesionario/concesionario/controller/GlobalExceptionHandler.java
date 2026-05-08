package com.concesionario.concesionario.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.multipart.MaxUploadSizeExceededException;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String manejarArchivoMuyGrande(MaxUploadSizeExceededException ex,
                                          RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute(
                "mensajeError",
                "La imagen es demasiado grande. El tamaño máximo permitido es 10 MB.");
        return "redirect:/libros/nuevo";
    }
}