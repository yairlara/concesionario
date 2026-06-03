# Concesionario - Proyecto Académico Spring Boot + Thymeleaf

Aplicación web para gestionar un concesionario de autos: permite registrar, listar, editar y eliminar **autos**, asociarles una **categoría** y subir una **imagen**. Además incluye gestión de **clientes**, **vendedores**, **sucursales** y **ventas**, todo protegido por un sistema de **autenticación con sesiones**.

Este README es una guía paso a paso para replicar el proyecto desde cero. Está pensada para estudiantes que están aprendiendo:

- Spring Boot (Web, Data JPA, Validation, Thymeleaf, DevTools)
- JPA / Hibernate con MySQL
- Plantillas Thymeleaf con Bootstrap 5
- Subida y publicación de archivos
- Validaciones de formulario y mensajes flash
- Autenticación manual con `HttpSession`
- Relaciones entre entidades (`@ManyToOne`)

---

## 1. Prerrequisitos

Antes de empezar, asegúrate de tener instalado:

- **JDK 25** (o el que use el `pom.xml`).
- **Maven** (o usa el `mvnw` incluido).
- **MySQL** local con un usuario con permisos.
- Un editor (VS Code, IntelliJ IDEA, Eclipse).

Crea la base de datos vacía:

```sql
CREATE DATABASE concesionario_db;
```

> El esquema de tablas se crea solo gracias a `spring.jpa.hibernate.ddl-auto=update`.

---

## 2. Crear el proyecto base

