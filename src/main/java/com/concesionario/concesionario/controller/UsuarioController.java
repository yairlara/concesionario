package com.concesionario.concesionario.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.concesionario.concesionario.model.Usuario;
import com.concesionario.concesionario.service.UsuarioService;

import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;

// ---------------------------------------------------
// @Controller: esta anotación le indica a Spring que
// esta clase maneja las rutas (URLs) de la aplicación.
//
// Cada método público con @GetMapping o @PostMapping
// se ejecuta cuando el usuario visita esa URL.
// ---------------------------------------------------
@Controller
public class UsuarioController {

    // ---------------------------------------------------
    // @Autowired: Spring inyecta automáticamente el
    // servicio para que el controlador pueda usarlo sin
    // tener que crear una instancia manualmente.
    // ---------------------------------------------------
    @Autowired
    private UsuarioService usuarioService;

    // ---------------------------------------------------
    // GET /registro
    //
    // Cuando el usuario visita /registro en su navegador,
    // este método se ejecuta.
    //
    // Crea un objeto Usuario vacío y lo agrega al modelo
    // para que el formulario de Thymeleaf pueda
    // enlazarlo con th:object.
    //
    // Devuelve "registro" que es el nombre de la plantilla
    // Thymeleaf (registro.html).
    // ---------------------------------------------------
    @GetMapping("/registro")
    public String mostrarFormularioRegistro(Model model) {

        // Agrega un Usuario vacío al modelo para que
        // el formulario pueda usar th:object y th:field
        model.addAttribute("usuario", new Usuario());

        // Retorna la vista registro.html
        return "registro";
    }

    // ---------------------------------------------------
    // POST /registro
    //
    // Cuando el usuario envía el formulario de registro,
    // este método procesa los datos.
    //
    // @Valid: activa las validaciones de Jakarta Validation
    // definidas en la entidad Usuario (@NotBlank, @Email,
    // @Size, etc.)
    //
    // BindingResult: contiene los resultados de las
    // validaciones. Si hay errores, la variable result
    // tendrá los detalles.
    //
    // RedirectAttributes: permite pasar mensajes entre
    // una redirección y la siguiente página.
    // ---------------------------------------------------
    @PostMapping("/registro")
    public String registrarUsuario(
            @Valid @ModelAttribute Usuario usuario,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // ---------------------------------------------------
        // Si las validaciones fallan (campo vacío, correo
        // inválido, contraseña muy corta), volvemos al
        // formulario con los errores visibles.
        //
        // Thymeleaf mostrará los mensajes de error
        // automáticamente gracias a th:field y la
        // integración con BindingResult.
        // ---------------------------------------------------
        if (result.hasErrors()) {
            return "registro";
        }

        // ---------------------------------------------------
        // Verificamos si el correo ya existe en la base
        // de datos usando el método del servicio.
        //
        // Si ya existe, agregamos un mensaje de error al
        // modelo y volvemos al formulario.
        // ---------------------------------------------------
        if (usuarioService.buscarPorCorreo(usuario.getCorreo()) != null) {
            model.addAttribute("errorCorreo", "El correo ya está registrado");
            return "registro";
        }

        // ---------------------------------------------------
        // Si todo está bien, guardamos el usuario en la
        // base de datos a través del servicio.
        // ---------------------------------------------------
        usuarioService.guardarUsuario(usuario);

        // ---------------------------------------------------
        // addFlashAttribute: guarda un mensaje que solo
        // se muestra UNA VEZ después de la redirección.
        // Es útil para mensajes de éxito.
        // ---------------------------------------------------
        redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");

        // ---------------------------------------------------
        // redirect: en lugar de renderizar una vista,
        // le dice al navegador que navegue a otra URL.
        // En este caso, redirige a /login.
        // ---------------------------------------------------
        return "redirect:/login";
    }

    // ---------------------------------------------------
    // GET /login
    //
    // Muestra el formulario de inicio de sesión.
    //
    // Agrega un Usuario vacío al modelo para que el
    // formulario pueda usar th:object y th:field.
    // ---------------------------------------------------
    @GetMapping("/login")
    public String mostrarLogin(Model model) {
        // Necesario para que th:object="${usuario}" funcione
        // en el formulario de login.html
        model.addAttribute("usuario", new Usuario());
        return "login";
    }

    // ---------------------------------------------------
    // POST /login
    //
    // Procesa el formulario de inicio de sesión.
    //
    // HttpSession: representa la sesión HTTP del usuario.
    // El servidor crea una sesión única para cada
    // navegador que se conecta.
    //
    // Flujo de autenticación manual:
    //   1. Buscar el usuario por correo en la base de datos
    //   2. Comparar la contraseña ingresada con la guardada
    //   3. Si coinciden → guardar usuario en sesión y
    //      redirigir a la página principal
    //   4. Si no coinciden → mostrar error y volver al login
    // ---------------------------------------------------
    @PostMapping("/login")
    public String iniciarSesion(
            @ModelAttribute Usuario usuario,
            HttpSession session,
            Model model) {

        // ---------------------------------------------------
        // Paso 1: Buscar usuario por correo en la base de
        // datos usando el servicio.
        //
        // Si el correo no existe, usuarioBD será null.
        // ---------------------------------------------------
        Usuario usuarioBD = usuarioService.buscarPorCorreo(usuario.getCorreo());

        // ---------------------------------------------------
        // Paso 2: Verificar si el usuario existe y la
        // contraseña coincide.
        //
        // usar.equals() compara el texto ingresado con el
        // texto guardado en la base de datos.
        // ---------------------------------------------------
        if (usuarioBD != null && usuarioBD.getPassword().equals(usuario.getPassword())) {

            // ---------------------------------------------------
            // Paso 3: Autenticación exitosa.
            //
            // session.setAttribute("usuarioLogueado", usuarioBD)
            // guarda el objeto Usuario en la sesión HTTP.
            //
            // Mientras la sesión esté activa, cualquier
            // página puede leer este atributo y saber quién
            // es el usuario que está navegando.
            // ---------------------------------------------------
            session.setAttribute("usuarioLogueado", usuarioBD);

            // Redirige a la lista de autos
            return "redirect:/autos";
        }

        // ---------------------------------------------------
        // Paso 4: Si el usuario no existe o la contraseña
        // no coincide, agregamos un mensaje de error al
        // modelo y volvemos a mostrar el formulario de login.
        // ---------------------------------------------------
        model.addAttribute("error", "Correo o contraseña incorrectos");
        return "login";
    }

    // ---------------------------------------------------
    // GET /logout
    //
    // Cierra la sesión del usuario.
    //
    // session.invalidate() elimina todos los datos
    // guardados en la sesión actual.
    //
    // Después de cerrar sesión, redirige al login.
    // ---------------------------------------------------
    @GetMapping("/logout")
    public String cerrarSesion(HttpSession session) {
        session.invalidate();
        return "redirect:/login";
    }
}
