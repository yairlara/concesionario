package com.concesionario.concesionario.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.concesionario.concesionario.model.Usuario;
import com.concesionario.concesionario.repository.UsuarioRepository;

// ---------------------------------------------------
// @Service: esta anotación le indica a Spring que
// esta clase contiene la lógica de negocio de la
// aplicación.
//
// El servicio se encarga de:
//   - Ejecutar la lógica antes de guardar o consultar
//   - Llamar al repositorio cuando necesita acceso a
//     la base de datos
// ---------------------------------------------------
@Service
public class UsuarioService {

    // ---------------------------------------------------
    // @Autowired: Spring crea automáticamente una
    // instancia de UsuarioRepository y la asigna aquí.
    // Esto se llama "inyección de dependencias".
    //
    // El servicio no crea el repositorio directamente,
    // sino que Spring se lo entrega listo para usar.
    // ---------------------------------------------------
    @Autowired
    private UsuarioRepository usuarioRepository;

    // ---------------------------------------------------
    // guardarUsuario(): recibe un objeto Usuario y lo
    // guarda en la base de datos.
    //
    // Si el usuario es nuevo (id = null), JPA hace un
    // INSERT. Si ya existe (id con valor), hace un
    // UPDATE.
    //
    // Devuelve el usuario guardado (con el id generado).
    // ---------------------------------------------------
    public Usuario guardarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    // ---------------------------------------------------
    // buscarPorCorreo(): busca un usuario por su correo
    // electrónico en la base de datos.
    //
    // Usa el método findByCorreo() del repositorio.
    // Si no encuentra ningún usuario, devuelve null.
    //
    // Este método se usará más adelante para el login.
    // ---------------------------------------------------
    public Usuario buscarPorCorreo(String correo) {
        return usuarioRepository.findByCorreo(correo);
    }
}