Genera el proyecto en [start.spring.io](https://start.spring.io) con:

- **Project:** Maven
- **Language:** Java
- **Spring Boot:** 3.5.x
- **Group:** `com.concesionario`
- **Artifact:** `concesionario`
- **Java:** 25
- **Dependencies:**
  - Spring Web
  - Spring Data JPA
  - Thymeleaf
  - Validation
  - MySQL Driver
  - Spring Boot DevTools
  - Lombok

Importa el proyecto y comprueba que arranca con:

```bash
./mvnw spring-boot:run
```

---

## 3. Configurar `application.properties`

`src/main/resources/application.properties`

```properties
spring.application.name=concesionario

spring.datasource.url=jdbc:mysql://localhost:3306/concesionario_db
spring.datasource.username=root
spring.datasource.password=1234

spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=true
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect

spring.jpa.defer-datasource-initialization=true
spring.sql.init.mode=always

server.error.include-message=always
server.error.include-stacktrace=always
server.error.include-binding-errors=always
```

Propiedades importantes:

| Propiedad | Para qué |
|---|---|
| `spring.datasource.*` | Credenciales y URL de MySQL. |
| `spring.jpa.hibernate.ddl-auto=update` | Crea/actualiza tablas según las entidades. |
| `spring.jpa.show-sql=true` | Muestra el SQL en consola (útil para depurar). |
| `spring.jpa.defer-datasource-initialization=true` | Asegura que el esquema exista antes de ejecutar `data.sql`. |
| `spring.sql.init.mode=always` | Ejecuta `data.sql` al arrancar para insertar datos iniciales. |

---

## 4. Poblar categorías con `data.sql`

`src/main/resources/data.sql`

```sql
INSERT INTO categoria (nombre) VALUES ('Sedán');
INSERT INTO categoria (nombre) VALUES ('Camioneta');
INSERT INTO categoria (nombre) VALUES ('Deportivo');
INSERT INTO categoria (nombre) VALUES ('Familiar');
```

> Este archivo se ejecuta automáticamente al arrancar la aplicación gracias a `spring.sql.init.mode=always`. Si lo ejecutas más de una vez, duplicará los registros — puedes agregar `INSERT IGNORE` o limpiar la tabla antes.

---

## 5. Estructura del proyecto

```
src/main/java/com/concesionario/concesionario/
├── ConcesionarioApplication.java
├── config/
│   └── WebConfig.java
├── controller/
│   ├── AutoController.java
│   ├── ClienteController.java
│   ├── SucursalController.java
│   ├── VendedorController.java
│   ├── VentaController.java
│   ├── UsuarioController.java
│   └── GlobalExceptionHandler.java
├── converter/
│   ├── StringToAutoConverter.java
│   ├── StringToCategoriaConverter.java
│   ├── StringToClienteConverter.java
│   ├── StringToSucursalConverter.java
│   └── StringToVendedorConverter.java
├── model/
│   ├── Auto.java
│   ├── Categoria.java
│   ├── Cliente.java
│   ├── Sucursal.java
│   ├── Vendedor.java
│   ├── Venta.java
│   └── Usuario.java
├── repository/
│   ├── AutoRepository.java
│   ├── CategoriaRepository.java
│   ├── ClienteRepository.java
│   ├── SucursalRepository.java
│   ├── VendedorRepository.java
│   ├── VentaRepository.java
│   └── UsuarioRepository.java
└── service/
    ├── AutoService.java
    ├── CategoriaService.java
    ├── ClienteService.java
    ├── SucursalService.java
    ├── VendedorService.java
    ├── VentaService.java
    ├── UsuarioService.java
    └── FileStorageService.java

src/main/resources/
├── application.properties
├── data.sql
├── static/uploads/         (se crea automáticamente al subir imágenes)
└── templates/
    ├── index.html
    ├── formulario.html
    ├── login.html
    ├── registro.html
    ├── clientes.html
    ├── formulario-cliente.html
    ├── sucursales.html
    ├── formulario-sucursal.html
    ├── vendedores.html
    ├── formulario-vendedor.html
    ├── ventas.html
    └── formulario-venta.html
```

---

## 6. Paso a paso

### Paso 1 — Entidad `Categoria`

`src/main/java/com/concesionario/concesionario/model/Categoria.java`

```java
@Entity
public class Categoria {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    // getters y setters
}
```

- `@Entity` marca la clase como tabla JPA.
- `@Id` + `@GeneratedValue(IDENTITY)` → MySQL genera el `id` automáticamente.
- Las categorías se insertan con `data.sql` al arrancar.

---

### Paso 2 — Entidad `Auto`

`src/main/java/com/concesionario/concesionario/model/Auto.java`

```java
@Entity
public class Auto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo marca no puede estar vacío")
    private String marca;

    @NotBlank(message = "El campo modelo no puede estar vacío")
    private String modelo;

    @Size(min = 4, max = 4, message = "El campo año debe tener exactamente 4 caracteres")
    private String anio;

    @NotNull(message = "El campo precio no puede estar vacío")
    private Double precio;

    private boolean disponible;

    @ManyToOne
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;

    private String imagen;

    // getters y setters
}
```

Conceptos clave:
- `@NotBlank` y `@NotNull` activan validaciones de Bean Validation.
- `@ManyToOne` define la relación muchos-a-uno con `Categoria` mediante la FK `categoria_id`.
- `imagen` guarda solo el **nombre del archivo**, no los bytes.

---

### Paso 3 — Entidad `Cliente`

```java
@Entity
public class Cliente {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El campo nombre no puede estar vacío")
    private String nombre;

    @NotBlank(message = "El campo cédula no puede estar vacío")
    private String cedula;

    @NotBlank(message = "El campo teléfono no puede estar vacío")
    private String telefono;

    @NotBlank(message = "El campo dirección no puede estar vacío")
    private String direccion;

    @NotBlank(message = "El campo correo no puede estar vacío")
    @Email(message = "Debe ingresar un correo electrónico válido")
    private String correo;

    // getters y setters
}
```

---

### Paso 4 — Entidad `Sucursal`

```java
@Entity
public class Sucursal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String nombre;
    @NotBlank private String ciudad;
    @NotBlank private String direccion;
    @NotBlank private String telefono;

    // getters y setters
}
```

---

### Paso 5 — Entidad `Vendedor`

```java
@Entity
public class Vendedor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank private String nombre;
    @NotBlank private String telefono;
    @NotBlank private String email;

    @NotNull
    private Double comision;

    @ManyToOne
    @JoinColumn(name = "sucursal_id")
    private Sucursal sucursal;

    // getters y setters
}
```

- Un `Vendedor` pertenece a una `Sucursal` mediante `@ManyToOne`.

---

### Paso 6 — Entidad `Venta`

```java
@Entity
public class Venta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull private LocalDate fecha;
    @NotNull private Double precioFinal;

    @ManyToOne
    @JoinColumn(name = "auto_id")
    private Auto auto;

    @ManyToOne
    @JoinColumn(name = "cliente_id")
    private Cliente cliente;

    @ManyToOne
    @JoinColumn(name = "vendedor_id")
    private Vendedor vendedor;

    // getters y setters
}
```

- `Venta` es la entidad más relacionada del proyecto: une `Auto`, `Cliente` y `Vendedor` en una sola transacción.

---

### Paso 7 — Entidad `Usuario`

```java
@Entity
public class Usuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @NotBlank @Email(message = "Debe ingresar un correo válido")
    @Column(unique = true)
    private String correo;

    @NotBlank
    @Size(min = 6, message = "La contraseña debe tener al menos 6 caracteres")
    private String password;

    // getters y setters
}
```

- `@Column(unique = true)` evita que dos usuarios se registren con el mismo correo.

---

### Paso 8 — Repositorios

Todos los repositorios extienden `JpaRepository` y heredan automáticamente `findAll`, `save`, `findById`, `deleteById`, etc.

```java
public interface AutoRepository extends JpaRepository<Auto, Long> {}
public interface ClienteRepository extends JpaRepository<Cliente, Long> {}
public interface SucursalRepository extends JpaRepository<Sucursal, Long> {}
public interface VendedorRepository extends JpaRepository<Vendedor, Long> {}
public interface VentaRepository extends JpaRepository<Venta, Long> {}
public interface CategoriaRepository extends JpaRepository<Categoria, Long> {}
```

`UsuarioRepository` agrega un método de consulta derivada:

```java
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    // Spring genera: SELECT * FROM usuario WHERE correo = ?
    Usuario findByCorreo(String correo);
}
```

---

### Paso 9 — Servicios

Cada servicio encapsula la lógica de negocio y delega el acceso a datos al repositorio.

`AutoService.java` (ejemplo representativo):

```java
@Service
public class AutoService {

    @Autowired
    private AutoRepository repository;

    public List<Auto> listarAutos() { return repository.findAll(); }
    public Auto guardarAuto(Auto auto) { return repository.save(auto); }
    public Auto obtenerAutoPorId(Long id) { return repository.findById(id).orElse(null); }
    public void eliminar(Long id) { repository.deleteById(id); }
}
```

`UsuarioService` agrega búsqueda por correo para el login:

```java
@Service
public class UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario guardarUsuario(Usuario usuario) { return usuarioRepository.save(usuario); }
    public Usuario buscarPorCorreo(String correo) { return usuarioRepository.findByCorreo(correo); }
}
```

---

### Paso 10 — Converters (String → Entidad)

Cuando un `<select>` del formulario envía el `id` de una entidad relacionada, llega como `String`. Spring necesita saber cómo convertirlo a la entidad correspondiente para hacer el binding.

`StringToCategoriaConverter.java` (ejemplo representativo):

```java
@Component
public class StringToCategoriaConverter implements Converter<String, Categoria> {

    private final CategoriaRepository categoriaRepository;

    public StringToCategoriaConverter(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Override
    public Categoria convert(String source) {
        if (source == null || source.isBlank()) return null;
        return categoriaRepository.findById(Long.parseLong(source)).orElse(null);
    }
}
```

El proyecto tiene 5 converters: `StringToAutoConverter`, `StringToCategoriaConverter`, `StringToClienteConverter`, `StringToSucursalConverter` y `StringToVendedorConverter`.

> Sin estos converters verías el error: *"Failed to convert from type String to [Entidad]"* al enviar formularios con `<select>`.

---

### Paso 11 — Servicio para guardar imágenes

`service/FileStorageService.java`

```java
@Service
public class FileStorageService {

    private static final List<String> EXTENSIONES_PERMITIDAS = Arrays.asList("jpg", "jpeg", "png", "gif");

    private static final Path DIRECTORIO_UPLOADS = Paths.get(
            System.getProperty("user.dir"),
            "src", "main", "resources", "static", "uploads");

    public String guardar(MultipartFile archivo) {
        // Valida extensión, genera nombre único con UUID,
        // crea el directorio si no existe y copia el archivo
        String extension = obtenerExtension(archivo.getOriginalFilename());
        if (!EXTENSIONES_PERMITIDAS.contains(extension))
            throw new IllegalArgumentException("Extensión no permitida.");
        String nombreFinal = UUID.randomUUID() + "." + extension;
        Files.copy(archivo.getInputStream(),
                   DIRECTORIO_UPLOADS.resolve(nombreFinal),
                   StandardCopyOption.REPLACE_EXISTING);
        return nombreFinal;
    }

    public void eliminar(String nombre) {
        Files.deleteIfExists(DIRECTORIO_UPLOADS.resolve(nombre));
    }
}
```

Decisiones clave:
- **Lista blanca de extensiones** (jpg, jpeg, png, gif) en lugar de lista negra.
- **Renombrado con UUID** para evitar colisiones y ataques de path traversal.
- Devuelve solo el **nombre** que se guarda en la columna `imagen` de la BD.

---

### Paso 12 — Servir imágenes por HTTP

`config/WebConfig.java`

```java
@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        Path uploadsDir = Paths.get(
                System.getProperty("user.dir"),
                "src", "main", "resources", "static", "uploads");

        registry.addResourceHandler("/uploads/**")
                .addResourceLocations(uploadsDir.toUri().toString());
    }
}
```

Esto mapea la URL pública `/uploads/foto.jpg` al archivo físico en disco. Sin este bean, las imágenes recién subidas **no** se mostrarían en el navegador.

---

### Paso 13 — Autenticación con sesiones

El proyecto implementa autenticación manual usando `HttpSession`, sin Spring Security.

**Registro** — `UsuarioController.java`:

```java
@PostMapping("/registro")
public String registrarUsuario(@Valid @ModelAttribute Usuario usuario,
                               BindingResult result, Model model,
                               RedirectAttributes redirectAttributes) {
    if (result.hasErrors()) return "registro";
    if (usuarioService.buscarPorCorreo(usuario.getCorreo()) != null) {
        model.addAttribute("errorCorreo", "El correo ya está registrado");
        return "registro";
    }
    usuarioService.guardarUsuario(usuario);
    redirectAttributes.addFlashAttribute("mensaje", "Registro exitoso. Ahora puedes iniciar sesión.");
    return "redirect:/login";
}
```

**Login** — guardar el usuario en sesión:

```java
@PostMapping("/login")
public String iniciarSesion(@ModelAttribute Usuario usuario,
                            HttpSession session, Model model) {
    Usuario usuarioBD = usuarioService.buscarPorCorreo(usuario.getCorreo());
    if (usuarioBD != null && usuarioBD.getPassword().equals(usuario.getPassword())) {
        session.setAttribute("usuarioLogueado", usuarioBD);
        return "redirect:/autos";
    }
    model.addAttribute("error", "Correo o contraseña incorrectos");
    return "login";
}
```

**Logout** — invalidar la sesión:

```java
@GetMapping("/logout")
public String cerrarSesion(HttpSession session) {
    session.invalidate();
    return "redirect:/login";
}
```

**Protección de rutas** — cada método de cada controlador verifica la sesión antes de proceder:

```java
if (session.getAttribute("usuarioLogueado") == null) {
    redirectAttributes.addFlashAttribute("mensaje", "Debes iniciar sesión primero");
    return "redirect:/login";
}
```

---

### Paso 14 — Controlador de Autos

`controller/AutoController.java` gestiona el CRUD completo de autos con validación, subida de imágenes y protección de sesión.

Puntos destacados:
- **`@Valid` + `BindingResult`**: si hay errores de validación, vuelve al formulario sin redirect.
- **`MultipartFile` opcional**: en una edición, si no se sube imagen nueva, se conserva la actual.
- **Reemplazo seguro de imagen**: primero guarda la nueva, luego borra la anterior.
- **Mensajes flash** con `RedirectAttributes` para mostrar éxito tras un redirect.

---

### Paso 15 — Manejador global de errores

`controller/GlobalExceptionHandler.java`

```java
@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MaxUploadSizeExceededException.class)
    public String manejarArchivoMuyGrande(MaxUploadSizeExceededException ex,
                                          RedirectAttributes redirectAttributes) {
        redirectAttributes.addFlashAttribute("mensajeError",
                "La imagen es demasiado grande. El tamaño máximo permitido es 10 MB.");
        return "redirect:/autos/nuevo";
    }
}
```

Captura el error cuando alguien sube una imagen mayor al límite **antes** de que llegue al controlador.

---

## 7. Diagrama de relaciones entre entidades

```
Categoria
    └── Auto (ManyToOne → categoria_id)

Sucursal
    └── Vendedor (ManyToOne → sucursal_id)

Venta
    ├── Auto     (ManyToOne → auto_id)
    ├── Cliente  (ManyToOne → cliente_id)
    └── Vendedor (ManyToOne → vendedor_id)

Usuario (independiente, maneja autenticación)
```

---

## 8. Cómo ejecutar

```bash
./mvnw spring-boot:run
```

Abre el navegador en: http://localhost:8080/login

Flujo sugerido para probarlo:

1. Ir a `/registro` y crear un usuario.
2. Iniciar sesión en `/login`.
3. Ir a **Autos → Nuevo** y registrar un auto con imagen y categoría.
4. Probar editar el auto: sin subir imagen nueva → conserva la actual; subiendo imagen → reemplaza.
5. Registrar una **Sucursal**, luego un **Vendedor** asignado a esa sucursal.
6. Registrar un **Cliente**.
7. Crear una **Venta** asociando un auto, un cliente y un vendedor.
8. Probar el logout y verificar que las rutas protegidas redirigen al login.

---

## 9. Errores frecuentes (FAQ)

- **No se sube el archivo:** falta `enctype="multipart/form-data"` en el `<form>`.
- **Imágenes no se ven en el navegador:** falta el `WebConfig` o la URL es incorrecta.
- **Error al convertir `<select>` de entidad relacionada:** falta el Converter correspondiente.
- **Error 413 al subir imagen:** ajustar el límite en `application.properties` con `spring.servlet.multipart.max-file-size`.
- **Las categorías de `data.sql` se duplican al reiniciar:** usar `INSERT IGNORE` o cambiar `spring.sql.init.mode=never` después del primer arranque.
- **`redirect:/login` en todas las páginas:** la sesión expiró o nunca se inició — ir a `/login`.

---

## 10. Ejercicios sugeridos

1. Agregar paginación al listado de autos.
2. Crear una vista de detalle para cada venta que muestre toda la información relacionada.
3. Cifrar las contraseñas de los usuarios con `BCryptPasswordEncoder` antes de guardarlas.
4. Agregar un campo `rol` al `Usuario` (ADMIN / EMPLEADO) y restringir acciones según el rol.
5. Implementar búsqueda de autos por marca o modelo.
6. Permitir que al registrar una venta, el auto quede marcado como `disponible = false` automáticamente.

---

## 11. Tecnologías usadas

- Spring Boot 3.5
- Spring Data JPA / Hibernate
- Bean Validation (Jakarta)
- Thymeleaf
- Bootstrap 5
- MySQL 8
- Maven
- Lombok
- Java 25
