# 🎯 INSTRUCTIVO DE DEMOSTRACIÓN - INICIO RÁPIDO

> **Lee esto primero antes de la entrevista**

---

## 📌 PARA LEER EN 5 MINUTOS

Este proyecto demuestra **Microservicios bancarios con Docker, RabbitMQ y APIs REST**.

### Lo que tienes que hacer en la entrevista:

1. **Levanta:** `docker compose up --build` (1 minuto)
2. **Verifica:** `curl http://localhost:8080/api/clientes` (30 segundos)
3. **Demo:** Crea cliente → Cuenta → Movimiento → Ve reporte (3 minutos)
4. **Explica:** Por qué esta arquitectura es profesional (30 segundos)

**Total: 5 minutos. Luego contesta preguntas.**

---

## 📚 DOCUMENTOS QUE CREÉ PARA TI

Tienes **5 documentos nuevos** que facilitan la demostración:

| Documento | Cuándo Leerlo | Propósito |
|-----------|--------------|----------|
| **CHEAT_SHEET.md** | Ahora mismo | 1 página con todo lo esencial |
| **GUIA_DEMOSTRACION.md** | 1 hora antes | Guía completa con explicaciones |
| **CHECKLIST_ENTREVISTA.md** | Imprime y ten a mano | Paso a paso durante la call |
| **GUIA_VISUAL.md** | Si necesitas ayuda | Diagramas y tablas |
| **demo-rapida.ps1** | Para pruebas | Script automático |

---

## ⚡ VERSIÓN MUY RÁPIDA

### Si tienes 2 minutos:
Lee **CHEAT_SHEET.md**

### Si tienes 30 minutos:
Lee **GUIA_DEMOSTRACION.md**

### Si tienes 1 hora:
Lee todo en orden:
1. RESUMEN_EJECUTIVO.md (5 min)
2. GUIA_DEMOSTRACION.md (15 min)
3. CHECKLIST_ENTREVISTA.md (10 min)
4. GUIA_VISUAL.md (20 min)
5. Práctica local (15 min)

---

## 🎬 ORDEN DE LECTURA RECOMENDADO

### AHORA (5 minutos):
```
1. Lee esta página
2. Lee CHEAT_SHEET.md
```

### 1 HORA ANTES DE LA ENTREVISTA:
```
3. Lee GUIA_DEMOSTRACION.md completo
4. Imprime CHECKLIST_ENTREVISTA.md
```

### DURANTE LA ENTREVISTA:
```
5. Sigue paso a paso CHECKLIST_ENTREVISTA.md
6. Si algo falla, consulta GUIA_VISUAL.md
```

---

## 📋 CONTENIDO DE CADA DOCUMENTO

### 1. CHEAT_SHEET.md ⚡
- Comandos exactos a ejecutar
- Timing de cada paso
- Respuestas cortas a preguntas
- Solución de problemas en 1 línea

### 2. GUIA_DEMOSTRACION.md 📖
- Explicación detallada
- Puntos clave sobre la arquitectura
- Preguntas esperadas y respuestas largas
- Tips profesionales

### 3. CHECKLIST_ENTREVISTA.md ✅
- Checklist pre-entrevista
- Paso a paso numerado
- Qué dices en cada punto
- Frases de cierre

### 4. GUIA_VISUAL.md 📊
- Diagramas ASCII de arquitectura
- Tablas de puertos y endpoints
- Flujos visuales
- Respuestas esperadas en JSON

### 5. demo-rapida.ps1 ⚙️
- Script PowerShell automático
- Ejecuta toda la demostración
- Valida que todo funciona

---

## 🚀 EJECUCIÓN TÍPICA

```powershell
# Terminal 1: Levantar Docker (1 minuto)
docker compose up --build

# Terminal 2: Esperar a que Docker esté listo, luego...
curl http://localhost:8080/api/clientes                           # GET
curl -X POST http://localhost:8080/api/clientes ... (json body)   # POST Cliente
curl -X POST http://localhost:8081/api/cuentas ... (json body)    # POST Cuenta
curl -X POST http://localhost:8081/api/movimientos ... (json)     # POST Movimiento
curl "http://localhost:8081/api/reportes?fecha=...&clienteId=1"   # GET Reporte
```

**Eso es literalmente todo.** Los comandos exactos están en CHEAT_SHEET.md.

---

## 💡 PUNTOS CLAVE A COMUNICAR

Mientras demuestras, asegúrate de mencionar:

