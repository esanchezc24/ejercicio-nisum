# Proyecto: API de Gestión de Usuarios

## Descripción
Este proyecto es una API RESTful desarrollada con Spring Boot que permite la gestión de usuarios. Proporciona funcionalidades para crear, actualizar, eliminar, restaurar y consultar usuarios. Además, incluye validaciones, manejo de excepciones y documentación interactiva mediante Swagger UI.

## Funcionalidades
- **Crear usuario**: Permite registrar un nuevo usuario con datos como nombre, email, contraseña y teléfonos.
- **Consultar usuarios**: Obtiene la lista de todos los usuarios registrados o un usuario específico por su ID.
- **Actualizar usuario**: Modifica los datos de un usuario existente.
- **Eliminar usuario**: Marca un usuario como eliminado.
- **Restaurar usuario**: Restaura un usuario previamente eliminado.

## Tecnologías utilizadas
- **Java 21**
- **Spring Boot 3.5.4**
- **H2 Database**: Base de datos en memoria para pruebas.
- **Spring Data JPA**: Para la interacción con la base de datos.
- **Spring Validation**: Para validaciones de datos de entrada.
- **SpringDoc OpenAPI**: Para la documentación de la API.

## Configuración del proyecto
### Prerrequisitos
- **Java 21** instalado.
- **Maven** instalado.

### Configuración de la base de datos
El proyecto utiliza H2 Database en memoria. La consola de H2 está habilitada y accesible en:
```
http://localhost:8080/h2-console
```
Credenciales predeterminadas:
- **URL**: `jdbc:h2:mem:nisumdb`
- **Usuario**: `sa`
- **Contraseña**: *(vacío)*

### Configuración de Swagger
La documentación interactiva de la API está disponible en:
```
http://localhost:8080/swagger-ui.html
```
La especificación OpenAPI en formato JSON está disponible en:
```
http://localhost:8080/v3/api-docs
```
Y en formato YAML:
```
http://localhost:8080/v3/api-docs.yaml
```

## Cómo ejecutar el proyecto
1. Clona este repositorio.
2. Navega al directorio del proyecto.
3. Ejecuta el siguiente comando para compilar y ejecutar la aplicación:
```
mvn spring-boot:run
```
4. La aplicación estará disponible en:
```
http://localhost:8080
```

## Uso de la API
### Endpoints principales
- **GET /api/v1/users**: Obtiene todos los usuarios.
- **POST /api/v1/users**: Crea un nuevo usuario.
- **GET /api/v1/users/{id}**: Obtiene un usuario por su ID.
- **PUT /api/v1/users/{id}**: Actualiza un usuario existente.
- **DELETE /api/v1/users/{id}**: Elimina un usuario.
- **PATCH /api/v1/users/{id}/restore**: Restaura un usuario eliminado.

### Ejemplo de solicitud para crear un usuario
**Endpoint**: `POST /api/v1/users`

**Cuerpo de la solicitud**:
```json
{
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "password": "Password123!",
  "phones": [
    {
      "number": "123456789",
      "cityCode": "1",
      "countryCode": "57"
    }
  ]
}
```

**Respuesta exitosa**:
```json
{
  "id": "uuid",
  "name": "Juan Pérez",
  "email": "juan.perez@example.com",
  "phones": [
    {
      "number": "123456789",
      "cityCode": "1",
      "countryCode": "57"
    }
  ],
  "createdAt": "2025-08-22T19:00:00",
  "updatedAt": "2025-08-22T19:00:00",
  "lastLogin": "2025-08-22T19:00:00",
  "isActive": true
}
```

## Pruebas
El proyecto incluye pruebas unitarias y de integración. Para ejecutarlas, utiliza el siguiente comando:
```
mvn test
```

## Autor
Este proyecto fue desarrollado por Erick Sánchez Chávez como parte de un ejercicio práctico.

