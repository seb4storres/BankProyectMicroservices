# Proyecto Bancario - Microservicios

Implementacion del ejercicio con dos microservicios Spring Boot y comunicacion asincronica por RabbitMQ.

## Objetivo de esta guia

Este README esta pensado para un entrevistador: levantar el sistema desde cero, ejecutar un flujo funcional completo y validar que la arquitectura funciona.

## Arquitectura

- `ms-clientes` (puerto `8080`): CRUD de clientes.
- `ms-cuentas` (puerto `8081`): cuentas, movimientos y reporte.
- Mensajeria: RabbitMQ (`ms-clientes` publica eventos de cliente, `ms-cuentas` consume).
- Persistencia: PostgreSQL separado por servicio.
- Orquestacion: `docker-compose.yml` en la raiz.

## Requisitos

### Opcion recomendada (Docker)

- Docker Desktop con Compose habilitado.
- Cliente HTTP: Postman o `curl.exe`.

### Opcion local (sin Docker para apps)

- Java 17 (el proyecto compila con Spring Boot 3.2.x).
- Maven 3.8+.
- PostgreSQL y RabbitMQ ejecutandose localmente.

## Inicio rapido para entrevista (5-10 minutos)

### 1) Levantar todo con Docker

Desde `ProyectoBancJuanTorres`:

```powershell
docker compose up --build
```

Cuando termine el arranque, deja esta terminal abierta.

### 2) Verificar que responde

En otra terminal:

```powershell
curl.exe http://localhost:8080/api/clientes
curl.exe http://localhost:8081/api/cuentas
```

Si responde `[]` o una lista JSON, los servicios estan arriba.

### 3) Flujo funcional completo (cliente -> cuenta -> movimiento -> reporte)

#### 3.1 Crear cliente

```powershell
curl.exe -X POST "http://localhost:8080/api/clientes" `
  -H "Content-Type: application/json" `
  -d '{"nombre":"Jose Lema","genero":"Masculino","edad":35,"identificacion":"1234567890","direccion":"Otavalo sn y principal","telefono":"098254785","contrasena":"1234","estado":true}'
```

Guarda el valor `clienteId` de la respuesta (ejemplo: `1`).

#### 3.2 Crear cuenta asociada al cliente

```powershell
curl.exe -X POST "http://localhost:8081/api/cuentas" `
  -H "Content-Type: application/json" `
  -d '{"numeroCuenta":"478758","tipoCuenta":"Ahorro","saldoInicial":2000,"estado":true,"clienteId":1}'
```

#### 3.3 Registrar movimiento

```powershell
curl.exe -X POST "http://localhost:8081/api/movimientos" `
  -H "Content-Type: application/json" `
  -d '{"numeroCuenta":"478758","tipoMovimiento":"Retiro","valor":-575}'
```

#### 3.4 Consultar reporte

```powershell
curl.exe "http://localhost:8081/api/reportes?fecha=2026-03-01,2026-03-31&clienteId=1"
```

Si el flujo responde correctamente, la demostracion funcional esta completa.

## Opcion con Postman

1. Abrir Postman.
2. Importar `postman/proyecto-bancario.postman_collection.json`.
3. Ejecutar en este orden:
   - `Clientes - Crear`
   - `Cuentas - Crear`
   - `Movimientos - Crear`
   - `Reporte por rango`

## Ejecucion local sin Docker (apps en local)

1. Levantar PostgreSQL y RabbitMQ.
2. Iniciar `ms-clientes`:

```powershell
cd .\ms-clientes
.\mvnw.cmd spring-boot:run
```

3. En otra terminal, iniciar `ms-cuentas`:

```powershell
cd .\ms-cuentas
.\mvnw.cmd spring-boot:run
```

4. Ejecutar el mismo flujo funcional de la seccion anterior.

## Pruebas automatizadas

```powershell
cd .\ms-clientes
.\mvnw.cmd test

cd ..\ms-cuentas
.\mvnw.cmd test
```

## Endpoints principales

### `ms-clientes` (`8080`)

- `GET /api/clientes`
- `GET /api/clientes/{id}`
- `POST /api/clientes`
- `PUT /api/clientes/{id}`
- `PATCH /api/clientes/{id}`
- `DELETE /api/clientes/{id}`

### `ms-cuentas` (`8081`)

- `GET /api/cuentas`
- `POST /api/cuentas`
- `GET /api/movimientos`
- `POST /api/movimientos`
- `GET /api/reportes?fecha=yyyy-MM-dd,yyyy-MM-dd&clienteId={id}`

## Puertos usados

- `8080`: API de clientes
- `8081`: API de cuentas y movimientos
- `5432`: PostgreSQL de clientes
- `5433`: PostgreSQL de cuentas
- `5672`: RabbitMQ
- `15672`: RabbitMQ Management

## Archivos utiles para entrevista

- `INDEX.md`: orden de lectura general.
- `INICIO_RAPIDO.md`: resumen operativo.
- `CHECKLIST_ENTREVISTA.md`: guia paso a paso para la demo.
- `CHEAT_SHEET.md`: comandos listos para copiar.


