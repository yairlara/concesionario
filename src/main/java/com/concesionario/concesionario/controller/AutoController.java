package com.concesionario.concesionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.concesionario.concesionario.model.Auto;
import com.concesionario.concesionario.repository.AutoRepository;
import com.concesionario.concesionario.service.AutoService;
import com.concesionario.concesionario.service.CategoriaService;
import com.concesionario.concesionario.service.FileStorageService;

import jakarta.validation.Valid;

@Controller
public class AutoController {
    
    @Autowired
    private AutoService autoService;

    @Autowired
    private FileStorageService fileStorageService; 

    @Autowired
    private AutoRepository autoRepository;

    @Autowired
    private CategoriaService categoriaService;

    @GetMapping("/autos")
    public String Autos(Model model) {
        model.addAttribute("autos", autoRepository.findAll());
        return "index";
    }

    @GetMapping("/autos/nuevo")
    public String nuevo (Model model) {
        model.addAttribute("auto", new Auto());
        model.addAttribute("categorias", categoriaService.listaCategorias());
        return "formulario";
    }

    @PostMapping("/autos/guardar")
    public String guardar(@Valid @ModelAttribute Auto auto, 
        BindingResult result,
        @RequestParam(value = "archivoImagen", required = false) MultipartFile archivoImagen,
        Model model,
        RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            return "formulario";
        }
        if (archivoImagen != null && !archivoImagen.isEmpty()) {
            String imagenPrevia = null;
            if (auto.getId() != null) {
                Auto existente = autoService.obtenerAutoPorId(auto.getId());
                if (existente != null) {
                    imagenPrevia = existente.getImagen();
                }
            }
            String nuevoNombre = fileStorageService.guardar(archivoImagen);
            auto.setImagen(nuevoNombre);
            if (imagenPrevia != null && !imagenPrevia.isBlank()) {
                fileStorageService.eliminar(imagenPrevia);
            }   
        }
        autoService.guardarAuto(auto);

        redirectAttributes.addFlashAttribute("mensaje", "Auto guardado exitosamente");

        return "redirect:/autos";
    }

    @GetMapping("/autos/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("auto", autoService.obtenerAutoPorId(id));
        model.addAttribute("categorias", categoriaService.listaCategorias());
        return "formulario";
    }

    @GetMapping("/autos/eliminar/{id}")
    public String eliminar(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        Auto auto = autoService.obtenerAutoPorId(id);
        if (auto != null && auto.getImagen() != null && !auto.getImagen().isBlank()) {
            fileStorageService.eliminar(auto.getImagen());
        }
        
        autoService.eliminar(id);
        
        redirectAttributes.addFlashAttribute("mensaje", "Auto eliminado exitosamente");
        
        return "redirect:/autos";
    }

    
}
