# Handoff tecnico para terminar `ms-cuentas`

Este documento sirve para que el equipo de `ms-cuentas` replique el estilo de `ms-clientes` y cierre el ejercicio a nivel Senior.

## 1) Objetivo

Implementar en `ms-cuentas`:
- CRUD de cuentas (`/api/cuentas`)
- CRUD de movimientos (`/api/movimientos`)
- Reporte de estado de cuenta (`/api/reportes`)
- Regla de saldo insuficiente con mensaje exacto: `Saldo no disponible`
- Consumo asincrono de eventos de cliente desde RabbitMQ (sin llamadas HTTP sincronas entre micros)

---

## 2) Patron y estilo que ya se uso en `ms-clientes`

### Arquitectura por capas
Se uso la siguiente separacion:
- `controller/` REST
- `service/` logica de negocio (`interface` + `impl`)
- `repository/` Spring Data JPA
- `domain/` entidades JPA
- `dto/` request/response
- `mapper/` conversion entity <-> dto
- `exception/` excepciones custom + `@RestControllerAdvice`
- `messaging/` publisher/consumer de RabbitMQ
- `config/` beans de configuracion

### Convenciones vistas en codigo
Referencia directa en `ms-clientes`:
- `src/main/java/com/proyecto/msclientes/controller/ClienteController.java`
- `src/main/java/com/proyecto/msclientes/service/impl/ClienteServiceImpl.java`
- `src/main/java/com/proyecto/msclientes/exception/GlobalExceptionHandler.java`
- `src/main/java/com/proyecto/msclientes/config/RabbitMQConfig.java`

### Mapeo (sin MapStruct)
`ms-clientes` ya quedo con mapeo manual convencional:
- `src/main/java/com/proyecto/msclientes/mapper/ClienteMapper.java`

Patron usado:
- `toEntity(RequestDTO)`
- `toResponseDTO(Entity)`
- `updateEntityFromDTO(RequestDTO, Entity)`
- Update parcial ignorando `null`

**Importante:** mantener este mismo estilo en `ms-cuentas` para consistencia y defensa tecnica.

---

## 3) Contrato de mensajeria entre microservicios

### Que publica `ms-clientes`
`ms-clientes` publica eventos de cliente en RabbitMQ:
- Exchange: `exchange.clientes`
- Queue: `queue.clientes`
- Routing key: `cliente.event`

Definido en:
- `src/main/java/com/proyecto/msclientes/config/RabbitMQConfig.java`

Evento publicado (`ClienteEvent`):
```json
{
  "clienteId": 1,
  "nombre": "Jose Lema",
  "identificacion": "1234567890",
  "estado": true,
  "eventType": "CREATED"
}
```
`eventType` posibles: `CREATED`, `UPDATED`, `DELETED`.

### Que debe hacer `ms-cuentas`
Crear un consumer que escuche `queue.clientes` y mantenga una tabla local de referencia de cliente (ej: `cliente_ref`):
- `cliente_id` (PK)
- `nombre`
- `identificacion`
- `estado`
- `updated_at`

Regla por evento:
- `CREATED`: inserta si no existe
- `UPDATED`: actualiza
- `DELETED`: marcar inactivo o borrar logico (recomendado: inactivo)

Con esto, `ms-cuentas` valida cliente sin invocar HTTP a `ms-clientes`.

---

## 4) Modelo de dominio esperado en `ms-cuentas`

### Entidad `Cuenta`
Campos minimos:
- `cuentaId` (PK)
- `numeroCuenta` (UNIQUE)
- `tipoCuenta` (Ahorro/Corriente)
- `saldoInicial` (decimal)
- `estado` (boolean)
- `clienteId` (referencia logica al cliente)

### Entidad `Movimiento`
Campos minimos:
- `movimientoId` (PK)
- `fecha` (timestamp)
- `tipoMovimiento` (Deposito/Retiro)
- `valor` (positivo o negativo)
- `saldo` (saldo resultante luego del movimiento)
- `cuenta` (`@ManyToOne`)

---

## 5) Endpoints a implementar en `ms-cuentas`

Base path recomendado igual que `ms-clientes`: `server.servlet.context-path=/api`

### Cuentas
- `GET /api/cuentas`
- `GET /api/cuentas/{id}`
- `POST /api/cuentas`
- `PUT /api/cuentas/{id}`
- `DELETE /api/cuentas/{id}`

### Movimientos
- `GET /api/movimientos`
- `GET /api/movimientos/{id}`
- `POST /api/movimientos`
- `PUT /api/movimientos/{id}`
- `DELETE /api/movimientos/{id}`

