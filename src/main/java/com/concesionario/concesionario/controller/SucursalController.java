package com.concesionario.concesionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.concesionario.concesionario.model.Sucursal;
import com.concesionario.concesionario.service.SucursalService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class SucursalController {

    @Autowired
    private SucursalService sucursalService;

    @GetMapping("/sucursales")
    public String listar(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        model.addAttribute("sucursales", sucursalService.listarSucursales());
        return "sucursales";
    }

    @GetMapping("/sucursales/nuevo")
    public String nuevo(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        model.addAttribute("sucursal", new Sucursal());
        return "formulario-sucursal";
    }

    @PostMapping("/sucursales/guardar")
    public String guardar(@Valid Sucursal sucursal, BindingResult result, Model model,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            return "formulario-sucursal";
        }
        sucursalService.guardarSucursal(sucursal);
        redirectAttributes.addFlashAttribute("mensaje", "Sucursal guardada exitosamente");
        return "redirect:/sucursales";
    }

    @GetMapping("/sucursales/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        Sucursal sucursal = sucursalService.obtenerSucursalPorId(id);
        if (sucursal == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Sucursal no encontrada");
            return "redirect:/sucursales";
        }
        model.addAttribute("sucursal", sucursal);
        return "formulario-sucursal";
    }

    @GetMapping("/sucursales/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        sucursalService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Sucursal eliminada exitosamente");
        return "redirect:/sucursales";
    }
}
