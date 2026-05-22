package com.concesionario.concesionario.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.concesionario.concesionario.model.Usuario;

// ---------------------------------------------------
// UsuarioRepository extiende JpaRepository.
// Esto le da acceso automático a métodos CRUD como:
//   save()        → guardar o actualizar
//   findById()    → buscar por id
//   findAll()     → listar todos
//   deleteById()  → eliminar por id
//
// <Usuario, Long> indica:
//   - Usuario: la entidad que este repositorio maneja
//   - Long:    el tipo de dato de la clave primaria
// ---------------------------------------------------
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    // ---------------------------------------------------
    // findByCorreo(): método de consulta derivada.
    //
    // Spring Data JPA lee el nombre del método y genera
    // automáticamente la consulta SQL:
    //   SELECT * FROM usuario WHERE correo = ?
    //
    // Solo debe coincidir el nombre del campo "correo"
    // con el atributo definido en la entidad Usuario.
    //
    // Devuelve un Usuario si encuentra, o null si no.
    // ---------------------------------------------------
    Usuario findByCorreo(String correo);
}
