package com.concesionario.concesionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.concesionario.concesionario.model.Vendedor;
import com.concesionario.concesionario.service.SucursalService;
import com.concesionario.concesionario.service.VendedorService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class VendedorController {

    @Autowired
    private VendedorService vendedorService;

    @Autowired
    private SucursalService sucursalService;

    @GetMapping("/vendedores")
    public String listar(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        model.addAttribute("vendedores", vendedorService.listarVendedores());
        return "vendedores";
    }

    @GetMapping("/vendedores/nuevo")
    public String nuevo(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        model.addAttribute("vendedor", new Vendedor());
        model.addAttribute("sucursales", sucursalService.listarSucursales());
        return "formulario-vendedor";
    }

    @PostMapping("/vendedores/guardar")
    public String guardar(@Valid Vendedor vendedor, BindingResult result, Model model,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            model.addAttribute("sucursales", sucursalService.listarSucursales());
            return "formulario-vendedor";
        }
        vendedorService.guardarVendedor(vendedor);
        redirectAttributes.addFlashAttribute("mensaje", "Vendedor guardado exitosamente");
        return "redirect:/vendedores";
    }

    @GetMapping("/vendedores/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        Vendedor vendedor = vendedorService.obtenerVendedorPorId(id);
        if (vendedor == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Vendedor no encontrado");
            return "redirect:/vendedores";
        }
        model.addAttribute("vendedor", vendedor);
        model.addAttribute("sucursales", sucursalService.listarSucursales());
        return "formulario-vendedor";
    }

    @GetMapping("/vendedores/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        vendedorService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Vendedor eliminado exitosamente");
        return "redirect:/vendedores";
    }
}
