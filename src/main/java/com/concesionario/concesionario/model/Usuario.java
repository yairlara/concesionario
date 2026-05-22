package com.concesionario.concesionario.model;

// ---------------------------------------------------
// Anotaciones JPA para mapear esta clase a una tabla
// de base de datos
// ---------------------------------------------------
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

// ---------------------------------------------------
// Anotaciones de Jakarta Validation para validar los
// datos ingresados por el usuario en los formularios
// ---------------------------------------------------
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// ---------------------------------------------------
// @Entity le indica a JPA que esta clase representa
// una tabla en la base de datos.
// Por defecto, el nombre de la tabla será "usuario"
// ---------------------------------------------------
@Entity
public class Usuario {

    // ---------------------------------------------------
    // @Id marca este campo como la clave primaria de la
    // tabla.
    //
    // @GeneratedValue le dice a JPA que el valor del id
    // se generará automáticamente.
    // strategy = GenerationType.IDENTITY usa el
    // auto-increment de MySQL.
    // ---------------------------------------------------
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // ---------------------------------------------------
    // @NotBlank valida que el campo no esté vacío ni
    // tenga solo espacios en blanco.
    // El mensaje "message" se muestra en la vista si la
    // validación falla.
    // ---------------------------------------------------
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    // ---------------------------------------------------
    // @Email valida que el texto ingresado tenga formato
    // de correo electrónico (ej: usuario@correo.com).
    //
    // @Column(unique = true) agrega una restricción
    // UNIQUE en la columna "correo" de MySQL.
    // Esto evita que dos usuarios se registren con el
    // mismo correo.
    // ---------------------------------------------------
    @NotBlank(message = "El correo es obligatorio")
    @Email(message = "Debe ingresar un correo válido")
    @Column(unique = true)
    private String correo;

    // ---------------------------------------------------
    // @Size(min = 6) exige que la contraseña tenga al
    // menos 6 caracteres.
    // ---------------------------------------------------
    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // ---------------------------------------------------
    // Constructor vacío: JPA lo necesita para crear
    // objetos al recuperar datos de la base de datos.
    // ---------------------------------------------------
    public Usuario() {
    }

    // ---------------------------------------------------
    // Getters y setters manuales (sin Lombok).
    // Cada campo público necesita un getter (leer) y
    // un setter (escribir) para que Thymeleaf y JPA
    // puedan acceder a ellos.
    // ---------------------------------------------------

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