### Reportes
- `GET /api/reportes?fechaInicio=yyyy-MM-dd&fechaFin=yyyy-MM-dd&clienteId={id}`

Retorno JSON con:
- datos del cliente
- cuentas del cliente
- movimientos por cuenta en el rango

---

## 6) Logica critica (F2 y F3)

En `MovimientoServiceImpl`:
1. Buscar cuenta
2. Calcular `nuevoSaldo = saldoActual + valorMovimiento`
3. Si `nuevoSaldo < 0` -> lanzar `SaldoInsuficienteException("Saldo no disponible")`
4. Guardar movimiento con `saldo = nuevoSaldo`
5. Actualizar `cuenta.saldoInicial = nuevoSaldo`

Esto debe ser transaccional (`@Transactional`).

---

## 7) Manejo de errores (replicar estilo)

En `ms-clientes` ya existe `GlobalExceptionHandler` con estructura `ApiError`.
Replicar misma idea en `ms-cuentas`:
- `ResourceNotFoundException` -> 404
- `DuplicateResourceException` -> 409
- `SaldoInsuficienteException` -> 400 (mensaje: `Saldo no disponible`)
- validaciones DTO -> 400 con detalle por campo
- fallback general -> 500

Payload sugerido (igual estilo):
```json
{
  "timestamp": "2026-03-26T18:30:00",
  "status": 400,
  "error": "Bad Request",
  "message": "Saldo no disponible",
  "validationErrors": null
}
```

---

## 8) Pruebas minimas para pasar rubric

### Unitarias
- `MovimientoServiceImplTest`
  - happy path de deposito/retiro
  - saldo insuficiente (debe lanzar excepcion)

### Integracion
- `MovimientoControllerIT` con `@SpringBootTest` + `@AutoConfigureMockMvc` + H2
  - flujo endpoint `POST /api/movimientos`
  - caso saldo insuficiente retorna 400 y mensaje exacto

### Recomendado adicional Senior
- prueba de consumer RabbitMQ (o test de slice de listener)
- prueba de reporte con rango de fechas

---

## 9) SQL / Base de datos

Ya existe `BaseDatos.sql` en este repo para `ms-clientes`.
Para `ms-cuentas` crear su script equivalente con:
- tabla `cliente_ref`
- tabla `cuentas`
- tabla `movimientos`
- indices por `cliente_id`, `numero_cuenta`, `fecha`

Sugerencia de constraints:
- `numero_cuenta` unique
- checks para tipos validos (si se decide enum en DB)

---

## 10) Docker y conectividad

En `docker-compose.yml` actual:
- `postgres-clientes` y `postgres-cuentas` ya estan declarados
- `rabbitmq` ya esta declarado
- `ms-clientes` activo
- `ms-cuentas` esta como placeholder comentado

Para terminar:
1. Descomentar `ms-cuentas`
2. Ajustar su `Dockerfile`
3. Variables env de `SPRING_DATASOURCE_*` y `SPRING_RABBITMQ_*`
4. `depends_on` con `postgres-cuentas` + `rabbitmq` healthy

---

## 11) Definition of Done (Senior)

Checklist final:
- [ ] CRUD completo de `clientes`, `cuentas`, `movimientos`
- [ ] Regla de saldo insuficiente con mensaje exacto
- [ ] Reporte por rango y cliente funcionando
- [ ] Comunicacion asincrona `ms-clientes -> RabbitMQ -> ms-cuentas`
- [ ] Sin MapStruct (mapeo manual consistente)
- [ ] Excepciones globales uniformes
- [ ] Unit tests + integration tests pasando
- [ ] Docker compose levanta todos los servicios
- [ ] Evidencia en Postman (coleccion + ejemplos)

---

## 12) Orden recomendado de ejecucion

1. Implementar entidades + repositorios en `ms-cuentas`
2. Implementar mappers manuales + DTOs
3. Implementar servicios (reglas F2/F3)
4. Implementar controllers REST
5. Implementar consumer RabbitMQ de `ClienteEvent`
6. Implementar endpoint de reportes
7. Agregar pruebas (unit + integracion)
8. Ajustar Dockerfile y docker-compose
9. Probar flujo completo desde Postman

---

## 13) Nota para entrevista tecnica

Si preguntan por que dos BDs y eventos asincronos:
- reduce acoplamiento entre microservicios
- evita dependencia runtime por HTTP entre dominios
- mejora resiliencia y escalabilidad
- permite evolucion independiente de cada micro

Si preguntan por que mapeo manual:
- menor complejidad operativa para el equipo actual
- depuracion directa sin codigo generado
- suficiente para alcance de este ejercicio