1. **"Docker levanta todo en 1 comando"** (Infrastructure as Code)
2. **"Dos servicios independientes en puertos diferentes"** (Separación de concerns)
3. **"RabbitMQ los comunica de forma asincrónica"** (Escalabilidad)
4. **"Cada servicio tiene su propia BD"** (Verdadero desacoplamiento)
5. **"El reporte integra datos de ambos"** (Sinergia)

---

## ✅ CHECKLIST PRE-ENTREVISTA

- [ ] Leer CHEAT_SHEET.md
- [ ] Leer GUIA_DEMOSTRACION.md
- [ ] Tener Docker funcionando
- [ ] Probar 1 vez localmente
- [ ] Imprimir CHECKLIST_ENTREVISTA.md
- [ ] Tener terminales abiertas
- [ ] Conocer los comandos de memoria

---

## 🎯 QUÉS DEMUESTRAS

✅ **Arquitectura:** Microservicios reales  
✅ **Tecnología:** Spring Boot, Docker, RabbitMQ, MySQL  
✅ **APIs:** Endpoints REST completos  
✅ **Databases:** Múltiples BD independientes  
✅ **Messaging:** Comunicación asincrónica  
✅ **DevOps:** Infrastructure as Code  
✅ **Testing:** Tests automatizados incluidos  
✅ **Documentación:** Profesional y completa  

---

## ⏱️ TIMING PERFECTO

```
0:00 - Introducción (30 seg)
0:30 - Docker compose up (1:00 min)
1:30 - Verificar conectividad (0:30 seg)
2:00 - Crear cliente (0:30 seg)
2:30 - Crear cuenta (0:30 seg)
3:00 - Crear movimiento (0:30 seg)
3:30 - Ver reporte (0:30 seg)
4:00 - Explicar arquitectura (1:00 min)
5:00 - Listo para preguntas
```

---

## 🔥 SI ALGO SALE MAL

| Problema | Solución |
|----------|----------|
| "Port 8080 already in use" | `docker compose down && docker system prune -a` |
| RabbitMQ timeout | Espera 30 segundos más |
| 404 Not Found | Verifica que usas puerto correcto (8080 vs 8081) |
| Otros | Ver GUIA_VISUAL.md sección "Solución de problemas" |

---

## 📱 ALTERNATIVA: POSTMAN

Si prefieres algo más visual:

```
1. Abre Postman
2. Import → postman/proyecto-bancario.postman_collection.json
3. Ejecuta: Cliente → Cuenta → Movimiento → Reporte
```

**Ventaja:** Más bonito visualmente  
**Desventaja:** Menos "developer"

---

## 🎙️ CÓMO EXPLICAR RÁPIDO

Si te preguntan "Resume tu proyecto en 30 segundos":

> "Es un sistema bancario con 2 microservicios.
> Uno maneja clientes (ms-clientes) en puerto 8080.
> Otro maneja cuentas y movimientos (ms-cuentas) en puerto 8081.
> Se comunican vía RabbitMQ de forma asincrónica.
> Cada uno tiene su propia base de datos MySQL.
> Todo está containerizado con Docker para reproducibilidad.
> Tienes tests automatizados y APIs REST completas."

---

## 🏆 QUÉ TE DIFERENCIA

Con esta demostración muestras que:

✓ Entiendes microservicios reales (no solo teoría)  
✓ Sabes usar Docker en producción  
✓ Comprendes messaging asincrónico  
✓ Sabes documentar profesionalmente  
✓ Puedes comunicar complejidad técnica fácilmente  

---

## 🎁 BONUS

### Si te queda tiempo, demuestra también:

```powershell
# Tests
cd .\ms-clientes
.\mvnw.cmd test
```

Esto muestra que tienes **testing automatizado** 💪

---

## 📞 DUDA RÁPIDA?

Busca en:
1. **CHEAT_SHEET.md** - Respuestas en 1 línea
2. **GUIA_VISUAL.md** - Diagramas y tablas
3. **GUIA_DEMOSTRACION.md** - Explicaciones completas

---

## 🚀 ESTÁS 100% LISTO

Tu proyecto tiene:
- ✅ README completo y profesional
- ✅ 5 documentos de demostración
- ✅ Ejemplos con datos reales
- ✅ Arquitectura escalable
- ✅ Tests automatizados
- ✅ Docker listo

**No hay nada más que preparar.** Solo práctica y confianza.

---

## 📊 PRÓXIMO PASO

**→ Lee CHEAT_SHEET.md ahora (5 minutos)**

Después, lee GUIA_DEMOSTRACION.md una hora antes de la entrevista.

---

**¡Mucho éxito en tu entrevista!** 🎉


