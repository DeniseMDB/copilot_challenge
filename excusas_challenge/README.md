# Excusas Sharks

Generador de "excusas tech" divertido y reproducible: combina fragmentos (contexto, causa, consecuencia, recomendación), memes y "leyes"/axiomas del mundo IT para producir respuestas tipo `SIMPLE`, `CON_MEME`, `CON_LEY` o `ULTRA_SHARK`.


## Características
- Generación de excusas aleatorias y por rol.
- Modo ULTRA que devuelve excusa + meme + ley.
- CRUD de fragmentos, memes y leyes (persistencia en H2 en memoria).
- Documentación OpenAPI / Swagger.
- DataLoader que puede precargar JSONs en `docs/json/`.

## Quick Start (Windows PowerShell)
- Compilar y ejecutar tests:

```powershell
.\mvnw.cmd clean package
```

- Ejecutar en desarrollo:

```powershell
.\mvnw.cmd spring-boot:run
```

- Ejecutar JAR generado:

```powershell
java -jar .\target\excusas_challenge-0.0.1-SNAPSHOT.jar
```

- Ejecutar tests:

```powershell
.\mvnw.cmd test
```

## Endpoints relevantes
- GET /excuses/random  — Excusa aleatoria (puede aceptar `seed` para reproducibilidad)
- GET /excuse/ultra    — Excusa ULTRA (excusa + meme + ley)
- CRUD: /fragments, /memes, /laws
- Swagger UI: `http://localhost:8080/swagger-ui.html` o `http://localhost:8080/swagger-ui/index.html`

## Ejemplos JSON de respuesta
A continuación se muestran dos ejemplos que puedes usar como referencia para los endpoints `/excuses/random` y `/excuse/ultra`.

### Ejemplo: `/excuses/random` (respuesta JSON)

```json
{
  "excusa": {
    "contexto": "Estábamos deployando un hotfix",
    "causa": "rollback manual mal ejecutado",
    "consecuencia": "cache inválida y carga excesiva en la base de datos",
    "recomendacion": "añadir migraciones transaccionales y pruebas previas"
  },
  "meme": "Nada tan argentino como decir 'queda para hardening' y no tocarlo nunca más.",
  "ley": "La cultura de 'no buscar culpables' dura hasta que aparece el primer incidente grande."
}
```

### Ejemplo: `/excuse/ultra` (respuesta JSON)

```json
{
  "contexto": "Refactor de autenticación",
  "causa": "el token de CI/CD venció",
  "consecuencia": "tabla truncada y pérdida parcial de registros",
  "recomendacion": "automatizar la rotación de secretos"
}
```

## Datos de ejemplo
Usa los JSON en `docs/json/` para precargar datos (memes, leyes, fragmentos) mediante `DataLoader`.

## PlantUML
Diagrama(s) PlantUML en `docs/plantuml/` — hay un script `scripts/generate-plantuml.ps1` para generar imágenes si tienes Java o Docker.

## Docker
Se incluye un `Dockerfile` multi-stage en la raíz para construir y ejecutar la aplicación.

- Construir la imagen (desde la raíz del proyecto):

```powershell
docker build -t excusas-sharks:latest .
```

- Ejecutar el contenedor exponiendo el puerto 8080:

```powershell
docker run --rm -p 8080:8080 excusas-sharks:latest
```

Nota: el Dockerfile usa Maven en la etapa de build y un runtime de Java 17 en la etapa final. En CI puedes omitir `-DskipTests` para ejecutar tests durante la construcción.

## Contribuir
1. Fork
2. Crear branch: `git checkout -b feature/mi-cambio`
3. Commit con Conventional Commits
4. Abrir PR

---
