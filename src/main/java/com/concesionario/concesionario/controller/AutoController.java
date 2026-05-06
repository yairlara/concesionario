package com.concesionario.concesionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import com.concesionario.concesionario.model.Auto;
import com.concesionario.concesionario.repository.AutoRepository;
import com.concesionario.concesionario.service.AutoService;

import jakarta.validation.Valid;

@Controller
public class AutoController {

    private final AutoService autoService;

    @Autowired
    private AutoRepository autoRepository;

    public AutoController(AutoService autoService) {
        this.autoService = autoService;
    }

    @GetMapping("/autos")
    public String Autos(Model model) {
        model.addAttribute("autos", autoRepository.findAll());
        return "index";
    }

    @GetMapping("/autos/nuevo")
    public String nuevo (Model model) {
        model.addAttribute("auto", new Auto());
        return "formulario";
    }

    @PostMapping("/autos/guardar")
    public String guardar(@Valid @ModelAttribute Auto auto, BindingResult result) {
        if (result.hasErrors()) {
            return "formulario";
        }
        autoService.guardarAuto(auto);
        return "redirect:/autos";
    }

    @GetMapping("/autos/editar/{id}")
    public String editar(@PathVariable Long id, Model model) {
        model.addAttribute("auto", autoService.obtenerAutoPorId(id));
        return "formulario";
    }

    @GetMapping("/autos/eliminar/{id}")
    public String eliminar(@PathVariable Long id) {
        autoService.eliminar(id);
        return "redirect:/autos";
    }

    
}
