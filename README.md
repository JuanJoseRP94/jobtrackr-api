# 🧭 JobTrackr API

API REST construida con **Spring Boot** para gestionar candidaturas de empleo: empresa, puesto, estado (aplicado / entrevista / oferta / rechazado), fuente y notas — con **autenticación JWT** para que cada usuario gestione solo sus propias candidaturas.

📄 **[Documentación interactiva (Swagger UI)](https://jobtrackr-api-m21i.onrender.com)** — prueba todos los endpoints desde el navegador, sin escribir código.

> ⚠️ Si el enlace tarda ~50 segundos en cargar la primera vez, es normal: el hosting gratuito "duerme" el servicio tras un rato de inactividad.

## ✨ Qué incluye

- **Autenticación con JWT**: registro/login, contraseñas encriptadas con BCrypt, cada usuario ve solo sus propias candidaturas.
- **CRUD completo** con validaciones (Bean Validation) y manejo de errores centralizado (`@RestControllerAdvice`) — sin stacktraces feos expuestos al cliente.
- **Arquitectura en capas**: Controller → Service → Repository, con DTOs separados de las entidades.
- **Tests unitarios** (JUnit 5 + Mockito) sobre la capa de servicio, con mocks del repositorio y del usuario autenticado.
- **Documentación OpenAPI/Swagger** interactiva y autenticable desde el propio navegador.

## 🛠️ Stack técnico

- **Java 21** · Spring Boot 3
- Spring Web, Spring Data JPA, Spring Security
- H2 (base de datos en memoria)
- JWT (io.jsonwebtoken)
- JUnit 5 + Mockito
- springdoc-openapi (Swagger UI)
- Docker

## 🚀 Ejecutar en local

Requisitos: Java 21 y Maven (o usa el wrapper incluido, no hace falta instalar Maven).

```bash
git clone https://github.com/JuanJoseRP94/jobtrackr-api.git
cd jobtrackr-api
./mvnw spring-boot:run        # En Windows: .\mvnw.cmd spring-boot:run
```

La app arranca en `http://localhost:8080`. Documentación interactiva en `http://localhost:8080/swagger-ui.html`.

### Probar los endpoints

1. `POST /api/auth/register` con `{"email": "...", "password": "..."}` (mínimo 8 caracteres).
2. `POST /api/auth/login` con las mismas credenciales → devuelve un `token`.
3. En Swagger, clic en **Authorize** (arriba a la derecha) y pega el token.
4. Prueba `POST /api/applications`, `GET /api/applications`, etc.

También incluyo un archivo [`requests.http`](requests.http) con ejemplos listos para usar con la extensión [REST Client](https://marketplace.visualstudio.com/items?itemName=humao.rest-client) de VS Code.

## 📁 Estructura del proyecto