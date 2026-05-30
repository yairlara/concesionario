package com.concesionario.concesionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.concesionario.concesionario.model.Venta;
import com.concesionario.concesionario.service.AutoService;
import com.concesionario.concesionario.service.ClienteService;
import com.concesionario.concesionario.service.VendedorService;
import com.concesionario.concesionario.service.VentaService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class VentaController {

    @Autowired
    private VentaService ventaService;

    @Autowired
    private AutoService autoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private VendedorService vendedorService;

    @GetMapping("/ventas")
    public String listar(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        model.addAttribute("ventas", ventaService.listar());
        return "ventas";
    }

    @GetMapping("/ventas/nueva")
    public String nueva(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        model.addAttribute("venta", new Venta());
        cargarListasFormulario(model);
        return "formulario-venta";
    }

    @PostMapping("/ventas/guardar")
    public String guardar(@Valid Venta venta, BindingResult result, Model model,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            cargarListasFormulario(model);
            return "formulario-venta";
        }
        ventaService.guardar(venta);
        redirectAttributes.addFlashAttribute("mensaje", "Venta guardada exitosamente");
        return "redirect:/ventas";
    }

    @GetMapping("/ventas/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        Venta venta = ventaService.obtenerPorId(id);
        if (venta == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Venta no encontrada");
            return "redirect:/ventas";
        }
        model.addAttribute("venta", venta);
        cargarListasFormulario(model);
        return "formulario-venta";
    }

    @GetMapping("/ventas/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        ventaService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Venta eliminada exitosamente");
        return "redirect:/ventas";
    }

    private void cargarListasFormulario(Model model) {
        model.addAttribute("autos", autoService.listarAutos());
        model.addAttribute("clientes", clienteService.listarClientes());
        model.addAttribute("vendedores", vendedorService.listarVendedores());
    }
}
