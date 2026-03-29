# ms-cuentas

Microservicio de cuentas y movimientos (Spring Boot 3, Java 17) para el ejercicio Senior.

## Funcionalidades

- F1: CRUD de `clientes`, `cuentas`, `movimientos`
- F2/F3: registro de movimientos con actualización de saldo y validación `Saldo no disponible`
- F4: reporte por rango y cliente en `/api/reportes`
- F5: prueba unitaria de dominio (`ClienteRefTest`) y de servicio (`MovimientoServiceImplTest`)
- F6: prueba de integración (`MovimientoControllerIT`)
- F7: despliegue en contenedores (`Dockerfile` y `docker-compose.yml`)

## Endpoints base

Con `server.servlet.context-path=/api`:

- `GET|POST /api/clientes`
- `GET|PUT|PATCH|DELETE /api/clientes/{id}`
- `GET|POST /api/cuentas`
- `GET|PUT|PATCH|DELETE /api/cuentas/{id}`
- `GET|POST /api/movimientos`
- `GET|PUT|PATCH|DELETE /api/movimientos/{id}`
- `GET /api/reportes?fechaInicio=yyyy-MM-dd&fechaFin=yyyy-MM-dd&clienteId={id}`
- `GET /api/reportes?fecha=yyyy-MM-dd,yyyy-MM-dd&clienteId={id}`

## Comunicacion asincrona

Se consume `ClienteEvent` desde RabbitMQ:

- Exchange: `exchange.clientes`
- Queue: `queue.clientes`
- Routing key: `cliente.event`

El consumer actualiza la tabla local `cliente_ref`.

## Ejecutar local

1. Configura PostgreSQL y RabbitMQ.
2. Ajusta variables de entorno si no usas defaults de `application.yaml`.
3. Ejecuta:

```bash
./mvnw spring-boot:run
```

En Windows PowerShell:

```powershell
.\mvnw.cmd spring-boot:run
```

## Pruebas

```powershell
.\mvnw.cmd test
```

## Docker

```powershell
docker compose up --build
```

Para levantar ambos microservicios juntos desde la carpeta raiz del workspace, usa:

```powershell
docker compose -f ..\docker-compose.yml up --build
```

## Archivos de entrega

- Script BD: `BaseDatos.sql`
- Coleccion Postman: `postman/ms-cuentas.postman_collection.json`

