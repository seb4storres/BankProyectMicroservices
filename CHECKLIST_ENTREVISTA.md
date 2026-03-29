# ✅ CHECKLIST DE DEMOSTRACIÓN - Imprime esto

## Antes de la Entrevista

- [ ] Tener Docker instalado y funcionando
- [ ] Tener Postman instalado (opcional, pero recomendado)
- [ ] Clonar el proyecto: `git clone <tu-repo>`
- [ ] Probar una vez localmente para asegurar que funciona
- [ ] Tener 2 terminales abiertas (una para Docker, otra para requests)
- [ ] Copiar la colección Postman a Postman (opcional)

---

## Durante la Entrevista (5 minutos)

### FASE 1: LEVANTAR INFRAESTRUCTURA (1 minuto)

```powershell
docker compose up --build
```

**Esperar a ver:** `Started MsCuentasApplication`

**Mientras se levanta, explica:**
> "Tengo un docker-compose.yml que levanta 3 contenedores:
>  1. Base de datos para ms-clientes
>  2. Base de datos para ms-cuentas  
>  3. RabbitMQ para comunicación asincrónica"

---

### FASE 2: VERIFICAR CONECTIVIDAD (30 segundos)

En otra terminal:

```powershell
curl http://localhost:8080/api/clientes
curl http://localhost:8081/api/cuentas
```

**Debe retornar:** `[]`

**Explica:**
> "Ambos servicios están respondiendo correctamente. Los arrays están vacíos porque es la primera vez."

---

### FASE 3: FLUJO PRINCIPAL (3 minutos)

#### Paso 1: Crear Cliente (30 seg)

```powershell
curl -X POST http://localhost:8080/api/clientes `
  -H "Content-Type: application/json" `
  -d '{"nombre":"Juan","apellido":"Torres","email":"juan@example.com","telefono":"3001234567"}'
```

**Anota el ID retornado (generalmente es 1)**

**Explica:**
> "POST a /api/clientes. El cliente se crea en ms-clientes y automáticamente publica un evento de ClienteCreado."

---

#### Paso 2: Crear Cuenta (30 seg)

```powershell
curl -X POST http://localhost:8081/api/cuentas `
  -H "Content-Type: application/json" `
  -d '{"numeroCuenta":"1234567890","tipoCuenta":"AHORRO","saldoInicial":5000,"clienteId":1}'
```

**Anota el ID (generalmente 1)**

**Explica:**
> "ms-cuentas escuchó el evento del cliente creado y ahora puede crear una cuenta para él."

---

#### Paso 3: Crear Movimiento (30 seg)

```powershell
curl -X POST http://localhost:8081/api/movimientos `
  -H "Content-Type: application/json" `
  -d '{"cuentaId":1,"tipoMovimiento":"DEPOSITO","monto":2000,"fecha":"2026-03-28"}'
```

**Explica:**
> "Registramos un movimiento. El saldo se actualiza automáticamente."

---

#### Paso 4: Ver Reporte (30 seg)

```powershell
curl "http://localhost:8081/api/reportes?fecha=2026-03-01,2026-03-31&clienteId=1"
```

**Explica:**
> "El reporte muestra todos los movimientos de Juan en el rango de fechas. Aquí vemos la integración de todos los componentes funcionando."

---

### FASE 4: BONUS - Pruebas (Opcional, +2 min)

```powershell
cd .\ms-clientes
.\mvnw.cmd test
# Esperar a que termine

cd ..\ms-cuentas
.\mvnw.cmd test
```

**Explica mientras se ejecutan:**
> "Tengo pruebas unitarias e integración. Estos tests validan la lógica de negocio."

---

## 📊 Lo que Demostraste (Copiar/Parafrasear)

- ✅ **Arquitectura de Microservicios** - 2 servicios independientes
- ✅ **Comunicación Asincrónica** - RabbitMQ conectando servicios
- ✅ **Docker & DevOps** - Todo en contenedores, 1 comando para levantar
- ✅ **API REST** - Endpoints CRUD funcionando
- ✅ **Persistencia** - Datos en base de datos
- ✅ **Reportes** - Queries complejas con filtros
- ✅ **Testing** - Pruebas automatizadas
- ✅ **Escalabilidad** - Servicios independientes pueden escalar por separado

---

## 🎯 Preguntas Esperadas y Respuestas

| Pregunta | Respuesta |
|----------|-----------|
| "¿Cómo se comunican?" | "RabbitMQ. Eventos publicador-suscriptor" |
| "¿Por qué 2 BD?" | "Independencia y escalabilidad horizontal" |
| "¿Qué pasa si falla ms-clientes?" | "ms-cuentas sigue funcionando" |
| "¿Cómo garantizas consistencia?" | "RabbitMQ + transacciones locales" |
| "¿Es escalable?" | "Sí, cada servicio escala independientemente" |

---

## ⚡ Si Algo Sale Mal

### Error de conexión a localhost:8080/8081
```powershell
# Ver logs
docker compose logs ms-clientes
docker compose logs ms-cuentas
```

### Puerto ya en uso
```powershell
docker compose down
docker system prune -a
docker compose up --build
```

### RabbitMQ no conecta
- Espera 30 segundos más después de `docker compose up`
- RabbitMQ es el más lento en levantar

---

## 📱 Alternativa Visual: Postman

Si prefieres visual:

1. Abre Postman
2. Import → `postman/proyecto-bancario.postman_collection.json`
3. Ejecuta: Crear Cliente → Crear Cuenta → Crear Movimiento → Ver Reporte

**Ventaja:** Los datos se ven más bonito en Postman  
**Desventaja:** Menos "developer" que cURL

---

## ⏱️ Timeline de la Demostración

```
0:00  - Explicar arquitectura
0:30  - Levantar docker compose up --build
1:30  - Servicios listos
2:00  - Verificar conectividad
2:30  - Crear cliente
3:00  - Crear cuenta
3:30  - Crear movimiento
4:00  - Ver reporte
4:30  - Explicar lo que vio
5:00  - Fin
```

**Si sobra tiempo:** Ejecutar tests o mostrar código

---

## 🎬 Frases de Cierre

Después de que todo funcione, cierra con una de estas:

> "Como ves, todo funciona seamlessly. Los microservicios están desacoplados, escalamos independientemente, y la comunicación es asincrónica para mejor performance."

> "Esta arquitectura nos permite que cada equipo trabaje en su servicio sin interferencias. Si necesitamos cambiar la lógica de clientes, no afecta a cuentas."

> "Docker hace que anyone pueda clonar esto y en un comando tener todo corriendo. No hay dependencias de máquinas locales."

---

## 📝 Notas Finales

- **Tiempo total:** 5-7 minutos
- **Lo más importante:** Que funcione
- **Lo segundo más importante:** Que entiendas qué está pasando
- **Practica 1 vez antes:** Para que baje el nerviosismo
- **Sé flexible:** Si algo tarda, ajusta la demostración

**¡Buena suerte en tu entrevista!** 🚀


