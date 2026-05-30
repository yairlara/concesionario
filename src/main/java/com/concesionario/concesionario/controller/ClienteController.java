package com.concesionario.concesionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import com.concesionario.concesionario.model.Cliente;
import com.concesionario.concesionario.service.ClienteService;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

@Controller
public class ClienteController {

    @Autowired
    private ClienteService clienteService;

    @GetMapping("/clientes")
    public String listar(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        model.addAttribute("clientes", clienteService.listarClientes());
        return "clientes";
    }

    @GetMapping("/clientes/nuevo")
    public String nuevo(Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        model.addAttribute("cliente", new Cliente());
        return "formulario-cliente";
    }

    @PostMapping("/clientes/guardar")
    public String guardar(@Valid Cliente cliente, BindingResult result,
            HttpSession session, RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        if (result.hasErrors()) {
            return "formulario-cliente";
        }
        clienteService.guardarCliente(cliente);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente guardado exitosamente");
        return "redirect:/clientes";
    }

    @GetMapping("/clientes/editar/{id}")
    public String editar(@PathVariable Long id, Model model, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        Cliente cliente = clienteService.obtenerClientePorId(id);
        if (cliente == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Cliente no encontrado");
            return "redirect:/clientes";
        }
        model.addAttribute("cliente", cliente);
        return "formulario-cliente";
    }

    @GetMapping("/clientes/eliminar/{id}")
    public String eliminar(@PathVariable Long id, HttpSession session,
            RedirectAttributes redirectAttributes) {
        if (session.getAttribute("usuarioLogueado") == null) {
            redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
            return "redirect:/login";
        }
        clienteService.eliminar(id);
        redirectAttributes.addFlashAttribute("mensaje", "Cliente eliminado exitosamente");
        return "redirect:/clientes";
    }
}
